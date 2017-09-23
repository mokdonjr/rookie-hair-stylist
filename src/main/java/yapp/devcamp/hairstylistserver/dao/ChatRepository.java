package yapp.devcamp.hairstylistserver.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yapp.devcamp.hairstylistserver.model.Chat;
import yapp.devcamp.hairstylistserver.model.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer>{

	List<Chat> findByReceiver(String receiver);
	
	List<Chat> findByUser(User user);
	
	List<Chat> findByReceiverAndUser(String receiver, User user);
}
