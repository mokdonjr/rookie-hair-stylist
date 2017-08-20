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
	 */
	@RequestMapping("/login")
	public String login(User user){
		
		return "redirect:index";
	}
	
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
