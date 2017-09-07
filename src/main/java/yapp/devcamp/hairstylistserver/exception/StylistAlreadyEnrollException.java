package yapp.devcamp.hairstylistserver.exception;

public class StylistAlreadyEnrollException extends RuntimeException {
	private static final long serialVersionUID = 3091173036919551635L;

	private int userId;
	
	public StylistAlreadyEnrollException(int userId){
		this.userId = userId;
	}
	
	public int getUserId(){
		return this.userId;
	}
}
