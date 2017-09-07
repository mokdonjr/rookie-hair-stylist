package yapp.devcamp.hairstylistserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	
}
