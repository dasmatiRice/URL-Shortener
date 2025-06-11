package com.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> testclass(Exception ex){
		
		return new ResponseEntity<>("Error recieved: " + ex.getMessage(), HttpStatus.NOT_FOUND);
	}
}
