package yapp.devcamp.hairstylistserver.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import yapp.devcamp.hairstylistserver.annotation.SocialUser;
import yapp.devcamp.hairstylistserver.model.Book;
import yapp.devcamp.hairstylistserver.model.Postscript;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.service.BookService;
import yapp.devcamp.hairstylistserver.service.EmailService;
import yapp.devcamp.hairstylistserver.service.PostscriptService;
import yapp.devcamp.hairstylistserver.service.ProductService;
import yapp.devcamp.hairstylistserver.service.ShopService;
import yapp.devcamp.hairstylistserver.service.StorageService;
import yapp.devcamp.hairstylistserver.service.StylistService;
import yapp.devcamp.hairstylistserver.service.UserService;

/**
 * User management Controller
 */
@Controller
@RequestMapping("/users")
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	private StylistService stylistService; // used in oauthComplete for session
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private PostscriptService postscriptService;
	
	/**
	 * Login user method
	 */
	@GetMapping("/login")
	public String login(OAuth2Authentication auth , Model model, 
			@RequestParam(value="error", required=false) String error,
			@RequestParam(value="logout", required=false) String logout){
		
		if(error != null){
			model.addAttribute("errorMsg", "에러 발생");
		}
		if(logout != null){
			model.addAttribute("logoutMsg", "로그아웃 되었습니다");
		}
		
		if(auth != null && auth.isAuthenticated()){
			
			Map<String, String> map = (HashMap<String, String>) auth.getUserAuthentication().getDetails();

			if(map.get("name") == null){ // kakao
				HashMap<String, String> propertyMap = (HashMap<String, String>) (Object) map.get("properties");
				model.addAttribute("name", propertyMap.get("nickname"));
			}
			else{ // facebook
				model.addAttribute("name", map.get("name"));
			}
			
		}
		return "login"; // never redirect to "/"
	}
	
	@GetMapping(value="/{facebook|kakao}/complete")
	public String oauthComplete(@SocialUser User user, HttpSession session, OAuth2Authentication auth){
		if(userService.isNotExistUser(user.getPrincipal())){
			userService.saveUser(user); // transactional
			
			try{
				emailService.sendWelcomeUserEmail(user);
			} catch(MailException | InterruptedException e) {
				logger.warn("Error sending email : " + e.getMessage());
			}
		}
		
		User currentUser = getCurrentUser();
		Stylist stylist = stylistService.findStylistByUser(currentUser);
		if(stylist != null){
			logger.warn("DB에 스타일리스트로 등록되어있습니다.");
			session.setAttribute("stylist", stylist);
		}
		
		session.setAttribute("user", currentUser);
		
		return "complete";
	}
	
	/**
	 * Logout user method
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		
		CookieClearingLogoutHandler cookiehandler 
			= new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
		cookiehandler.logout(request, response, null); // remove cookie
		
		
		SecurityContextLogoutHandler contextHandler = new SecurityContextLogoutHandler();
		contextHandler.logout(request, response, null); // remove security context
		
		return "redirect:/users/login?logout";
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
	
	@GetMapping("/mypage")
	public String msypage(HttpSession session, Model model,String type){
		User user = (User)session.getAttribute("user");
		List<Book> bookList = bookService.userBookList(user,type);
		for(Book book : bookList){
			book.setStylist(stylistService.findStylistByStylistCode(book.getStylistCode()));
			book.setProduct(productService.selectProductByCode(book.getProductCode()));
			book.setOption(productService.selectOptionByCode(book.getOptionCode()));
		}
		int count = bookList.size();
		model.addAttribute("bookList", bookList);
		model.addAttribute("count", count);
		model.addAttribute("type",type);
		return "user_order_view";
	}
	
	/**
	 * user가 보는 포트폴리오 페이지
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
		
		return "user_portfolio_list";
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
		
		return "user_postscript_list";
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
		
		return "user_page_sales_list";
	}
	
}
