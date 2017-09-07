package yapp.devcamp.hairstylistserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yapp.devcamp.hairstylistserver.dao.StylistRepository;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;

@Service
public class StylistService {
	
	@Autowired
	private StylistRepository stylistRepository;
	
	@Transactional
	public void saveStylist(Stylist stylist){
		stylistRepository.save(stylist);
	}
	
	public Stylist findStylistByStylistCode(int stylist_code){
		return stylistRepository.findByStylistCode(stylist_code);
	}
	
	public Stylist findStylistByStylistNickname(String stylist_nickname){
		return stylistRepository.findByStylistNickname(stylist_nickname);
	}
	
	public Stylist findStylistByUser(User user){
		return stylistRepository.findByUser(user);
	}
	
	public boolean isAlreadyEnrollUser(User user){ // must be one to one User/Stylist
		return stylistRepository.findByUser(user) != null;
	}
	
	public List<Stylist> findAllStylists(){
		return stylistRepository.findAll();
	}
	
	public void updateStylist(int stylist_code, Stylist stylist){
		stylistRepository.save(stylist);
//		stylistRepository.saveAndFlush(stylist);
//		return stylistRepository.findByStylistCode(stylist_code);
	}
	
	public void deleteStylist(Stylist stylist){
		stylistRepository.delete(stylist);
	}
	
	public boolean isSavedStylist(Stylist stylist){
		return stylistRepository.findByStylistCode(stylist.getStylistCode()) != null;
	}

}
