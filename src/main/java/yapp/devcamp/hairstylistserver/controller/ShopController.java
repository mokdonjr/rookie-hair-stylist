package yapp.devcamp.hairstylistserver.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import yapp.devcamp.hairstylistserver.exception.UserNotFoundException;
import yapp.devcamp.hairstylistserver.model.Product;
import yapp.devcamp.hairstylistserver.model.ProductOption;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.service.ShopService;

/**
 * Shop Management Controller
 */
@Controller
@RequestMapping("/shop")
public class ShopController {
	
	@Autowired
	private ShopService shopService;
	
	/**
	 * Shop delete method
	 */
	@RequestMapping("/delete")
	public String delete(int shopCode){
		Shop resultShop = shopService.selectShopByShopCode(shopCode);
		if(resultShop != null){
			shopService.deleteShop(resultShop);
		}
		return "redirect:home";
	}
	
	/**
	 * select Shop by shopCode
	 */
	@RequestMapping("/{shopCode}")
	public ModelAndView selectByShopCode(@PathVariable("shopCode") int shopCode){
		ModelAndView mv = new ModelAndView();
		Shop resultShop = shopService.selectShopByShopCode(shopCode);
		//수정 페이지 정해지면 수정할 것
		mv.setViewName("index");
		mv.addObject("result", resultShop);
		return mv;
	}
	
	/**
	 * Shop selectAll
	 */
	@RequestMapping("/home")
	public ModelAndView selectAll(){
		List<Shop> shopList = shopService.selectAllShop();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		mv.addObject("shopList", shopList);
		return mv;
	}
	
	/**
	 * Shop enroll method
	 */
	@RequestMapping("/enroll")
	public String enroll(HttpSession session,Shop shopModel
			,MultipartFile thumbnail,Product product,ProductOption productOption) throws IOException{
		if(shopModel != null){
			shopService.saveShop(shopModel,thumbnail);
			Shop resultShop = shopService.selectShopByShopName(shopModel.getShopName());
			if(product != null)
				shopService.saveProduct(product.getProductList(), resultShop);
			if(productOption != null)
				shopService.saveOption(productOption.getOptionList(), resultShop);
		}
		
		return "redirect:home";
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
