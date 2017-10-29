package yapp.devcamp.hairstylistserver.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import yapp.devcamp.hairstylistserver.enums.ShopStatus;
import yapp.devcamp.hairstylistserver.model.Book;
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
	 * select Shop by shopCode
	 */
	@RequestMapping(value="/{shopCode}", method=RequestMethod.GET)
	public ModelAndView selectByShopCode(@PathVariable("shopCode") int shopCode) throws IOException{
		ModelAndView mv = new ModelAndView();
		Shop shop = shopService.selectShopByShopCode(shopCode);
		
		//썸네일 URL 만들기
		String url = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "serveShopImage", shop.getStylist().getStylistCode(), shop.getShopName(), "thumbnail.jpg")
				.build().toString();
		shop.setImagePath(url);
		
		//포트폴리오 Url 만들기
		String portfolioPath = getUploadedImage(shop.getStylist().getStylistCode(), shop.getShopName());
		File file = new File(portfolioPath);
		File[] fileList = file.listFiles();
		String[] getPort = new String[fileList.length-1];
		
		for(int i=0;i<fileList.length;i++){
			String filename = fileList[i].getName();
			if(!filename.equals("thumbnail.jpg")){
				String portfolioUrl = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "serveShopImage", shop.getStylist().getStylistCode(), shop.getShopName(), filename)
									.build().toString();
				getPort[i] = portfolioUrl;
			}
		}
		shop.setPortfolioImg(getPort);
		
		Stylist stylist = shop.getStylist();
		//수정 페이지 정해지면 수정할 것
		mv.setViewName("sales_detail");
		mv.addObject("shop", shop);
		mv.addObject("stylist", stylist);
		return mv;
	}
	
	
	/**
	 * Shop enroll method
	 */
	@RequestMapping(value="/enroll", method=RequestMethod.POST)
	public String enroll(@Valid Shop shop
			,BindingResult result,HttpServletRequest request) throws Exception{
		
		
		if(result.hasErrors()){
			logger.debug("From data has errors");
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				error.getDefaultMessage();
			}
			return "shop_create";
		}
		
		Stylist stylist = (Stylist) request.getSession().getAttribute("stylist");
		
		if(shop != null){
			MultipartFile shopImage = shop.getShopImage();
			storageService.storeShopImage(stylist.getStylistCode(), shop.getShopName(), shopImage);
			
			//포트폴리오 이미지 업로드
			MultipartFile[] pImages = shop.getPortfolio();
			for(MultipartFile pImage : pImages){
				storageService.storePortfolioImage(stylist.getStylistCode(), shop.getShopName(), pImage);
			}
			
			// save imagepaths
			String imagePath = null;
			try {
				imagePath = getUploadedImage(stylist.getStylistCode(), shop.getShopName());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			shop.setImagePath(imagePath);

			shop.setStylist(stylist);
			shop.setShopStatus(ShopStatus.OPENED);
			shopService.saveShop(shop);
			
			Shop resultShop = shopService.selectShopByShopName(shop.getShopName());
			if(shop.getProducts() != null){
				shopService.saveProduct(shop.getProducts(), resultShop);
			}
			if(shop.getOptions() != null)
				shopService.saveOption(shop.getOptions(), resultShop);
		}
		else {
			throw new NullPointerException("shop이 null입니다.");
		}
		
//		// send enroll email
//		String requestURL = request.getRequestURL().toString();
//		String baseURL = StringUtil.getBaseURL(requestURL);
//		try{
//			emailService.sendCreatedShopEmail(baseURL, shop);	
//		} catch(MailException | InterruptedException e) {
//			logger.warn("Error sending email : " + e.getMessage());
//		}
		
		return "redirect:/stylist/mypage";
	}
	
//	public String getUploadedImage(int stylistCode, String shopName) throws IOException {
//		logger.warn("getUploadedImage 메서드 진입");
//		
//		Path path = storageService.loadShopImage(stylistCode, shopName);
//		String url = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "serveShopImage", stylistCode, shopName, path.getFileName().toString())
//						.build().toString();
//		logger.warn("getUploadedImage : " + url);
//				
//		return url;
//    }
	public String getUploadedImage(int stylistCode, String shopName) throws IOException {
		logger.warn("getUploadedImage 메서드 진입");
		
		
		Path path = storageService.shopLoad(stylistCode, shopName);
		return path.toString();
    }
	
//	@PostMapping(value="/enroll")
//	public String enrollShop(@Valid Shop shop, BindingResult result, HttpServletRequest request, Model model) {
//		
//		Stylist stylist = (Stylist) request.getSession().getAttribute("stylist");
//		int stylistCode = stylist.getStylistCode();
//		
//		if(result.hasErrors()){
//			logger.debug("From data has errors");
//			List<ObjectError> errors = result.getAllErrors();
//			for(ObjectError error : errors){
//				error.getDefaultMessage();
//			}
//			return "shop_create";
//		}
//		
//		// if shop already exists
//		String shopName = shop.getShopName();
//		if(shopService.isAlreadyEnrollShopName(shopName)){
//			model.addAttribute("alreadyExistShop", shopName);
//			return "shop_create";
//		}
//		
//		// shopCode >= 1
//		if(!shopService.isExistAnyShop()){
//			shop.setShopCode(1);
//		}
//		
//		// save images
//		MultipartFile shopImage = shop.getShopImage();
//		storageService.storeShopImage(stylistCode, shopName, shopImage);
//		
//		// save imagepaths
//		String imagePath = null;
//		try {
//			imagePath = getUploadedImage(stylistCode, shopName);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		shop.setImagePath(imagePath);
//
//		// set products, options, stylist FKs
//		List<Product> products = shop.getProducts();
//		for(Product product : products){
//			product.setShop(shop);
//		}
//		// set options
//		List<ProductOption> options = shop.getOptions();
//		for(ProductOption option : options){
//			option.setShop(shop);
//		}
//		shop.setStylist(stylist);
//		
//		// set shopStatus
//		shop.setShopStatus(ShopStatus.OPENED); // 신고 또는 영업종료시 CLOSED
//		shopService.saveShop(shop);
//		
//		// send enroll email
//		String requestURL = request.getRequestURL().toString();
//		String baseURL = StringUtil.getBaseURL(requestURL);
//		try{
//			emailService.sendCreatedShopEmail(baseURL, shop);	
//		} catch(MailException | InterruptedException e) {
//			logger.warn("Error sending email : " + e.getMessage());
//		}
//		
//		return "redirect:/stylist/mypage";
//	}
	
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
	
//	@PostMapping("/edit")
//	public String editShop(@Valid Shop shop, BindingResult result, HttpServletRequest request){
//		
//		Stylist stylist = (Stylist) request.getSession().getAttribute("stylist");
//		int stylistCode = stylist.getStylistCode();
//		
//		if(result.hasErrors()){
//			logger.debug("From data has errors");
//			List<ObjectError> errors = result.getAllErrors();
//			for(ObjectError error : errors){
//				error.getDefaultMessage();
//			}
//			return "editShop";
//		}
//		// set shop status
//		shop.setShopStatus(ShopStatus.OPENED);
//		
//		String shopName = shop.getShopName();
//		
//		// save images
//		MultipartFile shopImage = shop.getShopImage();
//		storageService.storeShopImage(stylistCode, shopName, shopImage);
//		
//		// save imagepaths
//		String imagePath = null;
//		try {
//			imagePath = getUploadedImage(stylistCode, shopName);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		shop.setImagePath(imagePath);
//		
//		// set products, options, stylist FKs
//		shop.setStylist(stylist);
//		
//		List<Product> products = shop.getProducts();
//		List<ProductOption> options = shop.getOptions();
//		shopService.saveProduct(products, shop);
//		shopService.saveOption(options, shop);
//		
//		shopService.saveShop(shop);
//		
//		String requestURL = request.getRequestURL().toString();
//		String baseURL = StringUtil.getBaseURL(requestURL);
//		try{
//			emailService.sendEditShopEmail(baseURL, shop);	
//		} catch(MailException | InterruptedException e) {
//			logger.warn("Error sending email : " + e.getMessage());
//		}
//		return "redirect:/stylist/mypage";
//	}
	
	/**
	 * Shop delete method
	 */
//	@RequestMapping(value="/{shopCode}",method=RequestMethod.DELETE)
//	public String delete(@PathVariable("shopCode") int shopCode){
//		Shop resultShop = shopService.selectShopByShopCode(shopCode);
//		if(resultShop != null){
//			shopService.deleteShop(resultShop);
//		}
//		return "redirect:home";
//	}
	

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
			User user = (User)session.getAttribute("user");
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
