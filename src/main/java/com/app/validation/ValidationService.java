package com.app.validation;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {


	public void validateCreateUrl(String apiDevKey, String originalUrl, String customAlias, LocalDate expiryTime) throws RuntimeException  {
		if(!(apiDevKey instanceof  String)){
			throw new IllegalArgumentException();
		}
		if(!(originalUrl instanceof  String)){
			throw new IllegalArgumentException();
		}
		
	}
	
	public void validateRedirectUrl(String apiDevKey, String shortUrl) throws RuntimeException  {
		if(!(apiDevKey instanceof  String)){
			throw new IllegalArgumentException();
		}
		if(!(shortUrl instanceof  String)){
			throw new IllegalArgumentException();
		}
		
	}
	
	
}
