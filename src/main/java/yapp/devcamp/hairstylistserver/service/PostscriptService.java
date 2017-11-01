package yapp.devcamp.hairstylistserver.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import yapp.devcamp.hairstylistserver.dao.PostscriptRepository;
import yapp.devcamp.hairstylistserver.model.Postscript;
import yapp.devcamp.hairstylistserver.model.Shop;
import yapp.devcamp.hairstylistserver.model.User;

@Service
@Transactional
public class PostscriptService {

	@Autowired
	PostscriptRepository postscriptRepository;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	ShopService shopService;
	
	public void enrollScript(Postscript postscript){
		Calendar calendar = Calendar.getInstance();
		String today = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
		postscript.setWriteDate(today);

		postscriptRepository.save(postscript);
	}
	
	public void deleteScript(int postscriptCode){
		postscriptRepository.deleteByCode(postscriptCode);
	}
	
	public List<Postscript> selectAll(Shop shop){
		return postscriptRepository.findByShop(shop);
	}
	
	public Postscript selectByPostscriptCode(int postscriptCode){
		return postscriptRepository.findByPostscriptCode(postscriptCode);
	}
}
