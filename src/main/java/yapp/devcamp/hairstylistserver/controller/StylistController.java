package yapp.devcamp.hairstylistserver.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import yapp.devcamp.hairstylistserver.service.StorageService;
import yapp.devcamp.hairstylistserver.service.StylistService;
import yapp.devcamp.hairstylistserver.service.UserService;

/**
 * Stylist management Controller
 */
@Controller
@RequestMapping("/stylist")
public class StylistController {

	Logger logger = LoggerFactory.getLogger("yapp.devcamp.hairstylistserver.controller.StylistController");
	
	@Autowired
	private StylistService stylistService;
	
	@Autowired
	private UserService userService; // User getCurrentUser() mothod (user.getUsername())
	
	@Autowired
	private StorageService storageService;
	
	@Value("${spring.http.multipart.max-file-size}")
	private String licenseImageMaxSize; // max size multipart data
	
	/**
	 * enroll stylist
	 * @return : shopList
	 */
	@GetMapping("/enroll")
	public String enrollStylist(HttpServletRequest request, Model model) {
		
		// 현재 User 정보
		User user = getCurrentUser();
		
		// User의 username을 Stylist의 nickname으로 디폴트
		Stylist stylist = new Stylist();
		stylist.setStylistNickname(user.getUsername());
		model.addAttribute("stylist", stylist);
		
		// info max multipart data size
		model.addAttribute("licenseImageMaxSize", licenseImageMaxSize);

		return "enrollStylist";
	}
	
	@PostMapping("/enroll")
	public String enrollStylistPost(@Valid Stylist stylist, BindingResult result, HttpServletRequest request){
		
		// message
		if(result.hasErrors()){
			logger.debug("From data has errors");
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error : errors){
				error.getDefaultMessage();
			}
			return "enrollStylist";
		}
		
		// set User must be one to one
		User user = getCurrentUser();
		
		// if not exist
		if(stylistService.isAlreadyEnrollUser(user)){
			throw new StylistAlreadyEnrollException(user.getId());
		}
		stylist.setUser(user);
		
		// set Image
		MultipartFile licenseImage = stylist.getLicenseImage();
		storageService.store(licenseImage);
		
		
		// set Image
//		MultipartFile licenseImage = stylist.getLicenseImage();
//		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
//		logger.warn("rootDirectory : " + rootDirectory);
//		
//		Path savePath = Paths.get(rootDirectory + "\\images\\stylist\\" + licenseImage.getOriginalFilename());
//		if(licenseImage != null && !licenseImage.isEmpty()){
//			try{
//				licenseImage.transferTo(new File(savePath.toString())); // save
//			} catch(Exception e){
//				e.printStackTrace();
//			}
//		}
		
		stylist.setLicenseImagePath(licenseImage.getOriginalFilename());
		stylistService.saveStylist(stylist);
		
		return "redirect:/stylist/mypage";
	}
	
	/**
	 * current login user
	 * @return
	 */
	private User getCurrentUser(){
		
//		String currentUserPrincipal = request.getUserPrincipal().getName(); // 506264477
//		User user = userService.findByPrincipal(currentUserPrincipal); // 백승찬 User
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String principal = authentication.getName();
		logger.warn(principal);
		
		User user = userService.findByPrincipal(principal);
		return user;
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
