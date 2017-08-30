package yapp.devcamp.hairstylistserver.aop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import yapp.devcamp.hairstylistserver.model.User;
import yapp.devcamp.hairstylistserver.oauth.SocialType;

@Component
@Aspect
public class UserAspect {
	Logger logger = LoggerFactory.getLogger("yapp.devcamp.hairstylistserver.aop.UserAspect");

	@Around("execution(* *(.., @yapp.devcamp.hairstylistserver.annotation.SocialUser (*), ..))")
	public Object convertUser(ProceedingJoinPoint joinPoint) throws Throwable{
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
		User user = (User) session.getAttribute("user");
		
		if(user == null){
			OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
			Map<String, String> map = (HashMap<String, String>) authentication.getUserAuthentication().getDetails();
			user = checkSocialType(String.valueOf(authentication.getAuthorities().toArray()[0]), map);
		}
		
		User finalUser = user;
		Object[] args = Arrays.stream(joinPoint.getArgs()).map(data -> {
			if(data instanceof User){
				data = finalUser;
			}
			return data;
		}).toArray();
		
		return joinPoint.proceed(args);
	}
	
	private User checkSocialType(String authority, Map<String, String> map){
		if(SocialType.FACEBOOK.isEquals(authority))
			return saveFacebook(map);
		else if(SocialType.KAKAO.isEquals(authority))
			return saveKakao(map);
		return null;
	}
	
	private User saveFacebook(Map<String, String> map){
		User user = new User();
		user.setPrincipal(map.get("id"));
		user.setUsername(map.get("name"));
		user.setEmail(map.get("email")); // 반드시 scope:email 지정 / userInfoUri에 field 파라미터 지정
		user.setProfileImagePath("http://graph.facebook.com/" + map.get("id") + "/picture?type=square");
		user.setSocialType(SocialType.FACEBOOK);
		return user;
	}
	
	private User saveKakao(Map<String, String> map){
		HashMap<String, String> propertyMap = (HashMap<String, String>) (Object) map.get("properties");
		User user = new User();
		user.setPrincipal(String.valueOf(map.get("id")));
		user.setUsername(propertyMap.get("nickname"));
		user.setEmail(map.get("kaccount_email"));
		user.setProfileImagePath(propertyMap.get("thumbnail_image"));
		user.setSocialType(SocialType.KAKAO);
		return user;
	}
}
