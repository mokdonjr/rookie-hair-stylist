package yapp.devcamp.hairstylistserver.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.Book;
import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	Book findBybookCode(int bookCode);
	
	@Modifying
	@Query(value="delete from book where book_code=?1",nativeQuery=true)
	@Transactional
	void deleteByCode(int bookCode);
	
	@Modifying
	@Query(value="select book_time from book where book_date=?1 and book_status=0",nativeQuery=true)
	List<String> selectTimeByDate(String selectDate);
	
	List<Book> findByUser(User user);
	
	@Modifying
	@Query(value="select book_code,book_date,book_status,book_time,option_code,product_code,shop_code,user_id,stylist_code from book where stylist_code=?1",nativeQuery=true)
	List<Book> selectByStylistCode(int stylistCode);
	
	@Modifying
	@Query(value="select book_code,book_date,book_status,book_time,option_code,product_code,shop_code,user_id,stylist_code from book where user_id=?1 and book_status=0",nativeQuery=true)
	List<Book> orderList(int userId);
	
	@Modifying
	@Query(value="select book_code,book_date,book_status,book_time,option_code,product_code,shop_code,user_id,stylist_code from book where user_id=?1 and book_status=1",nativeQuery=true)
	List<Book> waitList(int userId);
	
	@Modifying
	@Query(value="select book_code,book_date,book_status,book_time,option_code,product_code,shop_code,user_id,stylist_code from book where stylist_code=?1 and book_status=0",nativeQuery=true)
	List<Book> stylistOrderList(int stylistCode);
	
	@Modifying
	@Query(value="select book_code,book_date,book_status,book_time,option_code,product_code,shop_code,user_id,stylist_code from book where stylist_code=?1 and book_status=1",nativeQuery=true)
	List<Book> stylistWaitList(int stylistCode);
}
