package yapp.devcamp.hairstylistserver.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StringUtil {
	
	private static Logger logger = LoggerFactory.getLogger(StringUtil.class);
	
	private static final String ENCODE_TYPE = "UTF-8";

	public static String getBaseURL(String requestURL){
		int baseIndex = getThirdSlashIndexFromURL(requestURL);
		if(baseIndex != -1) return requestURL.substring(0, baseIndex);
		else return "ERROR_URL";
	}
	
	private static int getThirdSlashIndexFromURL(String requestURL){
		int slashCount = 0;
		for(int index=0; index<requestURL.length(); index++){
			if(requestURL.charAt(index) == '/') slashCount++;
			if(slashCount == 3) return index;
		}
		return -1;
	}
	
	public static String encodePath(String value) throws UnsupportedEncodingException{
//		String encodedValueExceptBlank = URLEncoder.encode(value, ENCODE_TYPE);
		String encodedValueExceptBlank = URLEncoder.encode(value, StandardCharsets.UTF_8.name());
		String encodedValue = encodedValueExceptBlank.replace("+", "%20"); // 스페이스바
		logger.warn("encodedValue : " + encodedValue);
		return encodedValue;
	}
	
//	public static String encodePath(String value) {
//		String encodedValue = value.replace(" ", "+"); // 스페이스바
//		logger.warn("encodedValue : " + encodedValue);
//		return encodedValue;
//	}
}
