package yapp.devcamp.hairstylistserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import nz.net.ultraq.thymeleaf.LayoutDialect;

/*
 * issue 
 * https://stackoverflow.com/questions/18309864/secauthorize-and-secauthentication-annotations-dont-work
 */
@Configuration
public class ThymeleafConfig {
	
	// issue
	// https://stackoverflow.com/questions/31601734/how-can-i-configure-springboot-with-thymeleaf-and-use-secauthentication-tag/31622977#31622977
	@Bean
	public SpringTemplateEngine templateEngine(TemplateResolver templateResolver) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		templateEngine.addDialect(new LayoutDialect()); // added
		templateEngine.addDialect(new SpringSecurityDialect());
		return templateEngine;
	}
}
