package yapp.devcamp.hairstylistserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String home(){
		return "index";
	}
	
	@RequestMapping("/front")
	public String front(){
		return "front";
	}
	
	@RequestMapping("/detail")
	public String detail(){
		System.out.println("asdaasd");
		return "sales_detail";
	}
	
	@RequestMapping("/secured")
	public String secured(){
		return "secured";
	}
}
