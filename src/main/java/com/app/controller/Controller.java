package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.CreateResponse;
import com.app.service.RedirectionService;
import com.app.service.ShortenerService;
import com.app.validation.ValidationService;
import com.app.validation.ValidatorEngine;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
public class Controller {

	// The URL shortener should support 3 methods creation, deletion, and redirect
	@Autowired
	ShortenerService shortenerService;

	@Autowired
	RedirectionService redirectionService;
	
	@Autowired
	ValidationService validationService;


	@PostMapping("/create")
	public CreateResponse testGetMethod(@RequestParam(value = "name", defaultValue = "test") String apiDevKey,
			@RequestParam(value = "name", defaultValue = "test") String originalUrl,
			@RequestParam(value = "name", defaultValue = "test") String customAlias) {

//	String URL="";
//	String apiDevKey="";

		shortenerService.createURL(apiDevKey, originalUrl);

		CreateResponse response = new CreateResponse();
		response.setResponse("test");
		return response;

	}

	@GetMapping("/delete")
	public CreateResponse deleteUrl(@RequestParam(value = "name", defaultValue = "test") String name) {

		CreateResponse response = new CreateResponse();
		response.setResponse("test");
		return response;

	}

	@GetMapping("/redirectUrl")
	public ResponseEntity<String> redirectUrl(@Valid @NotBlank @RequestParam String apiDevKey,
			@Valid @NotBlank @RequestParam String shortUrl) {

		// call validator here to validate data fields
		validationService.validate(apiDevKey,shortUrl);
		
	
		String urlPath = redirectionService.redirectUrl(apiDevKey, shortUrl);

		return new ResponseEntity<>("Redirecting to URL " + urlPath, HttpStatus.OK);

	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
}
