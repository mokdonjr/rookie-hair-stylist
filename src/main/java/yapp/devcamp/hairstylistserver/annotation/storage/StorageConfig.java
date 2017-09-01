package yapp.devcamp.hairstylistserver.annotation.storage;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfig {
	
//	@Bean
//    CommandLineRunner init(StorageService storageService) {
//        return (args) -> {
////            storageService.deleteAll();
//            storageService.init();
//        };
//    }
	
//	@Bean
//	public MultipartResolver multipartResolver(){
//		return new CommonsMultipartResolver();
//	}

}
