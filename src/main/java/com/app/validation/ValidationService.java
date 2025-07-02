package com.app.validation;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.app.service.GeminiService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import jakarta.validation.ValidationException;

@Service
public class ValidationService {
	
	@Autowired
	GeminiService geminiService;


	public void validateCreateUrl(String apiDevKey, String originalUrl, String customAlias, LocalDate expiryTime) throws RuntimeException  {
		if(!(apiDevKey instanceof  String)){
			throw new IllegalArgumentException();
		}
		if(!(originalUrl instanceof  String)){
			throw new IllegalArgumentException();
		}
		
		boolean isMalicious=geminiService.checkForMaliciousUrl(originalUrl);
		
		if(isMalicious) {
			throw new ValidationException("Malicious URL");
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
