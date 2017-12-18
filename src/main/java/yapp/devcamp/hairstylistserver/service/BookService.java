package yapp.devcamp.hairstylistserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yapp.devcamp.hairstylistserver.dao.BookRepository;
import yapp.devcamp.hairstylistserver.dao.ShopRepository;
import yapp.devcamp.hairstylistserver.model.Book;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;

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
		bookRepository.save(bookModel);
	}
	
	/**
	 * 예약 가능 시간 전송
	 */
	public List<String> enable(String selectDate){
		return bookRepository.selectTimeByDate(selectDate);
	}
	
	/**
	 * user 전체 예약 조회
	 */
	public List<Book> userBookList(User user,String type){
		
		if("order".equals(type)){
			return bookRepository.orderList(user.getId());
		}
		if("waiting".equals(type)){
			return bookRepository.waitList(user.getId());
		}
		
		return bookRepository.findByUser(user);
	}
	
	/**
	 * stylist 전체 예약 조회
	 */
	public List<Book> stylistBookList(Stylist stylist,String type){
		if("order".equals(type)){
			return bookRepository.stylistOrderList(stylist.getStylistCode());
		}
		if("waiting".equals(type)){
			return bookRepository.stylistWaitList(stylist.getStylistCode());
		}
		return bookRepository.selectByStylistCode(stylist.getStylistCode());
	}
	
	/**
	 * 예약 취소
	 */
	public void cancelBook(int bookCode){
		Book resultBook = selectBookByCode(bookCode);
		resultBook.setBookStatus(2);
		bookRepository.save(resultBook);
	}
	
	/**
	 * 스타일링 진행하기
	 */
	public void confirm(int bookCode){
		Book resultBook = selectBookByCode(bookCode);
		resultBook.setBookStatus(1);
		bookRepository.save(resultBook);
	}
	
	/**
	 * 스타일링 완료하기
	 */
	public void complete(int bookCode){
		Book resultBook = selectBookByCode(bookCode);
		resultBook.setBookStatus(3);
		bookRepository.save(resultBook);
	}
	
	/**
	 * 예약코드로 예약정보 찾아오기
	 */
	public Book selectBookByCode(int bookCode){
		return bookRepository.findBybookCode(bookCode);
	}
	
}
