package yapp.devcamp.hairstylistserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import yapp.devcamp.hairstylistserver.model.Postscript;
import yapp.devcamp.hairstylistserver.service.PostscriptService;

@Controller
@RequestMapping("/postscript")
public class PostscriptController {
	
	@Autowired
	PostscriptService postscriptService;
	
	@RequestMapping("/enroll")
	public String enrollScript(Postscript postscript){
		if(postscript != null){
			postscriptService.enrollScript(postscript);
		}
		return "index";
	}
	
	@RequestMapping("/delete")
	public String deleteScript(int postscriptCode){
		Postscript postscript = null;
		
		if(postscriptCode != 0){
			postscript = postscriptService.selectByPostscriptCode(postscriptCode);
		}
		
		if(postscript != null){
			postscriptService.deleteScript(postscript);
		}
		
		return "redirect:read";
	}
	
	@RequestMapping("/read")
	public ModelAndView selectAll(){
		List<Postscript> list = postscriptService.selectAll();
		ModelAndView mv = new ModelAndView();
		mv.addObject("list", list);
		mv.setViewName("index");
		return mv;
	}
	
	public Postscript selectBypostscriptCode(int postscriptCode){
		
		return postscriptService.selectByPostscriptCode(postscriptCode);
	}
}
