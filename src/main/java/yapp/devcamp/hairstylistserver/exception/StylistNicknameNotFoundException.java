package yapp.devcamp.hairstylistserver.exception;

public class StylistNicknameNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -7655081274713077402L;
	
	private String stylistNickname;
	
	public StylistNicknameNotFoundException(String stylistNickname){
		this.stylistNickname = stylistNickname;
	}
	
	public String getStylistNickname(){
		return this.stylistNickname;
	}
}
