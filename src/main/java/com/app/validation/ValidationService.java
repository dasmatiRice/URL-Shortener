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
	
}
