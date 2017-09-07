package yapp.devcamp.hairstylistserver.exception;

public class StylistNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -4416898361540117950L;
	
	private int stylistCode;
	
	public StylistNotFoundException(int stylistCode){
		this.stylistCode = stylistCode;
	}
	
	public int getStylistCode(){
		return this.stylistCode;
	}
}
