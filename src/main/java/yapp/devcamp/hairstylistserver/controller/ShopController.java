package yapp.devcamp.hairstylistserver.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import yapp.devcamp.hairstylistserver.enums.ShopStatus;
import yapp.devcamp.hairstylistserver.model.Book;
import yapp.devcamp.hairstylistserver.model.Product;
import yapp.devcamp.hairstylistserver.model.ProductOption;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.service.EmailService;
import yapp.devcamp.hairstylistserver.service.ShopService;
import yapp.devcamp.hairstylistserver.service.StorageService;

/**
 * Shop Management Controller
 */
@Controller
@RequestMapping("/shop")
public class ShopController {
	Logger logger = LoggerFactory.getLogger(ShopController.class);
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private EmailService emailService;
	
	/**
	 * Shop delete method
	 */
	@RequestMapping(value="/{shopCode}",method=RequestMethod.DELETE)
	public String delete(@PathVariable("shopCode") int shopCode){
		Shop resultShop = shopService.selectShopByShopCode(shopCode);
		if(resultShop != null){
			shopService.deleteShop(resultShop);
		}
		return "redirect:home";
	}
	
	/**
	 * select Shop by shopCode
	 */
	@RequestMapping(value="/{shopCode}", method=RequestMethod.GET)
	public ModelAndView selectByShopCode(@PathVariable("shopCode") int shopCode){
		ModelAndView mv = new ModelAndView();
		Shop shop = shopService.selectShopByShopCode(shopCode);
		Stylist stylist = shop.getStylist();
		//수정 페이지 정해지면 수정할 것
		mv.setViewName("sales_detail");
		mv.addObject("shop", shop);
		mv.addObject("stylist", stylist);
		return mv;
	}
	
	/**
	 * Shop selectAll
	 */
	@RequestMapping("/home")
	public ModelAndView selectAll(){
		List<Shop> shopList = shopService.selectAllShop();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		mv.addObject("shopList", shopList);
		return mv;
	}
	
	/**
	 * Shop enroll method
	 */
//	@RequestMapping(value="/enroll", method=RequestMethod.POST)
//	public String enroll(Shop shopModel
//			,MultipartFile[] thumbnail,Product product,ProductOption productOption) throws IOException{
//		if(shopModel != null){
//			shopService.saveShop(shopModel,thumbnail);
//			Shop resultShop = shopService.selectShopByShopName(shopModel.getShopName());
//			if(product != null)
//				shopService.saveProduct(product.getProductList(), resultShop);
//			if(productOption != null)
//				shopService.saveOption(productOption.getOptionList(), resultShop);
//		}
//		
//		return "redirect:/stylist/designerInfo";
//	}
	
	@PostMapping(value="/enroll")
	public String enrollShop(@Valid Shop shop, BindingResult result, HttpServletRequest request, Model model) {
		
		Stylist stylist = (Stylist) request.getSession().getAttribute("stylist");
		int stylistCode = stylist.getStylistCode();
		
		if(result.hasErrors()){
			logger.debug("From data has errors");
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				error.getDefaultMessage();
			}
			return "shop_create";
		}
		
		// if shop already exists
		String shopName = shop.getShopName();
		if(shopService.isAlreadyEnrollShopName(shopName)){
			model.addAttribute("alreadyExistShop", shopName);
			return "shop_create";
		}
		
		// shopCode >= 1
		if(!shopService.isExistAnyShop()){
			shop.setShopCode(1);
		}
		
		// save images
		MultipartFile shopImage = shop.getShopImage();
		storageService.storeShopImage(stylistCode, shopName, shopImage);
		
		// save imagepaths
		String imagePath = null;
		try {
			imagePath = getUploadedImage(stylistCode, shopName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		shop.setImagePath(imagePath);

		// set products, options, stylist FKs
		List<Product> products = shop.getProducts();
		for(Product product : products){
			product.setShop(shop);
		}
		// set options
		List<ProductOption> options = shop.getOptions();
		for(ProductOption option : options){
			option.setShop(shop);
		}
		shop.setStylist(stylist);
		
		// set shopStatus
		shop.setShopStatus(ShopStatus.OPENED); // 신고 또는 영업종료시 CLOSED
		shopService.saveShop(shop);
		
		// send enroll email
		try{
			emailService.sendCreatedShopEmail(shop);	
		} catch(MailException | InterruptedException e) {
			logger.warn("Error sending email : " + e.getMessage());
		}
		
		return "redirect:/stylist/mypage";
	}
	
	@GetMapping("/enroll")
	public String enrollShopPage(Model model, HttpSession session){
		Stylist stylist = (Stylist) session.getAttribute("stylist");
		
		Shop shop = new Shop();
		shop.setStylist(stylist);
		
		shop.setLocation(stylist.getLocation() + " " + stylist.getDetailLocation());
		
		model.addAttribute("shop", shop);
		
		return "shop_create";
	}
	
	@GetMapping("/edit/{shopCode}")
	public String editShopPage(@PathVariable("shopCode") int shopCode, Model model){
		
		Shop shop = shopService.selectShopByShopCode(shopCode);
		model.addAttribute("shop", shop);
		
		return "editShop";
	}
	
	@PostMapping("/edit")
	public String editShop(@Valid Shop shop, BindingResult result, HttpServletRequest request){
		
		Stylist stylist = (Stylist) request.getSession().getAttribute("stylist");
		int stylistCode = stylist.getStylistCode();
		
		if(result.hasErrors()){
			logger.debug("From data has errors");
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				error.getDefaultMessage();
			}
			return "editShop";
		}
		String shopName = shop.getShopName();
		
		// save images
		MultipartFile shopImage = shop.getShopImage();
		storageService.storeShopImage(stylistCode, shopName, shopImage);
		
		// save imagepaths
		String imagePath = null;
		try {
			imagePath = getUploadedImage(stylistCode, shopName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		shop.setImagePath(imagePath);
		
		// set products, options, stylist FKs
		List<Product> products = shop.getProducts();
		for(Product product : products){
			product.setShop(shop);
		}
		// set options
		List<ProductOption> options = shop.getOptions();
		for(ProductOption option : options){
			option.setShop(shop);
		}
		
		shopService.saveShop(shop);
		
		// null point issue encountered
//		try{
//			emailService.sendEditShopEmail(shop);	
//		} catch(MailException | InterruptedException e) {
//			logger.warn("Error sending email : " + e.getMessage());
//		}
		return "redirect:/stylist/mypage";
	}
	
	public String getUploadedImage(int stylistCode, String shopName) throws IOException {
		logger.warn("getUploadedImage 메서드 진입");
		
		Path path = storageService.loadShopImage(stylistCode, shopName);
		
		String url = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "serveShopImage", stylistCode, shopName, path.getFileName().toString())
						.build().toString();
		logger.warn("getUploadedImage : " + url);
		
		return url;
    }

	@GetMapping("/chat/{shopCode}")
	public String chat(@PathVariable int shopCode, Model model){
		Shop shop = shopService.selectShopByShopCode(shopCode);
		model.addAttribute("shop", shop);
		return "chat";
	}
	
	/**
	 * shop 예약하기
	 */
	@RequestMapping("/book")
	public String book(HttpSession session,Book bookModel) throws Exception{
		if(bookModel != null){
//			User user = (User)session.getAttribute("user");
//			bookModel.setUser(user);
			User user = new User();
			user.setId(1);
			bookModel.setUser(user);
			bookModel.setBookStatus(true);
			shopService.book(bookModel);
		}
		return "index";
	}
	/**
	 * 예약 취소
	 */
	@RequestMapping(value="/cancel/{bookCode}",method=RequestMethod.GET)
	public String cancelBook(@PathVariable("bookCode") int bookCode){
		shopService.cancelBook(bookCode);
		return "index";
	}
	/**
	 * 예약 완료
	 */
	@RequestMapping(value ="/complete/{bookCode}",method=RequestMethod.GET)
	public String conpleteBook(@PathVariable("bookCode") int bookCode){
		shopService.completeBook(bookCode);
		return "index";
	}
}
