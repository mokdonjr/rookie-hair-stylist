package yapp.devcamp.hairstylistserver.controller;

import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
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

	Logger logger = LoggerFactory.getLogger("yapp.devcamp.hairstylistserver.StylistController");
	/**
	 * enroll stylist
	 * 
	 * @param :
	 *            stylist data
	 * @return : shopList
	 */
	@GetMapping("/enroll")
	public String enrollStylist(Stylist stylist) {
		

		return "enrollStylist";
	}
	
//	@PostMapping("/enroll")
//	public String enrollStylistPost(@Valid Stylist stylist, BindingResult result, HttpServletRequest request){
//		
//		if(result.hasErrors()){
//			logger.debug("From data has errors");
//			List<ObjectError> errors = result.getAllErrors();
//			for(ObjectError error : errors){
//				error.getDefaultMessage();
//			}
//			return "enrollStylist";
//		}
//		
//		MultipartFile licenseImage = stylist.getLicenseImage();
//		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
//		
////		Path savePath = Paths.get(rootDirectory + "\\images\\stylist\\")
//	}

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
