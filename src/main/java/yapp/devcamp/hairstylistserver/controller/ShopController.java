package yapp.devcamp.hairstylistserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import yapp.devcamp.hairstylistserver.model.Shop;

/**
 * Shop Management Controller
 */
@Controller
@RequestMapping("/shop")
public class ShopController {
	
	/**
	 * Save booked shop data
	 * @param : shop data
	 */
	@RequestMapping("/book")
	public String book(Shop shop){
		//예약하는 shop 정보를 저장해서
		
		//shop.html로 리턴
		return "shop";
	}
	
	/**
	 * Shop update method
	 */
	public void update(){
		
	}
	
	/**
	 * Shop exit method
	 */
	public void exit(){
		
		
	}
	
	/**
	 * Shop create method
	 */
	public void create(){
		
	}
	
	/**
	 * Read Shop method
	 */
	public void search(){
		
	}
	
	/**
	 * Sort shop list
	 */
	public void sort(){
		
	}
}
