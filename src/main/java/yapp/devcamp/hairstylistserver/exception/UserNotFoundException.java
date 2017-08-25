package yapp.devcamp.hairstylistserver.exception;

public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507805695756002250L;
	
	private int userId;
	
	public UserNotFoundException(int userId){
		this.userId = userId;
	}
	
	public int getUserId(){
		return this.userId;
	}
}
