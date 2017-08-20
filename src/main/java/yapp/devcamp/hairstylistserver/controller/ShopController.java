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
	 */
	@RequestMapping("/book")
	public String book(Shop shop){
		
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
