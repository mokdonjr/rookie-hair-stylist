package yapp.devcamp.hairstylistserver.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;

/**
 * Stylist management Controller
 */
@Controller
@RequestMapping("/stylist")
public class StylistController {

	/**
	 * enroll stylist
	 * @return : shopList
	 */
	@RequestMapping("/enroll")
	public ModelAndView enroll(Stylist stylist) {


		ModelAndView model = new ModelAndView();
		model.setViewName("/");

		return model;
	}

	/**
	 * redirect apply_form.html
	 * @return : userId
	 */
	@RequestMapping("/apply")
	public ModelAndView apply(User user) {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("apply_form");
		
		return model;
	}
	
	/**
	 * redirect mypage method
	 * @return : stylist shop, portfolio, postscript data 
	 */
	@RequestMapping("/mypage")
	public ModelAndView mypage(HttpSession session){
		
		
		ModelAndView model = new ModelAndView();
		model.setViewName("mypage");
		
		return model;
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
