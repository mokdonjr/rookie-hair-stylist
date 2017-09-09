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

	@RequestMapping("/secured")
	public String secured(){
		return "secured";
	}
}
