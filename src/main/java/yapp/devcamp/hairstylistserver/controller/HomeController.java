package yapp.devcamp.hairstylistserver.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.service.ShopService;

@Controller
public class HomeController {
	Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ShopService shopService;
	
	@GetMapping("/")
	public String home(Model model){
		List<Shop> shopList = shopService.selectAllShop();
		for(Shop shop : shopList){
			logger.warn("shop : " + shop.getShopCode() + " / " + shopList.size());
		}
		
		model.addAttribute("shopList", shopList);
		
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
	
	@RequestMapping("/detail2")
	public String detail2(){
		return "designer_info";
	}
	
	@RequestMapping("/secured")
	public String secured(){
		return "secured";
	}
}
