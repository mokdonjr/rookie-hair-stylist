package yapp.devcamp.hairstylistserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import yapp.devcamp.hairstylistserver.exception.UserNotFoundException;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.oauth.AuthorityType;
import yapp.devcamp.hairstylistserver.service.EmailService;
import yapp.devcamp.hairstylistserver.service.StylistService;
import yapp.devcamp.hairstylistserver.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StylistService stylistService;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/grant/stylist/{id}")
	public String grantStylistAuthorityToUser(@PathVariable("id") int userId, Model model){
		
		// 1. 스타일리스트 신청(apply)한 사용자
		User appliedUser = userService.findById(userId);
		if(appliedUser == null){
//			throw new UserNotFoundException(userId);
			model.addAttribute("errorMsg", "존재하지 않는 유저입니다.");
			return "error";
		}
		
		// 2. already granted STYLIST
		if(appliedUser.getAuthorityType().isEquals(AuthorityType.STYLIST.getRoleType())){
			model.addAttribute("errorMsg", "이미 스타일리스트 입니다.");
			return "error";
		}
		
		// 3. update authority USER to STYLIST
		appliedUser.setAuthorityType(AuthorityType.STYLIST);
		userService.saveUser(appliedUser); 
		
		try{
			emailService.sendEnrollStylistEmail(appliedUser);
		} catch(MailException | InterruptedException e){
			logger.warn("Error sending mail : " + e.getMessage());
		}
		
		Stylist startingStylist = stylistService.findStylistByUser(appliedUser);
		
		model.addAttribute("user", appliedUser);
		model.addAttribute("stylist", startingStylist);
//		return new ResponseEntity<Stylist>(startingStylist, HttpStatus.OK);
		return "admin/grantStylist";
	}

}
