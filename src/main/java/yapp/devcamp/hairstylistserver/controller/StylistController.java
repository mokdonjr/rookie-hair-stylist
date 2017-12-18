package yapp.devcamp.hairstylistserver.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import yapp.devcamp.hairstylistserver.exception.StylistAlreadyEnrollException;
import yapp.devcamp.hairstylistserver.model.Book;
import yapp.devcamp.hairstylistserver.model.Postscript;
import yapp.devcamp.hairstylistserver.model.ProductOption;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.oauth.AuthorityType;
import yapp.devcamp.hairstylistserver.service.BookService;
import yapp.devcamp.hairstylistserver.service.EmailService;
import yapp.devcamp.hairstylistserver.service.PostscriptService;
import yapp.devcamp.hairstylistserver.service.ProductService;
import yapp.devcamp.hairstylistserver.service.ShopService;
import yapp.devcamp.hairstylistserver.service.StorageService;
import yapp.devcamp.hairstylistserver.service.StylistService;
import yapp.devcamp.hairstylistserver.service.UserService;
import yapp.devcamp.hairstylistserver.utils.StringUtil;

/**
 * Stylist management Controller
 */
@Controller
@RequestMapping("/stylist")
public class StylistController {

	Logger logger = LoggerFactory.getLogger(StylistController.class);
	
	@Autowired
	private StylistService stylistService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StorageService storageService;
	
	@Value("${spring.http.multipart.max-file-size}")
	private String licenseImageMaxSize;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private PostscriptService postscriptService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ProductService productService;
	
	private static AtomicInteger counter = new AtomicInteger(); // only use applyStylist method
	
	/**
	 * enroll stylist
	 * @return : shopList
	 */
	@GetMapping("/apply")
	public String applyStylistPage(HttpServletRequest request, Model model) {
		User user = getCurrentUser();
		
		Stylist stylist = new Stylist();
		stylist.setStylistNickname(user.getUsername());
		model.addAttribute("stylist", stylist);
		
		model.addAttribute("licenseImageMaxSize", licenseImageMaxSize);

		return "applyStylist";
	}
	
	@PostMapping("/apply")
	public String applyStylist(@Valid Stylist stylist, BindingResult result, HttpServletRequest request){
		
		if(result.hasErrors()){
			logger.debug("From data has errors");
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				error.getDefaultMessage();
			}
			return "applyStylist";
		}
		
		// set FK
		User user = getCurrentUser();
		stylist.setUser(user);
		
		// if already Stylist
		if(stylistService.isAlreadyEnrollUser(user)){
			throw new StylistAlreadyEnrollException(user.getId());
		}
		
		// stylistCode >= 1
		stylist.setStylistCode(counter.incrementAndGet());
		logger.warn("applyStylist - setStylistCode(" + stylist.getStylistCode() + ")");
		
		// store Image
		MultipartFile licenseImage = stylist.getLicenseImage();
		storageService.storeStylistEnrollImage(stylist.getStylistCode(), licenseImage);
		
		// set ImagePath
		String imagePath = null;
		try {
			imagePath = getUploadedImage(stylist.getStylistCode());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		stylist.setLicenseImagePath(imagePath);
		stylistService.saveStylist(stylist);
		
		// send enroll email (심사대기)
		String requestURL = request.getRequestURL().toString();
		String baseURL = StringUtil.getBaseURL(requestURL);
		try{
			emailService.sendApplyStylistEmail(baseURL, user); // User
			emailService.sendAdminStylistEmail(baseURL, user, stylist); // Administer (rookies.yapp@gmail.com)
			
		} catch(MailException | InterruptedException e) {
			logger.warn("Error sending email : " + e.getMessage());
		}
		
		return "redirect:/stylist/enroll";
	}
	
	@GetMapping("/edit")
	public String editStylistPage(HttpSession session, Model model){
		
		Stylist stylist = (Stylist) session.getAttribute("stylist");
		if(stylist == null){
			model.addAttribute("errorMsg", "스타일리스트가 아닙니다.");
			return "error";
		}
		
		model.addAttribute("stylist", stylist);
		
		return "editStylist";
	}
	
	@PostMapping("/edit")
	public String editStylist(@Valid Stylist stylist, BindingResult result, HttpServletRequest request){
		
		if(result.hasErrors()){
			logger.debug("From data has errors");
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				error.getDefaultMessage();
			}
			return "editStylist";
		}
		
		stylistService.saveStylist(stylist);
		
		String requestURL = request.getRequestURL().toString();
		String baseURL = StringUtil.getBaseURL(requestURL);
		try{
			emailService.sendEditStylistEmail(baseURL, stylist);
		} catch(MailException | InterruptedException e) {
			logger.warn("Error sending email : " + e.getMessage());
		}
		
		return "redirect:/stylist/mypage";
	}
	
	public String getUploadedImage(int stylistCode) throws IOException {
		
		Path path = storageService.loadStylistEnrollImage(stylistCode);
		
		String url = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "serveStylistEnrollImage", stylistCode, path.getFileName().toString())
						.build().toString();
		logger.warn("getUploadedImage : " + url);
		
		return url;
    }
	
	/**
	 * current login user
	 * @return
	 */
	private User getCurrentUser(){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String principal = authentication.getName();
		logger.warn(principal);
		
		User user = userService.findByPrincipal(principal);
		return user;
	}
	
	@RequestMapping("/enroll")
	public String enrolledStylist(Model model, HttpSession session){
		
		User user = getCurrentUser();
		
		// update session
		Stylist stylist = stylistService.findStylistByUser(user);
		if(stylist != null){
			session.setAttribute("stylist", stylist);
		}
		
		// after granted STYLIST authority by ADMIN
		if(user.getAuthorityType().equals(AuthorityType.STYLIST.getRoleType())){
			return "redirect:/stylist/designerInfo";
		}
		
		// before granted STYLIST authority
		model.addAttribute("user", user);
		model.addAttribute("stylist", stylist);
		
		return "enrollStylist";
	}
	
	/**
	 * redirect mypage method
	 * @return : stylist shop, portfolio, postscript data 
	 */
	@GetMapping("/mypage")
	public String mypage(HttpSession session, Model model,String type){
		
		Stylist stylist;
		if((stylist = (Stylist) session.getAttribute("stylist")) == null){
			model.addAttribute("errorMsg", "권한이 없습니다");
			return "error";
		}
		
		List<Book> bookList = bookService.stylistBookList(stylist, type);
		
		for(Book book : bookList){
			book.setProduct(productService.selectProductByCode(book.getProductCode()));
			book.setOption(productService.selectOptionByCode(book.getOptionCode()));
		}
		int count = bookList.size();
		
		model.addAttribute("stylist", stylist);
		model.addAttribute("bookList", bookList);
		model.addAttribute("count", count);
		return "designer_order_view";
	}
	/**
	 * 스타일리스트 고객주문 페이지
	 */
	@GetMapping("/designerInfo/{stylistCode}")
	public String showDesignerInfo(@PathVariable("stylistCode") int stylistCode, Model model){
		
		Stylist stylist = stylistService.findStylistByStylistCode(stylistCode);
		List<Shop> shopList = shopService.findByStylist(stylist);
		
		for(Shop shop : shopList){
			//후기 평균 계산
			List<Postscript> postscriptList = postscriptService.selectAll(shop);
			float sum=0;
			for(Postscript postscript : postscriptList){
				float grade = postscript.getGrade(); 
				sum += grade;
			}
			int count = postscriptList.size();
			shop.setCount(count);
			shop.setAvg((int)((sum/count)*10)/10.0f);
		}
		
		model.addAttribute("stylist", stylist);
		model.addAttribute("shopList", shopList);
		
		return "designer_page_sales_list";
	}
	
	/**
	 * 스타일리스트 포트폴리오 페이지
	 */
	@GetMapping("/portfolioInfo/{stylistCode}")
	public String showPortfolio(@PathVariable("stylistCode") int stylistCode, Model model) throws IOException{
		Stylist stylist = stylistService.findStylistByStylistCode(stylistCode);
		List<Shop> shopList = shopService.findByStylist(stylist);
		
		for(Shop shop : shopList){
			//포트폴리오 Url 만들기
			String portfolioPath = getUploadedImage(shop.getStylist().getStylistCode(), shop.getShopName());
			File file = new File(portfolioPath);
			File[] fileList = file.listFiles();
			
			//포트폴리오 사진 가져오기
			if(fileList != null) {
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
			}
		}
		
		model.addAttribute("shopList", shopList);
		model.addAttribute("stylist", stylist);
		
		return "designer_portfolio_list";
	}
	
	public String getUploadedImage(int stylistCode, String shopName) throws IOException {
		logger.warn("getUploadedImage 메서드 진입");
		Path path = storageService.shopLoad(stylistCode, shopName);
		return path.toString();
    }
	
	/**
	 * 스타일리스트 후기 페이지
	 */
	@GetMapping("postscriptInfo/{stylistCode}")
	public String showPostscript(@PathVariable("stylistCode") int stylistCode,Model model){
		Stylist stylist = stylistService.findStylistByStylistCode(stylistCode);
		List<Shop> shopList = shopService.findByStylist(stylist);
		List<List<Postscript>> allList = new ArrayList<>();
		int count=0;
		float sum=0;
		float avg=0;
		
		for(Shop shop : shopList){
			List<Postscript> postList = postscriptService.selectAll(shop);
			int length = postList.size();
			if(length > 0){
				//평균 계산
				for(Postscript post : postList){
					float grade = post.getGrade();
					sum += grade;
					post.setOpinion(makeOpinion((int)grade));
					post.setUser(userService.findById(post.getUser().getId()));
				}
				
				allList.add(postList);
				count += length;
			}
		}
		avg += (int)((sum/count)*10)/10.0f;
		String opinion = makeOpinion((int)avg);
		
		model.addAttribute("opinion", opinion);
		model.addAttribute("count", count);
		model.addAttribute("average", avg);
		model.addAttribute("postList", allList);
		model.addAttribute("stylist", stylist);
		
		return "designer_postscript_list";
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
}
