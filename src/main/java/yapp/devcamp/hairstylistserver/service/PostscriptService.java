package yapp.devcamp.hairstylistserver.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yapp.devcamp.hairstylistserver.dao.PostscriptRepository;
import yapp.devcamp.hairstylistserver.model.Postscript;

@Service
@Transactional
public class PostscriptService {

	@Autowired
	PostscriptRepository postscriptRepository;
	
	public void enrollScript(Postscript postscript){
		Calendar calendar = Calendar.getInstance();
		String today = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
		postscript.setWriteDate(today);
		postscriptRepository.save(postscript);
	}
	
	public void deleteScript(int postscriptCode){
		postscriptRepository.deleteByCode(postscriptCode);
	}
	
	public List<Postscript> selectAll(){
		return postscriptRepository.findAll();
	}
	
	public Postscript selectByPostscriptCode(int postscriptCode){
		return postscriptRepository.findByPostscriptCode(postscriptCode);
	}
}
