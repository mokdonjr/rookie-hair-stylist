package yapp.devcamp.hairstylistserver.dao;

import javax.transaction.Transactional;

import org.jboss.logging.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.Postscript;

@Repository
public interface PostscriptRepository extends JpaRepository<Postscript, Integer> {
	Postscript findByPostscriptCode(int postscriptCode);
	
	@Modifying
	@Query(value="insert into postscript(grade,content,user_id,shop_code) values(?1,?2,?3,?4)",nativeQuery=true)
	@Transactional
	void insert(int grade, String content,int id,int shopCode);
}
