package yapp.devcamp.hairstylistserver.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import yapp.devcamp.hairstylistserver.model.Book;
import yapp.devcamp.hairstylistserver.model.Product;
import yapp.devcamp.hairstylistserver.model.ProductOption;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.service.BookService;
import yapp.devcamp.hairstylistserver.service.ProductService;
import yapp.devcamp.hairstylistserver.service.ShopService;
import yapp.devcamp.hairstylistserver.service.StylistService;

/**
 * Book management Controller
 */
@Controller
@SessionAttributes("bookModel")
@RequestMapping("/book")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private StylistService stylistService;
	
	
	/**
	 * shop 예약하기
	 */
	@PostMapping(value = "/{shopCode}")
	public String book(HttpSession session,Model model ,@PathVariable("shopCode") int shopCode, Book bookModel)
			throws Exception {
		Shop shop = shopService.selectShopByShopCode(shopCode);
		bookModel.setShop(shop);
		User user = (User) session.getAttribute("user");
		bookModel.setUser(user);
		// 0-예약 접수, 1-스타일링 대기중, 2-예약 취소, 3-스타일링 완료
		bookModel.setBookStatus(0);
		bookService.book(bookModel);
		
		model.addAttribute("bookModel", bookModel);
		return "redirect:complete.rk";
	}
	
	/**
	 * 예약 완료
	 */
	@GetMapping(value="/complete.rk")
	public String complete(@ModelAttribute(value="bookModel") Book bookModel,Model model){
		Product product = productService.selectProductByCode(bookModel.getProductCode());
		ProductOption option = productService.selectOptionByCode(bookModel.getOptionCode());
		Stylist stylist = stylistService.findStylistByStylistCode(bookModel.getStylistCode());
		
		model.addAttribute("product", product);
		model.addAttribute("option",option);
		model.addAttribute("book", bookModel);
		model.addAttribute("stylist", stylist);
		
		return "reservation_confirm";
	}
	
	/**
	 * 예약 가능 시간 전송
	 */
	@RequestMapping(value="/enableTime", method=RequestMethod.GET)
	@ResponseBody
	public List<String> enable(String selectedDate){
		List<String> timeList = bookService.enable(selectedDate);
		
		return timeList;
	}
	
	/**
	 * 예약 취소
	 */
	@RequestMapping(value="/cancel/{bookCode}",method=RequestMethod.GET)
	public String cancelBook(@PathVariable("bookCode") int bookCode){
		bookService.cancelBook(bookCode);
		return "redirect:/users/mypage";
	}
	
	/**
	 * 디자이너 예약 취소
	 */
	@GetMapping(value="/designer/cancel/{bookCode}")
	public String designerCancel(@PathVariable("bookCode") int bookCode){
		bookService.cancelBook(bookCode);
		return "redirect:/stylist/mypage";
	}
	
	/**
	 * 스타일링 진행하기
	 */
	@GetMapping(value="/confirm/{bookCode}")
	public String confirm(@PathVariable("bookCode") int bookCode){
		bookService.confirm(bookCode);
		return "redirect:/stylist/mypage";
	}
	
	/**
	 * 스타일링 완료하기
	 */
	@GetMapping(value="/complete/{bookCode}")
	public String complete(@PathVariable("bookCode") int bookCode){
		bookService.complete(bookCode);
		return "redirect:/stylist/mypage";
	}
	
}
