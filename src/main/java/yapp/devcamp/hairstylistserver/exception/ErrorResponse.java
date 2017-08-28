package yapp.devcamp.hairstylistserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	private String errorCode;
	private String errorMessage;
	private String requestURL;
}
