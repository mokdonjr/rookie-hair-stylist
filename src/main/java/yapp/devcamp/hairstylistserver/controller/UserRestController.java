package yapp.devcamp.hairstylistserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yapp.devcamp.hairstylistserver.exception.UserNotFoundException;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
	Logger logger = LoggerFactory.getLogger("yapp.devcamp.hairstylistserver.controller.UserRestController");
	
	@Autowired
	private UserService userService;
	
	// create user는 제공되지 않음
	
	@GetMapping(value="/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") int id){
		User user = userService.findById(id);
		
		if(user == null){
			throw new UserNotFoundException(id);
		}
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	// request body : email, nickname, username, profile_image_path
	@PutMapping(value="/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User user){
		User currentUser = userService.findById(id);
		
		if(currentUser == null){
			throw new UserNotFoundException(id);
		}
		
		currentUser.setEmail(user.getEmail());
		currentUser.setNickname(user.getNickname());
		currentUser.setProfileImagePath(user.getProfileImagePath());
		currentUser.setUsername(user.getUsername());
		
//		userService.saveUser(currentUser); // merge user
		userService.updateUser(id, currentUser);
		
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") int id){
		User currentUser = userService.findById(id);
		
		if(currentUser == null){
			throw new UserNotFoundException(id);
		}
		
		userService.deleteUser(currentUser);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
