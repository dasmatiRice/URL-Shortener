package com.app.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Repository.UrlItem;
import com.app.Repository.UrlItemRepository;
import com.app.model.CreateResponse;
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
	ValidationService validationService;
	
	@Autowired
	UrlItemRepository urlRepository;


	@PostMapping("/create")
	public CreateResponse testGetMethod(@RequestParam String apiDevKey,
			@RequestParam String originalUrl,
			@RequestParam(required = false)  String customAlias) {


		// call validator here to validate data fields
		validationService.validateCreate(apiDevKey,originalUrl,customAlias);

		String s= shortenerService.createURL(apiDevKey, originalUrl,customAlias);

		CreateResponse response = new CreateResponse();
		response.setResponse(s);
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
		
	
		String urlPath = shortenerService.redirectUrl(apiDevKey, shortUrl);

		return new ResponseEntity<>("Redirecting to URL: " + urlPath, HttpStatus.OK);

	}

	@GetMapping("/GetAll")
	public List<UrlItem> GetAll() {
		
		List<UrlItem> response= urlRepository.findAll();
		return response;
	}
	
	@GetMapping("/saveItem")
	public UrlItem saveItem() {
		
		UrlItem u= new UrlItem((long)123,"or","sh",5);
		UrlItem x= new UrlItem((long)1233,"or","sh",5);
		
		UrlItem response= urlRepository.save(u);
		UrlItem response2= urlRepository.save(x);
		return response;
	}
	
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
}
