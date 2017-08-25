package yapp.devcamp.hairstylistserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yapp.devcamp.hairstylistserver.dao.StylistRepository;
import yapp.devcamp.hairstylistserver.model.Stylist;

@Service
public class StylistService {
	
	@Autowired
	private StylistRepository stylistRepository;
	
	public void saveStylist(Stylist stylist){
		stylistRepository.save(stylist);
	}
	
	public Stylist getStylistByStylistCode(int stylist_code){
		return stylistRepository.findByStylistCode(stylist_code);
	}
	
	public Stylist getStylistByStylistNickname(String stylist_nickname){
		return stylistRepository.findByStylistNickname(stylist_nickname);
	}
	
	public List<Stylist> getAllStylists(){
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
