package yapp.devcamp.hairstylistserver.exception;

public class StorageException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8895551793978354205L;

	public StorageException(String message){
		super(message);
	}
	
	public StorageException(String message, Throwable cause){
		super(message, cause);
	}

}
