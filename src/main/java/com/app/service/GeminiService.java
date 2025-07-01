package com.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.app.controller.Controller;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Service
public class GeminiService {

	private static final Logger log = LoggerFactory.getLogger(GeminiService.class);
	
	public Boolean checkForMaliciousUrl(String input) {
		Client client = new Client();
		
		String question="Can validate if the following link is malicious or not? If it has a high chance of being maliscious return only true, otherwise return only false : "+ input;

		GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash",
				question,
				null);
		log.info(response.text());
		System.out.println();
		
		return Boolean.valueOf(response.text());
	}
}
