package yapp.devcamp.hairstylistserver.controller;

import java.io.IOException;
import java.nio.file.Path;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import yapp.devcamp.hairstylistserver.exception.StylistAlreadyEnrollException;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.oauth.AuthorityType;
import yapp.devcamp.hairstylistserver.service.EmailService;
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
	public String mypage(HttpSession session, Model model){
		
		Stylist stylist;
		if((stylist = (Stylist) session.getAttribute("stylist")) == null){
			model.addAttribute("errorMsg", "권한이 없습니다");
			return "error";
		}
		List<Shop> shopList = shopService.findByStylist(stylist);
		
		model.addAttribute("stylist", stylist);
		model.addAttribute("shopList", shopList);
		
		return "designer_info";
	}
	
	@GetMapping("/designerInfo/{stylistCode}")
	public String showDesignerInfo(@PathVariable("stylistCode") int stylistCode, Model model){
		
		Stylist stylist = stylistService.findStylistByStylistCode(stylistCode);
		List<Shop> shopList = shopService.findByStylist(stylist);
		
		model.addAttribute("stylist", stylist);
		model.addAttribute("shopList", shopList);
		
		return "designer_info";
	}
	
	/**
	 * redirect mypage
	 */
	@RequestMapping("/sellproduct")
	public String sellproduct(){
		
		return "redirect:mypage";
	}
	
	/**
	 * redirect mypage
	 */
	@RequestMapping("/portfolio")
	public String portfolio(){
		return "redirect:mypage";
	}
	
	/**
	 * redirect mypage
	 */
	@RequestMapping("/postscript")
	public String postscript(){
		return "redirect:mypage";
	}
	
	/**
	 * redirect to shop.html
	 * @return : shopData
	 */
	@RequestMapping("/shop")
	public ModelAndView shop(Shop shop){
		
		
		ModelAndView model = new ModelAndView();
		model.setViewName("shop");
		
		return model;
	}

}
