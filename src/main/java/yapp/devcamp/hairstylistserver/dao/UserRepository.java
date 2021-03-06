package yapp.devcamp.hairstylistserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String email);
	
	User findById(int id);
	
	User findByPrincipal(String principal);
}
