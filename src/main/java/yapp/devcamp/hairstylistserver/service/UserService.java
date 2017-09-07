package yapp.devcamp.hairstylistserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yapp.devcamp.hairstylistserver.dao.UserRepository;
import yapp.devcamp.hairstylistserver.model.User;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public void saveUser(User user){
		userRepository.save(user);
	}
	
	public User findById(int id){
		return userRepository.findById(id);
	}
	
	public User findByEmail(String email){
		return userRepository.findByEmail(email);
	}
	
	public User findByPrincipal(String principal){
		return userRepository.findByPrincipal(principal);
	}
	
	public void updateUser(int id, User user){
		userRepository.save(user); // merge new user
//		userRepository.saveAndFlush(user); // 위에 안되면 이걸로
//		return findById(id);
	}
	
	public void deleteUser(User user){
		userRepository.delete(user);
	}
	
	public boolean isNotExistUser(String principal){
		return userRepository.findByPrincipal(principal) == null ? true:false;
	}

}
