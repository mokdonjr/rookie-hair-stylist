package yapp.devcamp.hairstylistserver.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import yapp.devcamp.hairstylistserver.exception.ErrorResponse;
import yapp.devcamp.hairstylistserver.exception.ShopAlreadyEnrollException;
import yapp.devcamp.hairstylistserver.exception.ShopNotFoundException;
import yapp.devcamp.hairstylistserver.exception.StorageFileNotFoundException;
import yapp.devcamp.hairstylistserver.exception.StylistAlreadyEnrollException;
import yapp.devcamp.hairstylistserver.exception.StylistNicknameNotFoundException;
import yapp.devcamp.hairstylistserver.exception.StylistNotFoundException;
import yapp.devcamp.hairstylistserver.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionController {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(HttpServletRequest request,
			UserNotFoundException ex) {

		String requestURL = request.getRequestURL().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("user.not-found.exception");
		errorResponse.setErrorMessage("User with id " + ex.getUserId() + " not found");

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(StylistNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleStylistNotFoundException(HttpServletRequest request,
			StylistNotFoundException ex) {
		String requestURL = request.getRequestURL().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("stylist.not-found.exception");
		errorResponse.setErrorMessage("Stylist with stylist_code " + ex.getStylistCode() + " not found");

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(StylistNicknameNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleStylistNicknameNotFoundException(HttpServletRequest request,
			StylistNicknameNotFoundException ex){
		String requestURL = request.getRequestURL().toString();
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("stlist-nickname.not-found.exepction");
		errorResponse.setErrorMessage("Stylist with stylist_nickname " + ex.getStylistNickname() + " not found");
		
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	// should not be restful ...
	@ExceptionHandler(StylistAlreadyEnrollException.class)
	public ResponseEntity<ErrorResponse> handleStylistAlreadyEnrollException(HttpServletRequest request,
			StylistAlreadyEnrollException ex){
		String requestURL = request.getRequestURL().toString();
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("stlist.already-enroll.exepction");
		errorResponse.setErrorMessage("User with user_id " + ex.getUserId() + " already enrolled stylist");
		
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFoundException(StorageFileNotFoundException ex){
		return ResponseEntity.notFound().build();
	}
	
	// not restful
//	@ExceptionHandler(MultipartException.class)
//    public String handleMaxUploadSizeExceededException(MultipartException e, RedirectAttributes redirectAttributes) {
//
////        redirectAttributes.addFlashAttribute("maxSizeExceedMsg", e.getCause().getMessage());
//		redirectAttributes.addFlashAttribute("maxSizeExceedMsg", "이미지 크기가 128KB를 넘습니다. 사이즈를 줄여서 업로드 해 주세요.");
//        return "redirect:/stylist/enroll";
//
//    }
	@ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MultipartException ex, HttpServletRequest request) {

		String requestURL = request.getRequestURL().toString();
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("MultipartException");
		errorResponse.setErrorMessage("File size is exceeding 128KB.");
		
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }
	
	@ExceptionHandler(ShopNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleShopNotFoundException(HttpServletRequest request,
			ShopNotFoundException ex) {
		String requestURL = request.getRequestURL().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("shop.not-found.exception");
		errorResponse.setErrorMessage("Shop with shop_code " + ex.getShopCode() + " not found");

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ShopAlreadyEnrollException.class)
	public ResponseEntity<ErrorResponse> handleShopAlreadyEnrollException(HttpServletRequest request,
			ShopAlreadyEnrollException ex){
		String requestURL = request.getRequestURL().toString();
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("shop.already-enroll.exepction");
		errorResponse.setErrorMessage("Shop with shopname " + ex.getShopName() + " already enrolled shop");
		
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);
	}
	
}
