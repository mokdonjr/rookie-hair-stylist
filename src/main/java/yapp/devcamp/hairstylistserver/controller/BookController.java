package yapp.devcamp.hairstylistserver.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import yapp.devcamp.hairstylistserver.model.Book;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.service.ShopService;

/**
 * Book management Controller
 */
@RestController
@RequestMapping("/book")
public class BookController {
	
	@Autowired
	private ShopService shopService;
	
	/**
	 * shop 예약하기
	 */
	@RequestMapping("/")
	public String book(HttpSession session,Book bookModel) throws Exception{
		if(bookModel != null){
			User user = (User)session.getAttribute("user");
			bookModel.setUser(user);
			bookModel.setBookStatus(true);
			shopService.book(bookModel);
		}
		return "index";
	}
	/**
	 * 예약 취소
	 */
	@RequestMapping(value="/cancel/{bookCode}",method=RequestMethod.GET)
	public String cancelBook(@PathVariable("bookCode") int bookCode){
		shopService.cancelBook(bookCode);
		return "index";
	}
	/**
	 * 예약 완료
	 */
	@RequestMapping(value ="/complete/{bookCode}",method=RequestMethod.GET)
	public String conpleteBook(@PathVariable("bookCode") int bookCode){
		shopService.completeBook(bookCode);
		return "index";
	}
}
