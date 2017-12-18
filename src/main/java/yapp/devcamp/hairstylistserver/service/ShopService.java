package yapp.devcamp.hairstylistserver.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import yapp.devcamp.hairstylistserver.controller.StorageRestController;
import yapp.devcamp.hairstylistserver.dao.BookRepository;
import yapp.devcamp.hairstylistserver.dao.ProductOptionRepository;
import yapp.devcamp.hairstylistserver.dao.ProductRepository;
import yapp.devcamp.hairstylistserver.dao.ShopRepository;
import yapp.devcamp.hairstylistserver.model.Book;
import yapp.devcamp.hairstylistserver.model.Product;
import yapp.devcamp.hairstylistserver.model.ProductOption;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;

@Service
public class ShopService {
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductOptionRepository productOptionRepository;
	
	@Autowired
	private StorageService storageService;
	
	//shop 등록
	public void saveShop(Shop shopModel) throws IOException{ 
		String shopName = shopModel.getShopName();
		int shopCode = shopModel.getShopCode();
		
		//shop 수정
		if(shopCode !=0){
			Shop resultShop = selectShopByShopCode(shopCode);
			int resultStylistCode = resultShop.getStylist().getStylistCode();
			String resultShopName = resultShop.getShopName();
			

		}
		
		shopRepository.save(shopModel);
	}
//	public void saveShop(Shop shop){
//		shopRepository.save(shop);
//	}
	
	//shop_code 알아오기
	public Shop selectShopByShopName(String shopName){
		return shopRepository.findByShopName(shopName);
	}
	
	public Shop selectShopByShopCode(int shopCode){
		return shopRepository.findByShopCode(shopCode);
	}
	
//	public List<Shop> findByStylist(Stylist stylist){
//		return shopRepository.findByStylist(stylist);
//	}
	public List<Shop> findByStylist(Stylist stylist){
		List<Shop> list = shopRepository.findByStylist(stylist);
		for(Shop shop : list){
			//썸네일 URL 만들기
			String url = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "serveShopImage", stylist.getStylistCode(), shop.getShopName(), "thumbnail.jpg")
			.build().toString();
			shop.setImagePath(url);
			
			//포트폴리오 Url 만들기
			String portfolioPath = storageService.shopLoad(stylist.getStylistCode(), shop.getShopName()).toString();
			File file = new File(portfolioPath);
			File[] fileList = file.listFiles();
			String[] getPort = new String[fileList.length-1];
			
			for(int i=0;i<fileList.length;i++){
				String filename = fileList[i].getName();
				if(!filename.equals("thumbnail.jpg")){
					String portfolioUrl = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "serveShopImage", shop.getStylist().getStylistCode(), shop.getShopName(), filename)
										.build().toString();
					getPort[i] = portfolioUrl;
				}
			}
			shop.setPortfolioImg(getPort);
		}
		return list;
	}
	
	public boolean isExistAnyShop(){
		return shopRepository.findOne(1) != null;
	}
	
	public boolean isAlreadyEnrollShopName(String shopName){
		return shopRepository.findByShopName(shopName) != null;
	}
	
	//product 등록
	public void saveProduct(List<Product> productList,Shop shop){
		for(Product product : productList){
			product.setShop(shop);
			productRepository.save(product);
		}
	}
	
	//shopCode로 product 알아오기
	public List<Product> findByshopCode(Shop shop){
		return productRepository.findByShop(shop);
	}
	
	//shopCode로 option 알아오기
	public List<ProductOption> findByshopCodeOption(Shop shop){
		return productOptionRepository.findByShop(shop);
	}
	
	//option 등록
	public void saveOption(List<ProductOption> optionList,Shop shop){
		for(ProductOption option : optionList){
			option.setShop(shop);
			productOptionRepository.save(option);
		}
	}
	
	//shop selectAll
	public List<Shop> selectAllShop(){
		List<Shop> list = shopRepository.orderByshopDate();
		for(Shop shop : list){
			String url = MvcUriComponentsBuilder.fromMethodName(StorageRestController.class, "serveShopImage", shop.getStylist().getStylistCode(), shop.getShopName(), "thumbnail.jpg")
			.build().toString();
			shop.setImagePath(url);
		}
		return list;
	}
	
	//shop delete
	public void deleteShop(Shop shop){
		shopRepository.delete(shop);
	}
	
	
}
