package yapp.devcamp.hairstylistserver.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import yapp.devcamp.hairstylistserver.annotation.SocialUser;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.service.EmailService;
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
		
		session.setAttribute("user", user);
		
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
	
	/**
	 * return index.html
	 * @return : shopList
	 */
	@RequestMapping("/index")
	public ModelAndView index(){
		ModelAndView model = new ModelAndView();
		
		
		model.setViewName("/");
		
		return model;
	}
	
	/**
	 * Read booking Information method
	 * @return : booklist
	 */
	@RequestMapping("/orderlist")
	public ModelAndView bookList(User user){
		
		ModelAndView model = new ModelAndView();
		model.setViewName("order_list");
		
		return model;
	}
	
	/**
	 * Alarm List method
	 */
	public void alarmList(){
		
	}
	
	/**
	 * Chatting send method
	 */
	public void sendChatting(){
		
	}
	
	/**
	 * Save chatting message
	 */
	public void saveChatMessage(){
		
	}
	
}
