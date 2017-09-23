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
	private ServletContext context;
	
	//shop 등록
	public void saveShop(Shop shopModel,MultipartFile[] thumbnail) throws IOException{
			if(thumbnail !=null){
				uploadFile(shopModel, thumbnail);
			}
		
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
	public void uploadFile(Shop shopModel, MultipartFile[] thumbnail) throws IOException {
		// 경로 설정을 위해 userId 필요
		String userId = "test";
		String path = context.getRealPath("resources/upload")+"/" + userId + "/" + shopModel.getShopName() + "/";
		String originName="";
		String fileName = "thumbnail.jpg";
		
		shopModel.setImagePath("/" + userId + "/" + shopModel.getShopName() + "/");
		
		
		//기존 디렉토리 이름 가져오기
		if(shopModel.getShopCode() != 0){
			Shop resultShop = selectShopByShopCode(shopModel.getShopCode());
			originName = resultShop.getShopName();
		}
		
		for(int i=0;i<thumbnail.length;i++){
			//파일 업로드
			if (!thumbnail[i].isEmpty()) {
				File file = new File(path);
				
				//이름 수정
				if(!originName.equals("")){
					String originPath = context.getRealPath("resources/upload")+"/" + userId + "/" + originName + "/";
					File originFile = new File(originPath);
					originFile.renameTo(file);
				}
				
				if (!file.exists()) {
					file.mkdirs();
				}
				
				if(i!=0){
					fileName = thumbnail[i].getOriginalFilename();
				}
				File transFile=new File(path + fileName);
			
				thumbnail[i].transferTo(transFile);
			}
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
		//return shopRepository.findAll();
		return shopRepository.orderByshopDate();
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
		
//		shopDate = resultShop.getShopDay();
//		resultDate = subDate(shopDate, bookModel.getBookDay());
//		if(resultDate != null)
//			resultShop.setShopDay(resultDate);
//		else
//			throw new Exception("예약 가능 요일이 없습니다.");
		
//		shopDate = resultShop.getShopTime();
//		resultDate = subDate(shopDate, bookModel.getBookTime());
//		if(resultDate != null)
//			resultShop.setShopTime(resultDate);
//		else
//			throw new Exception("예약 가능 시간이 없습니다.");
		
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
