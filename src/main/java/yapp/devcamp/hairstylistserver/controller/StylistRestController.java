package yapp.devcamp.hairstylistserver.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yapp.devcamp.hairstylistserver.exception.StylistNicknameNotFoundException;
import yapp.devcamp.hairstylistserver.exception.StylistNotFoundException;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.service.StylistService;

@RestController
@RequestMapping("/api/stylists")
public class StylistRestController {
	Logger logger = LoggerFactory.getLogger(StylistRestController.class);
	
	@Autowired
	private StylistService stylistService;
	
	@PostMapping
	public ResponseEntity<Stylist> enrollStylist(@RequestBody Stylist stylist){
		if(stylistService.isSavedStylist(stylist)){
			// already exist in stylist table
		}
		stylistService.saveStylist(stylist);
		return new ResponseEntity<Stylist>(stylist, HttpStatus.OK);
	}
	
	@GetMapping("/{stylist_code}")
	public ResponseEntity<Stylist> getStylist(@PathVariable("stylist_code") int stylist_code){
		Stylist stylist = stylistService.findStylistByStylistCode(stylist_code);
		
		if(stylist == null){
			throw new StylistNotFoundException(stylist_code);
		}
		
		return new ResponseEntity<Stylist>(stylist, HttpStatus.OK);
	}
	
	@GetMapping("/{stylist_nickname}")
	public ResponseEntity<Stylist> getStylistByStylistNickname(@PathVariable("stylist_nickname") String stylist_nickname){
		Stylist stylist = stylistService.findStylistByStylistNickname(stylist_nickname);
		
		if(stylist == null){
			throw new StylistNicknameNotFoundException(stylist_nickname);
		}
		
		return new ResponseEntity<Stylist>(stylist, HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Stylist>> getAllStylists(){
		
		List<Stylist> stylists = stylistService.findAllStylists();
		if(stylists.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Stylist>>(stylists, HttpStatus.OK);
	}
	
	// request body : stylist_nickname, qualified, license_image_path, location, detail_location, career
	@PutMapping("/{stylist_code}")
	public ResponseEntity<Stylist> updateStylist(@PathVariable("stylist_code") int stylist_code, @RequestBody Stylist stylist){
		Stylist currentStylist = stylistService.findStylistByStylistCode(stylist_code);
		
		if(currentStylist == null){
			throw new StylistNotFoundException(stylist_code);
		}
		
		currentStylist.setStylistNickname(stylist.getStylistNickname());
		currentStylist.setQualified(stylist.isQualified());
		currentStylist.setLicenseImagePath(stylist.getLicenseImagePath());
		currentStylist.setLocation(stylist.getLocation());
		currentStylist.setDetailLocation(stylist.getDetailLocation());
		
		stylistService.updateStylist(stylist_code, currentStylist);
		
		return new ResponseEntity<Stylist>(currentStylist, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{stylist_code}")
	public ResponseEntity<Void> quitStylist(@PathVariable("stylist_code") int stylist_code){
		Stylist stylist = stylistService.findStylistByStylistCode(stylist_code);
		
		if(stylist == null){
			throw new StylistNotFoundException(stylist_code);
		}
		
		stylistService.deleteStylist(stylist);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
}
