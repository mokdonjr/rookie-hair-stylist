package yapp.devcamp.hairstylistserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN").anyRequest().authenticated() // 관리자 접근 페이지
//			.antMatchers("/admin/**").access("ROLE_ADMIN"); // 위에 안되면 이걸로
			.antMatchers("/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN").anyRequest().authenticated(); // 모든 페이지 인증 필요

		http.formLogin().loginProcessingUrl("/login") // 로그인 페이지
			.failureUrl("/login?error=true");
//			.defaultSuccessUrl("/"); // 보류
		
		// /logout 호출시 로그아웃
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/"); // 메인페이지 이동
		
		http.exceptionHandling().accessDeniedPage("/access-denied");
			
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// 메인페이지, 리소스들 시큐리티 해제
		web.ignoring().antMatchers("/css/**", "/script/**", "/");
	}

}
