package yapp.devcamp.hairstylistserver.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
		
		User user = getCurrentUser();
		
		// if already Stylist
		if(stylistService.isAlreadyEnrollUser(user)){
			throw new StylistAlreadyEnrollException(user.getId());
		}
		
		// set FK
		stylist.setUser(user);
		
		// set Image
		MultipartFile licenseImage = stylist.getLicenseImage();
		long imageSize = licenseImage.getSize();
		logger.warn("uploading image size : " + imageSize + "Bytes(" + imageSize/1024 + "KBytes)");
//		storageService.store(licenseImage); // StylistService에 직접 구현!!
		
		storageService.storeStylistEnrollImage(stylist.getStylistCode(), licenseImage);
		
		stylist.setLicenseImagePath(licenseImage.getOriginalFilename());
		stylistService.saveStylist(stylist);
		
		// send enroll email (심사대기)
		try{
			emailService.sendApplyStylistEmail(user); // User
			emailService.sendAdminStylistEmail(user, stylist); // Administer (rookies.yapp@gmail.com)
			
		} catch(MailException | InterruptedException e) {
			logger.warn("Error sending email : " + e.getMessage());
		}
		
		return "redirect:/stylist/enroll";
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
			return "redirect:/stylist/mypage";
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
	
	@GetMapping("/designerInfo")
	public String mypage(HttpSession session, Model model){
		//session에서 stylistID 가져오기
		Stylist stylist = (Stylist) session.getAttribute("stylist");
//		List<Shop> shopList = stylist.getShops();
		List<Shop> shopList = shopService.findByStylist(stylist);
		
		model.addAttribute("shopList", shopList);
		//스타일리스트 샵, 포트폴리오, 후기 정보들을 mypage로 전달
		
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
