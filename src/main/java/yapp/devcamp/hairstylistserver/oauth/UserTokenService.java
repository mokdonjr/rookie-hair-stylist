package yapp.devcamp.hairstylistserver.oauth;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import yapp.devcamp.hairstylistserver.model.Stylist;
import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.service.StylistService;
import yapp.devcamp.hairstylistserver.service.UserService;

public class UserTokenService extends UserInfoTokenServices {
	
	Logger logger = LoggerFactory.getLogger(UserTokenService.class);
	
	private UserService userService; // @Autowired null
	private StylistService stylistService;
	
	public UserTokenService(ClientResources resources, SocialType socialType, AuthorityType authorityType, UserService userService, StylistService stylistService) {
		super(resources.getResource().getUserInfoUri(), resources.getClient().getClientId());
		this.userService = userService;
		this.stylistService = stylistService;
		setAuthoritiesExtractor(new OAuth2AuthoritiesExtractor(socialType, authorityType, userService, stylistService));
	}
	
	public static class OAuth2AuthoritiesExtractor implements AuthoritiesExtractor{
		
		Logger logger = LoggerFactory.getLogger(OAuth2AuthoritiesExtractor.class);
		
		private String socialType;
		private String authorityType; // added
		
		private UserService userService;
		private StylistService stylistService;
		public OAuth2AuthoritiesExtractor(SocialType socialType, AuthorityType authorityType, UserService userService, StylistService stylistService){
			this.socialType = socialType.getRoleType();
			this.authorityType = authorityType.getRoleType();
			this.userService = userService;
			this.stylistService = stylistService;
		}
		
		// hard wired UsernamePasswordAuthenticationToken
		@Override
		public List<GrantedAuthority> extractAuthorities(Map<String, Object> map){
			
			if(userService == null){
				logger.warn("userService is null");
			}
			else logger.warn("userService is not null");
			
			/* [from User to Stylist strategies]
			 *	0. 'registered' and 'map.email == rookies.yapp@gmail.com' User : ROLE_ADMIN
			 *	1. 'registered' and not 'applyed-stylist' User : ROLE_USER
			 *	2. 'registered' and 'applyed-stylist' User : ROLE_USER
			 *	3. 'registered', 'applyed-stylist' and 'enrolled-stylist-by-admin' User : ROLE_STYLIST 
			 */
			String adminPrincipal = "104670550274248";
			String currentPrincipal = map.get("id").toString(); // no idea why 'email' not available
			logger.warn("Hardwired 관리자 Principal : " + adminPrincipal + " / Current User Principal : " + currentPrincipal);
			if(adminPrincipal.equals(currentPrincipal)){
				logger.warn("관리자가 로그인 했습니다." + currentPrincipal);
				// 0. 'registered' and 'map.email == rookies.yapp@gmail.com' User : ROLE_ADMIN
				return AuthorityUtils.createAuthorityList(this.socialType, AuthorityType.ADMIN.getRoleType());
			}
			
			User user = userService.findByPrincipal(currentPrincipal);
			if(user != null){
				Stylist stylist = stylistService.findStylistByUser(user);
				if(stylist != null && user.getAuthorityType().equals(AuthorityType.STYLIST)){
					logger.warn("스타일리스트가 로그인 했습니다." + currentPrincipal);
					// 3. 'registered', 'applyed-stylist' and 'enrolled-stylist-by-admin' User : ROLE_STYLIST 
					return AuthorityUtils.createAuthorityList(this.socialType, AuthorityType.STYLIST.getRoleType());
				}				
			}
			
			// 1. 'registered' and not 'applyed-stylist' User : ROLE_USER
			// 2. 'registered' and 'applyed-stylist' User : ROLE_USER
			logger.warn("일반고객이 로그인 했습니다." + currentPrincipal);
			return AuthorityUtils.createAuthorityList(this.socialType, this.authorityType);
			
		}
	}
}
