package com.app.validation;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;


@Component
@NoArgsConstructor
public class ValidatorEngine extends RuntimeException {
	
	public String validate(String apiDevKey, String shortUrl) throws RuntimeException  {
		
		
		return validateRedirectUrl(apiDevKey,shortUrl);
		
	}
	
	public String validateRedirectUrl(String apiDevKey, String shortUrl) throws RuntimeException  {
		if(!(apiDevKey instanceof  String)){
			throw new IllegalArgumentException();
		}
		if(!(shortUrl instanceof  String)){
			throw new IllegalArgumentException();
		}
		
		return "done validating";
	}
	

}
