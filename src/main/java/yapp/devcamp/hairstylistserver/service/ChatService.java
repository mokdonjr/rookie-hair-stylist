package yapp.devcamp.hairstylistserver.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yapp.devcamp.hairstylistserver.dao.ChatRepository;
import yapp.devcamp.hairstylistserver.model.Chat;
import yapp.devcamp.hairstylistserver.model.User;

@Service
@Transactional
public class ChatService {

	@Autowired
	private ChatRepository chatRepository;
	
	public void saveChat(Chat chat){
		chatRepository.save(chat);
	}
	
	public List<Chat> findByReceiver(String receiver){
		return chatRepository.findByReceiver(receiver);
	}
	
	public List<Chat> findByUser(User user){ // sender
		return chatRepository.findByUser(user);
	}
	
	public List<Chat> findByReceiverAndUser(String receiver, User user){
		return chatRepository.findByReceiverAndUser(receiver, user);
	}
}
