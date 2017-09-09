package yapp.devcamp.hairstylistserver.exception;

public class StorageFileNotFoundException extends StorageException{
	private static final long serialVersionUID = -253049311996574897L;

	public StorageFileNotFoundException(String message){
		super(message);
	}
	
	public StorageFileNotFoundException(String message, Throwable cause){
		super(message, cause);
	}

}
