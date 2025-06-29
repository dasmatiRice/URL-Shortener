package com.app.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> testclass(Exception ex){
		
		log.error("Exception occured: ",ex.getMessage());
		
		return new ResponseEntity<>("Error recieved: " + ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<String> originalUrlExists(Exception ex){
		
		log.error("Exception occured: ",ex.getMessage());
		
		return new ResponseEntity<>("Error recieved: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> invalidParameter(Exception ex){
		
		log.error("Exception occured: ",ex.getMessage());
		
		return new ResponseEntity<>("Error recieved: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ExpiredUrlException.class)
	public ResponseEntity<String> expiredUrl(Exception ex){
		
		log.error("Exception occured: ",ex.getMessage());
		
		return new ResponseEntity<>("Error recieved: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
}
