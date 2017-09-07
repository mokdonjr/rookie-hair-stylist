package yapp.devcamp.hairstylistserver.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
	
	// 폴더명 디렉터리
	private String location = "upload";
	
	public String getLocation(){
		return this.location;
	}

}
