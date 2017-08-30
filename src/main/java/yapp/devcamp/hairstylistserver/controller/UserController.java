package yapp.devcamp.hairstylistserver.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import yapp.devcamp.hairstylistserver.annotation.SocialUser;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.service.UserService;

/**
 * User management Controller
 */
@Controller
public class UserController {
	Logger logger = LoggerFactory.getLogger("yapp.devcamp.hairstylistserver.UserController");
	
	@Autowired
	private UserService userService;
	
	/**
	 * Login user method
	 * @param : user login data
	 */
	@GetMapping("/login")
	public String login(OAuth2Authentication auth, Model model){
		
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
		return "login";
	}
	
	@GetMapping(value="/{facebook|kakao}/complete")
	public String oauthComplete(@SocialUser User user, HttpSession session){
		if(userService.isNotExistUser(user.getPrincipal())){
			userService.saveUser(user); // transactional
		}
		session.setAttribute("user", user);
		return "complete";
	}
	
	/**
	 * Logout user method
	 */
	@RequestMapping("/logout")
	public String logout(){
		//로그아웃 처리
		
		//메인페이지 리턴
		return "redirect:index";
	}
	
	/**
	 * return index.html
	 * @return : shopList
	 */
	@RequestMapping("/index")
	public ModelAndView index(){
		ModelAndView model = new ModelAndView();
		
		//shopList를 가져와서
		
		//메인으로 리턴
		model.setViewName("/");
		
		return model;
	}
	
	/**
	 * Read booking Information method
	 * @return : booklist
	 * @param : userId
	 */
	@RequestMapping("/orderlist")
	public ModelAndView bookList(User user){
		//고객 예약 정보를 가져와
		
		//주문조회 페이지로 리턴
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
