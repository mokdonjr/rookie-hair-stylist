package yapp.devcamp.hairstylistserver.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yapp.devcamp.hairstylistserver.model.Role;
import yapp.devcamp.hairstylistserver.model.User;

@Data
@EqualsAndHashCode(callSuper=false)
public class SocialUserDetails extends org.springframework.security.core.userdetails.User {
	private static final long serialVersionUID = 513539402689813795L;
	
	private long userDetailCode;
	
	public SocialUserDetails(User user){
		super(user.getEmail(), user.getPrincipal(), getAuthorities(user.getRoles()));
		userDetailCode = user.getId();
	}
	
	public static List<GrantedAuthority> getAuthorities(List<Role> roles){
		List<GrantedAuthority> authorityList = new ArrayList<>(1);
		authorityList.add(new SimpleGrantedAuthority(RoleType.USER.toString()));
		if(roles != null){
			roles.stream().forEach(
				(Role role) -> {
					authorityList.add(new SimpleGrantedAuthority(role.getRoleName()));
				}
			);
		}
		return authorityList;
	}
}
