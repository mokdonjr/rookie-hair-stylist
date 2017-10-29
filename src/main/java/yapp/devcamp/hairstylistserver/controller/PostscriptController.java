package yapp.devcamp.hairstylistserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import yapp.devcamp.hairstylistserver.model.Postscript;
import yapp.devcamp.hairstylistserver.service.PostscriptService;

@Controller
@RequestMapping("/postscript")
public class PostscriptController {
	
	@Autowired
	PostscriptService postscriptService;
	
	@RequestMapping(value="/enroll",method=RequestMethod.POST)
	public String enrollScript(Postscript postscript){
		if(postscript != null){
			postscriptService.enrollScript(postscript);
		}
		return "index";
	}
	
	@RequestMapping(value="/{postscriptCode}",method=RequestMethod.GET)
	public String deleteScript(@PathVariable("postscriptCode") int postscriptCode){
		
		if(postscriptCode != 0)
			postscriptService.deleteScript(postscriptCode);
		
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
