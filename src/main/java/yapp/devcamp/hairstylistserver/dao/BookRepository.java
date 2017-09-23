package yapp.devcamp.hairstylistserver.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	Book findBybookCode(int bookCode);
	
	@Modifying
	@Query(value="delete from book where book_code=?1",nativeQuery=true)
	@Transactional
	void deleteByCode(int bookCode);
}
