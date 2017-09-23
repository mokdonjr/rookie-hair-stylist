package yapp.devcamp.hairstylistserver.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
@Transactional
public class ShopService {
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductOptionRepository productOptionRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private StorageService storageService;
	
	//shop 등록
	public void saveShop(Shop shopModel,MultipartFile[] thumbnail) throws IOException{
		int stylistCode = 0;
		String shopName = shopModel.getShopName();
		
		if(shopModel != null){
			Stylist stylist = new Stylist();
			//세션에서 받아와서 저장할 것
			stylist.setStylistCode(1);
			stylistCode = stylist.getStylistCode();
			shopModel.setStylist(stylist);
			shopModel.setShopStatus("true");
		}
		int shopCode = shopModel.getShopCode();
		//shopname 수정
		if(shopCode !=0){
			Shop resultShop = selectShopByShopCode(shopCode);
			int resultStylistCode = resultShop.getStylist().getStylistCode();
			String resultShopName = resultShop.getShopName();
			
			String originPath = storageService.shopLoad(resultStylistCode, resultShopName).toString();
			File originFile = new File(originPath);
			
			String newPath = storageService.shopLoad(stylistCode, shopName).toString();
			File newFile = new File(newPath);
			originFile.renameTo(newFile);
			
			originPath = storageService.postscriptLoad(resultStylistCode, resultShopName).toString();
			originFile = new File(originPath);
			
			newPath = storageService.postscriptLoad(resultStylistCode, resultShopName).toString();
			newFile = new File(newPath);
			originFile.renameTo(newFile);
		}
		if(thumbnail !=null){
			for(int i=0;i<thumbnail.length;i++){
				if(!thumbnail[i].getOriginalFilename().equals("")){
					storageService.storeShopImage(shopModel.getStylist().getStylistCode(), shopModel.getShopName(), thumbnail[i],i);
				}
			}
		}
		
		shopRepository.save(shopModel);
	}
	
	//shop_code 알아오기
	public Shop selectShopByShopName(String shopName){
		return shopRepository.findByShopName(shopName);
	}
	
	public Shop selectShopByShopCode(int shopCode){
		return shopRepository.findByShopCode(shopCode);
	}
	
	public List<Shop> findByStylist(Stylist stylist){
		return shopRepository.findByStylist(stylist);
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
		List<Shop> list = shopRepository.orderByshopDate();
		for(Shop shop : list){
			String filePath = storageService.shopLoad(shop.getStylist().getStylistCode(), shop.getShopName()).toString();
			File file = new File(filePath);
			if(file != null && file.listFiles()!=null){
				shop.setFiles(file.listFiles());
			}

		}
		return list;
	}
	
	//shop delete
	public void deleteShop(Shop shop){
		shopRepository.delete(shop);
	}
	
	/**
	 * shop 예약하기
	 */
	public void book(Book bookModel) throws Exception{
		//예약한 날짜시간요일 빼기
		Shop resultShop = shopRepository.findByShopCode(bookModel.getShop().getShopCode());
		
		String shopDate = resultShop.getShopDate();
		String resultDate = subDate(shopDate, bookModel.getBookDate());
		if(resultDate != null)
			resultShop.setShopDate(resultDate);
		else
			throw new Exception("예약 가능 날짜가 없습니다.");
		
		//예약시간 빼서 다시 update
		shopRepository.save(resultShop);
		
		bookRepository.save(bookModel);
	}
	
	/**
	 * 예약된 날짜 빼는 메소드
	 */
	private String subDate(String shopDate,String bookDate){
		String[] dateArr = shopDate.split(",");
		String insertDate="";
		boolean flag = false;
		
		for(int i=0;i<dateArr.length;i++){
			if(!dateArr[i].equals(bookDate)){
				insertDate += dateArr[i];
				if(i != dateArr.length-1){
					insertDate += ",";
				}
			} else {
				flag = true;
			}
		}
		if(flag)
			return insertDate;
		else
			return null;
	}
	
	/**
	 * 예약 취소
	 */
	public void cancelBook(int bookCode){
		Book resultBook = selectBookByCode(bookCode);
		
		if(resultBook != null){
			int shopCode = resultBook.getShop().getShopCode();
			Shop resultShop = selectShopByShopCode(shopCode);
			if(resultShop != null){
				String shopDate = resultShop.getShopDate();
				if(shopDate.length()!=0){
					shopDate += ","+resultBook.getBookDate();
				} else{
					shopDate = resultBook.getBookDate();
				}
				resultShop.setShopDate(shopDate);
				
				shopRepository.save(resultShop);
				bookRepository.deleteByCode(bookCode);
			}
		}
	}
	
	/**
	 * 예약 시술 완료
	 */
	public void completeBook(int bookCode){
		Book resultBook = selectBookByCode(bookCode);
		if(resultBook != null){
			resultBook.setBookStatus(false);
		}
		bookRepository.save(resultBook);
	}
	
	/**
	 * 예약 코드로 레코드 찾아오기
	 */
	public Book selectBookByCode(int bookCode){
		return bookRepository.findBybookCode(bookCode);
	}
}
