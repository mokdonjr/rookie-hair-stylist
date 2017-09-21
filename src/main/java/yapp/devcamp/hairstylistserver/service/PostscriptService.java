package yapp.devcamp.hairstylistserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yapp.devcamp.hairstylistserver.dao.PostscriptRepository;
import yapp.devcamp.hairstylistserver.model.Postscript;

@Service
public class PostscriptService {

	@Autowired
	PostscriptRepository postscriptRepository;
	
	public void enrollScript(Postscript postscript){
		postscriptRepository.insert(postscript.getGrade(), postscript.getContent(), postscript.getUser().getId(), postscript.getShop().getShopCode());
	}
	
	public void deleteScript(Postscript postscript){
		postscriptRepository.delete(postscript);
	}
	
	public List<Postscript> selectAll(){
		return postscriptRepository.findAll();
	}
	
	public Postscript selectByPostscriptCode(int postscriptCode){
		return postscriptRepository.findByPostscriptCode(postscriptCode);
	}
	
}
