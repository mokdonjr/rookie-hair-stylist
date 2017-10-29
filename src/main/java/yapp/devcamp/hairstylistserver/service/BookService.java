package yapp.devcamp.hairstylistserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yapp.devcamp.hairstylistserver.dao.BookRepository;
import yapp.devcamp.hairstylistserver.dao.ShopRepository;
import yapp.devcamp.hairstylistserver.model.Book;
import yapp.devcamp.hairstylistserver.model.Shop;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private ShopService shopService;
	
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
			Shop resultShop = shopService.selectShopByShopCode(shopCode);
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
