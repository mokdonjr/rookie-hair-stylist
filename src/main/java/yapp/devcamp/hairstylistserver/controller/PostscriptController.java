package yapp.devcamp.hairstylistserver.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import yapp.devcamp.hairstylistserver.model.Postscript;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.service.PostscriptService;
import yapp.devcamp.hairstylistserver.service.ShopService;
import yapp.devcamp.hairstylistserver.service.StorageService;

@Controller
@RequestMapping("/postscript")
public class PostscriptController {
	
	@Autowired
	PostscriptService postscriptService;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	ShopService shopService;
	
	@RequestMapping(value="/enroll", method=RequestMethod.POST)
	public String enrollScript(Postscript postscript) throws IOException{
		if(postscript != null){
			String content = postscript.getContent();
			content = content.replace("\r\n", "<br>");
			postscript.setContent(content);
			
			int shopCode = postscript.getShop().getShopCode();
			Shop shop = shopService.selectShopByShopCode(shopCode);
			int stylistCode = shop.getStylist().getStylistCode();
			String shopName = shop.getShopName();
			
			//후기 이미지 저장
			MultipartFile postscriptImg = postscript.getPostscriptImg();
			if(!postscriptImg.getOriginalFilename().equals("")){
				String filename = storageService.storePostscriptImage(stylistCode, shopName, postscriptImg);
				String imagePath = getPostscriptImage(stylistCode, shopName, filename);
				postscript.setImagePath(imagePath);
			}
			
			postscriptService.enrollScript(postscript);
			return "redirect:/shop/"+shopCode;
		}
		return "redirect:/";
		
	}
	
	public String getPostscriptImage(int stylistCode, String shopName,String filename) throws IOException {
		String url = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "servePostscriptImage", stylistCode, shopName, filename)
						.build().toString();
		return url;
    }
	
//	@RequestMapping("/read")
//	public String selectAll(Model model){
//		List<Postscript> list = postscriptService.selectAll();
//		model.addAttribute("postscript", list);
//		return "";
//	}
	
	
//	@RequestMapping(value="/{postscriptCode}",method=RequestMethod.GET)
//	public String deleteScript(@PathVariable("postscriptCode") int postscriptCode){
//		
//		if(postscriptCode != 0)
//			postscriptService.deleteScript(postscriptCode);
//		
//		return "redirect:read";
//	}
	
	public Postscript selectBypostscriptCode(int postscriptCode){
		
		return postscriptService.selectByPostscriptCode(postscriptCode);
	}
}
