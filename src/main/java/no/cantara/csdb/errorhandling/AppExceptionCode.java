package no.cantara.csdb.errorhandling;

import org.springframework.http.HttpStatus;

public class AppExceptionCode {

	//USER EXCEPTIONS
	public static final AppException USER_UNAUTHORIZED_6000 = new AppException(HttpStatus.UNAUTHORIZED, 6000, "Unauthorized", "Unauthorized", "");
	
	//APPLICATION EXCEPTIONS
	public static final AppException APP_ILLEGAL_7000 = new AppException(HttpStatus.FORBIDDEN, 7000, "Illegal Application.", "Application is invalid", "");

	//MISC
	public static final AppException MISC_MISSING_PARAMS_9998 = new AppException(HttpStatus.BAD_REQUEST, 9998, "Missing required parameters", "Missing required parameters", "");

}
