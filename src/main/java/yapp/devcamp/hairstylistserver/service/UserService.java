package yapp.devcamp.hairstylistserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yapp.devcamp.hairstylistserver.dao.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

}
