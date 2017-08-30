package yapp.devcamp.hairstylistserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import yapp.devcamp.hairstylistserver.dao.UserRepository;
import yapp.devcamp.hairstylistserver.model.User;

@Service("socialUserDetailService")
public class SocialUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	// loadUserByPrincipal
	@Override
	public SocialUserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
		User user = userRepository.findByPrincipal(principal);
		if(user == null){
			throw new UsernameNotFoundException(principal);
		}
		return new SocialUserDetails(user);
	}

}
