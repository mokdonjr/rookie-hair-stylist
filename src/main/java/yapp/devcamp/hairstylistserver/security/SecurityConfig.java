package yapp.devcamp.hairstylistserver.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity // issue
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) // issue
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	@Qualifier("socialUserDetailService")
	private UserDetailsService socialUserDetailService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		http
			.authorizeRequests()
				.antMatchers("/", "/login", "/login/**", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
//				.antMatchers("/**").hasAuthority("ROLE_USER").anyRequest().authenticated()
//				.antMatchers("/stylist/enroll").hasRole("USER")
//				.antMatchers("/stylist/**").hasRole("STYLIST")
				.antMatchers("/admin**").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
			.formLogin().loginProcessingUrl("/login").failureUrl("/login?error=true")
				.and()
			.headers().frameOptions().disable()
				.and()
			.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
				.and()
			.logout().logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll()
				.and()
			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.and()
			.addFilterBefore(filter, CsrfFilter.class)
			.addFilterBefore((Filter)context.getBean("sso.filter"), BasicAuthenticationFilter.class);
//			.csrf().disable(); 보류
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(socialUserDetailService);
	}
	
	// issue 
	// https://stackoverflow.com/questions/23686022/spring-boot-security-thymeleaf-secauthorize-url-not-working
//	@Override
//    public void configure(final WebSecurity web) throws Exception {
//        final HttpSecurity http = getHttp();
//        web.postBuildAction(new Runnable() {
//            @Override
//            public void run() {
//                web.securityInterceptor(http.getSharedObject(FilterSecurityInterceptor.class));
//            }
//        });
//    }
	
	

}
