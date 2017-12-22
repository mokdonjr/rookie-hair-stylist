package yapp.devcamp.hairstylistserver.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
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
import yapp.devcamp.hairstylistserver.model.Postscript;
import yapp.devcamp.hairstylistserver.model.Product;
import yapp.devcamp.hairstylistserver.model.ProductOption;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.service.EmailService;
import yapp.devcamp.hairstylistserver.service.PostscriptService;
import yapp.devcamp.hairstylistserver.service.ShopService;
import yapp.devcamp.hairstylistserver.service.StorageService;
import yapp.devcamp.hairstylistserver.service.UserService;
import yapp.devcamp.hairstylistserver.utils.StringUtil;

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
	
	@Autowired
	private PostscriptService postscriptService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * select Shop by shopCode
	 */
	@RequestMapping(value="/{shopCode}", method=RequestMethod.GET)
	public ModelAndView selectByShopCode(@PathVariable("shopCode") int shopCode,HttpServletRequest request) throws IOException{
		ModelAndView mv = new ModelAndView();
		
		Shop shop = shopService.selectShopByShopCode(shopCode);
		
		//썸네일 URL 만들기
		String url = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "serveShopImage", shop.getStylist().getStylistCode(), shop.getShopName().replaceAll(" ", ""), "thumbnail.jpg")
				.build().toString();
		shop.setImagePath(url);
		
		//포트폴리오 Url 만들기
		String portfolioPath = getUploadedImage(shop.getStylist().getStylistCode(), shop.getShopName().replaceAll(" ", ""));
		File file = new File(portfolioPath);
		File[] fileList = file.listFiles();

		//포트폴리오 사진 가져오기
		if(fileList != null) {
			String[] getPort = new String[fileList.length-1];
			for(int i=0;i<fileList.length;i++){
				String filename = fileList[i].getName();
				if(!filename.equals("thumbnail.jpg")){
					String portfolioUrl = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "serveShopImage", shop.getStylist().getStylistCode(), shop.getShopName().replaceAll(" ", ""), filename)
										.build().toString();
					getPort[i] = portfolioUrl;
				}
			}
			shop.setPortfolioImg(getPort);
		}
		
		//후기 평균 계산
		List<Postscript> postscriptList = postscriptService.selectAll(shop);
		float sum=0;
		for(Postscript postscript : postscriptList){
			float grade = postscript.getGrade(); 
			sum += grade;
			postscript.setOpinion(makeOpinion((int)grade));
			postscript.setUser(userService.findById(postscript.getUser().getId()));
		}
		int count = postscriptList.size();
		float avg = (int)((sum/count)*10)/10.0f;
		String opinion = makeOpinion((int)avg);
		
		List<Product> pList = shopService.findByshopCode(shop);
		List<ProductOption> oList = shopService.findByshopCodeOption(shop);
		
		shop.setProducts(pList);
		shop.setOptions(oList);
		DecimalFormat df = new DecimalFormat("#,###");
		
		for(Product pro : pList){
			pro.setProductPriceStr(df.format(pro.getProductPrice()));
		}
		for(ProductOption op : oList){
			op.setOptionPriceStr(df.format(op.getOptionPrice()));
		}
		
		Stylist stylist = shop.getStylist();
		
		mv.setViewName("sales_detail");
		mv.addObject("shop", shop);
		mv.addObject("stylist", stylist);
		mv.addObject("postscripts",postscriptList);
		mv.addObject("average", avg);
		mv.addObject("count", count);
		mv.addObject("opinion", opinion);
		return mv;
	}
	
	private String makeOpinion(int avg){
		String result = "";
		switch(avg){
			case 5 : result="\"완전 대박이에요!!\""; break;
			case 4 : result="\"완전 좋아요!\""; break;
			case 3 : result="\"마음에 들어요\""; break;
			case 2 : result="\"괜찮은 편이에요\""; break;
			case 1 : result="\"별로에요\""; break;
			case 0 : result="\"완전 망했어요\""; break;
		}
		return result;
	}
	
	
	/**
	 * Shop enroll method
	 */
	@RequestMapping(value="/enroll", method=RequestMethod.POST)
	public String enroll(@Valid Shop shop, BindingResult result, HttpServletRequest request, Model model) 
								throws IOException {
		
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
		
		Stylist stylist = (Stylist) request.getSession().getAttribute("stylist");
		
		if(shop != null){
			MultipartFile shopImage = shop.getShopImage();
			storageService.storeShopImage(stylist.getStylistCode(), shop.getShopName().replaceAll(" ", ""), shopImage);
			
			//포트폴리오 이미지 업로드
			MultipartFile[] pImages = shop.getPortfolio();
			for(MultipartFile pImage : pImages){
				storageService.storePortfolioImage(stylist.getStylistCode(), shop.getShopName().replaceAll(" ", ""), pImage);
			}
			
			// save imagepaths
			String imagePath = null;
			try {
				imagePath = getUploadedImage(stylist.getStylistCode(), shop.getShopName().replaceAll(" ", ""));
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
		
		// send enroll email
		String requestURL = request.getRequestURL().toString();
		String baseURL = StringUtil.getBaseURL(requestURL);
		try{
			emailService.sendCreatedShopEmail(baseURL, shop);	
		} catch(MailException | InterruptedException e) {
			logger.warn("Error sending email : " + e.getMessage());
		}
		
		return "redirect:/stylist/designerInfo/"+stylist.getStylistCode();
	}
	
	public String getUploadedImage(int stylistCode, String shopName) throws IOException {
		logger.warn("getUploadedImage 메서드 진입");
		Path path = storageService.shopLoad(stylistCode, shopName);
		return path.toString();
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
		// set shop status
		shop.setShopStatus(ShopStatus.OPENED);
		
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
		shop.setStylist(stylist);
		
		List<Product> products = shop.getProducts();
		List<ProductOption> options = shop.getOptions();
		shopService.saveProduct(products, shop);
		shopService.saveOption(options, shop);
		
		try {
			shopService.saveShop(shop);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String requestURL = request.getRequestURL().toString();
		String baseURL = StringUtil.getBaseURL(requestURL);
		try{
			emailService.sendEditShopEmail(baseURL, shop);	
		} catch(MailException | InterruptedException e) {
			logger.warn("Error sending email : " + e.getMessage());
		}
		return "redirect:/stylist/mypage";
	}
	
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
	

//	@GetMapping("/chat/{shopCode}")
//	public String chat(@PathVariable int shopCode, Model model){
//		Shop shop = shopService.selectShopByShopCode(shopCode);
//		model.addAttribute("shop", shop);
//		return "chat";
//	}
	
	
}
