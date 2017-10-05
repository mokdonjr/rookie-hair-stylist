package yapp.devcamp.hairstylistserver.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yapp.devcamp.hairstylistserver.exception.ShopNotFoundException;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.service.ShopService;

@RestController
@RequestMapping("/api/shops")
public class ShopRestController {
	Logger logger = LoggerFactory.getLogger(ShopRestController.class);

	@Autowired
	private ShopService shopService;
	
	@GetMapping("/{shop_code}")
	public ResponseEntity<Shop> getShop(@PathVariable("shop_code") int shop_code){
		Shop shop = shopService.selectShopByShopCode(shop_code);
		
		if(shop == null){
			throw new ShopNotFoundException(shop_code);
		}
		
		return new ResponseEntity<Shop>(shop, HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Shop>> getAllShop(){
		List<Shop> shops = shopService.selectAllShop();
		if(shops.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Shop>>(shops, HttpStatus.OK);
	}

}
