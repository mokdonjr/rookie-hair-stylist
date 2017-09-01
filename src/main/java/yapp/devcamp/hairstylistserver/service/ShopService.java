package yapp.devcamp.hairstylistserver.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import yapp.devcamp.hairstylistserver.dao.ProductOptionRepository;
import yapp.devcamp.hairstylistserver.dao.ProductRepository;
import yapp.devcamp.hairstylistserver.dao.ShopRepository;
import yapp.devcamp.hairstylistserver.model.Product;
import yapp.devcamp.hairstylistserver.model.ProductOption;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;

@Service
@Transactional
public class ShopService {
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductOptionRepository productOptionRepository;
	
	//shop 등록
	public void saveShop(Shop shopModel,MultipartFile thumbnail) throws IOException{
		
		if(thumbnail !=null)
			uploadFile(shopModel, thumbnail);
		
		if(shopModel != null){
			Stylist stylist = new Stylist();
			//세션에서 받아와서 저장할 것
			stylist.setStylistCode(1);
			shopModel.setStylist(stylist);
			shopModel.setShopStatus("true");
		}
		
		shopRepository.save(shopModel);
	}
	
	// 파일 업로드
	public void uploadFile(Shop shopModel, MultipartFile thumbnail) throws IOException {
		// 경로 설정을 위해 userId 필요
		String userId = "test";
		String path = "C:/save/" + userId + "/shop/" + shopModel.getShopName() + "/";
		String originName="";
		String fileName = "thumbnail.jpg";
		
		//기존 디렉토리 이름 수정
		if(shopModel.getShopCode() != 0){
			Shop resultShop = selectShopByShopCode(shopModel.getShopCode());
			originName = resultShop.getShopName();
		}
		
		//파일 업로드
		if (!thumbnail.isEmpty()) {
			File file = new File(path);
			File transFile = new File(path + fileName);
			//이름 수정
			if(!originName.equals("")){
				String originPath = "C:/save/" + userId + "/shop/" + originName + "/";
				File originFile = new File(originPath);
				originFile.renameTo(file);
			}
		
			if (!file.exists()) {
				file.mkdirs();
			}
			if (transFile.exists()) {
				transFile.delete();
			}

			thumbnail.transferTo(transFile);
		}
	}
	
	//shop_code 알아오기
	public Shop selectShopByShopName(String shopName){
		return shopRepository.findByShopName(shopName);
	}
	
	public Shop selectShopByShopCode(int shopCode){
		return shopRepository.findByShopCode(shopCode);
	}
	
	//product 등록
	public void saveProduct(List<Product> productList,Shop shop){
		for(Product product : productList){
			product.setShop(shop);
			productRepository.save(product);
		}
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
		return shopRepository.findAll();
	}
	
	//shop delete
	public void deleteShop(Shop shop){
		shopRepository.delete(shop);
	}
}
