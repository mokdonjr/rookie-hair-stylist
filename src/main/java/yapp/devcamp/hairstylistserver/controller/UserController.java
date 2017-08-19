package yapp.devcamp.hairstylistserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import yapp.devcamp.hairstylistserver.model.User;

/**
 * User management Controller
 */
@RestController
@RequestMapping("/users")
public class UserController {

	/**
	 * Login user method
	 * @param : user login data
	 */
	@RequestMapping("/login")
	public String login(User user){
		//로그인 처리
		
		//메인페이지 리턴
		return "redirect:index";
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
