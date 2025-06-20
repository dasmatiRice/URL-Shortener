package com.app.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

	@Autowired
	ValidatorEngine validatorEngine;
	
	
	public void validate(String apiDevKey, String shortUrl) throws RuntimeException  {
		String s= validatorEngine.validateRedirectUrl(apiDevKey,shortUrl);
		System.out.println(s);
	}


	public void validateCreate(String apiDevKey, String originalUrl, String customAlias) {
		
		String s= validateCreateUrl(apiDevKey,originalUrl,customAlias);
		System.out.println(s);
	}
	
	
	public String validateCreateUrl(String apiDevKey, String originalUrl, String customAlias) throws RuntimeException  {
		if(!(apiDevKey instanceof  String)){
			throw new IllegalArgumentException();
		}
		if(!(originalUrl instanceof  String)){
			throw new IllegalArgumentException();
		}
		
		return "done validating";
	}
	
	
}
