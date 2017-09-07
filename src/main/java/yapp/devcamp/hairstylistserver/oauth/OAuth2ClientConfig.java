package yapp.devcamp.hairstylistserver.oauth;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.filter.CompositeFilter;

import yapp.devcamp.hairstylistserver.service.StylistService;
import yapp.devcamp.hairstylistserver.service.UserService;

@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfig {
	
	Logger logger = LoggerFactory.getLogger(OAuth2ClientConfig.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StylistService stylistService;
	
	@Autowired
	private OAuth2ClientContext oauth2ClientContext;

	@Bean
	@ConfigurationProperties("facebook")
	public ClientResources facebook(){
		return new ClientResources();
	}
	
	@Bean
	@ConfigurationProperties("kakao")
	public ClientResources kakao(){
		return new ClientResources();
	}
	
	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter){
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(-100); // 필터 우선순위 높게
		return registration;
	}
	
	@Bean("sso.filter")
	public Filter ssoFilter(){
		CompositeFilter filter = new CompositeFilter();
		List<Filter> filters = new ArrayList<>();
		filters.add(ssoFilter(facebook(), "/login/facebook", SocialType.FACEBOOK, AuthorityType.USER));
		filters.add(ssoFilter(kakao(), "/login/kakao", SocialType.KAKAO, AuthorityType.USER));		
		filter.setFilters(filters);
		return filter;
	}
	
	private Filter ssoFilter(ClientResources client, String path, SocialType socialType, AuthorityType authorityType){
		OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter = new OAuth2ClientAuthenticationProcessingFilter(path);
		// 1. filter에 oauth2Rest 등록
		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
		oAuth2ClientAuthenticationFilter.setRestTemplate(oAuth2RestTemplate);
		// 2. redirect url
		StringBuilder redirectUrl = new StringBuilder("/users");
		redirectUrl.append("/" + socialType.getValue()).append("/complete"); // url -> /facebook|kakao/complete
		// 3. filter에 tokenService 등록
		oAuth2ClientAuthenticationFilter.setTokenServices(new UserTokenService(client, socialType, authorityType, userService, stylistService));
		// 4. filter에 handler 등록
		oAuth2ClientAuthenticationFilter.setAuthenticationSuccessHandler(
			(request, response, authentication) -> response.sendRedirect(redirectUrl.toString())
		);
		oAuth2ClientAuthenticationFilter.setAuthenticationFailureHandler(
			(request, response, exeption) -> response.sendRedirect("/error")
		);
		
		return oAuth2ClientAuthenticationFilter;
	}
}
