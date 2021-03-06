package yapp.devcamp.hairstylistserver.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.Postscript;
import yapp.devcamp.hairstylistserver.model.Shop;

@Repository
public interface PostscriptRepository extends JpaRepository<Postscript, Integer> {
	
	Postscript findByPostscriptCode(int postscriptCode);
	
	@Modifying
	@Query(value="delete from postscript where postscript_code=?1",nativeQuery=true)
	@Transactional
	void deleteByCode(int postscriptCode);
	
	List<Postscript> findByShop(Shop shop);
}
