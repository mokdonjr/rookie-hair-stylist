package yapp.devcamp.hairstylistserver.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
	 */
	@RequestMapping("/login")
	public String login(User user){
		
		return "redirect:index";
	}
	
	@RequestMapping(value="/oauth/kakao", produces="applicaion/json", method={RequestMethod.GET, RequestMethod.POST})
	public String kakaoLogin(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response){
		// 로그인 후 code를 얻음
		logger.info("Kakao Oauth login user's code : " + code);
		
		return "redirect:index";
	}
	
//	private JsonNode getAccessToken(String authorized_code){
//		final String requestUrl = "https://kauth.kakao.com/oauth/token";
//		final String kakaoRestApiKey = "308a1cd7fa6d5ed0ea8bb2e61d00d9e3";
//		
//		final List<NameValuePair> postParams = new ArrayList<>();
//		postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
//		postParams.add(new BasicNameValuePair("client_id", kakaoRestApiKey));
//		
//	}
	
	/**
	 * Logout user method
	 */
	@RequestMapping("/logout")
	public String logout(){
		
		return "redirect:index";
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
