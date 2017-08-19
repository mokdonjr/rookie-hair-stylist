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
	 * 
	 * @param :
	 *            stylist data
	 * @return : shopList
	 */
	@RequestMapping("/enroll")
	public ModelAndView enroll(Stylist stylist) {
		// stylist정보 저장하고

		// shopList를 가져와

		// 메인으로 리턴
		ModelAndView model = new ModelAndView();
		model.setViewName("/");

		return model;
	}

	/**
	 * redirect apply_form.html
	 * @param : nickname
	 * @return : userId
	 */
	@RequestMapping("/apply")
	public ModelAndView apply(User user) {
		//nickname으로 userId 가져와 apply_form으로 전달
		
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
		//session에서 stylistID 가져오기
		
		//스타일리스트 샵, 포트폴리오, 후기 정보들을 mypage로 전달
		
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
	 * @param : shopCode
	 * @return : shopData
	 */
	@RequestMapping("/shop")
	public ModelAndView shop(Shop shop){
		//shopCode로 샵정보 읽어와서
		
		//shop.html로 정보 리턴
		
		ModelAndView model = new ModelAndView();
		model.setViewName("shop");
		
		return model;
	}

}
