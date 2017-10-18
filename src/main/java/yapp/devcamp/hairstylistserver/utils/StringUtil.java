package yapp.devcamp.hairstylistserver.utils;

public class StringUtil {

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
}
