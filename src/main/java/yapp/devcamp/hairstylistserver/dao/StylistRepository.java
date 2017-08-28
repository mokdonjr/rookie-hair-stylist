package yapp.devcamp.hairstylistserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.Stylist;

@Repository
public interface StylistRepository extends JpaRepository<Stylist, Integer>{

	Stylist findByStylistCode(int stylist_code);
	
	Stylist findByStylistNickname(String stylist_nickname);
	
}
