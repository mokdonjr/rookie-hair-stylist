package yapp.devcamp.hairstylistserver.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		http
			.authorizeRequests()
				.anyRequest().permitAll();
//				.antMatchers("/", "/users/login**", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
//				.antMatchers("/stylist/enroll", "/stylist/apply").hasRole("USER")
//				.antMatchers("/stylist/**").hasRole("STYLIST") // stylist/mypage
//				.antMatchers("/admin/**").hasRole("ADMIN")
//				.antMatchers("/shop/**").hasAnyRole("USER", "STYLIST", "ADMIN")
//				.antMatchers("/chat/**").hasAnyRole("USER", "STYLIST", "ADMIN")
//				.antMatchers("/websocketHandler","/chat**", "/app**", "/topic**").hasAnyRole("USER", "STYLIST", "ADMIN")
//				//.antMatchers("/front/**").hasAnyRole("USER", "STYLIST", "ADMIN") // 성훈 테스트
//				.anyRequest().authenticated()
//				.and()
//			.formLogin().loginProcessingUrl("/users/login").failureUrl("/users/login?error=true")
//				.and()
//			.headers().frameOptions().disable()
//				.and()
//			.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/users/login"))
//				.and()
//			.logout().logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll()
//				.and()
//			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//				.and()
//			.addFilterBefore(filter, CsrfFilter.class)
//			.addFilterBefore((Filter)context.getBean("sso.filter"), BasicAuthenticationFilter.class);
//			.csrf().disable();// 보류
	}

}
