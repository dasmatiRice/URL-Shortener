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
import com.app.model.DeleteResponse;
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
	public ResponseEntity<CreateResponse> createUrl(
			@Valid @NotBlank @RequestParam String apiDevKey,
			@Valid @NotBlank @RequestParam String originalUrl,
			@RequestParam(required = false)  String customAlias) {


		// call validator here to validate data fields
		validationService.validateCreate(apiDevKey,originalUrl,customAlias);

		String newShortUrl= shortenerService.createURL(apiDevKey, originalUrl,customAlias);

		CreateResponse response = new CreateResponse().builder().
				originalUrl(originalUrl)
				.shortUrl(newShortUrl)
				.code(HttpStatus.OK)
				.response("succeeded")
				.build();
		
		
		return new ResponseEntity<>(response, response.getCode());

	}

	@GetMapping("/delete")
	public ResponseEntity<DeleteResponse> deleteUrl(
			@Valid @NotBlank @RequestParam String apiDevKey,
			@Valid @NotBlank @RequestParam String urlKey) {
		
		int count= shortenerService.deleteURL(apiDevKey, urlKey);

		DeleteResponse response = generateDeleteResponse(urlKey,count);
		
		return new ResponseEntity<>(response, response.getCode());


	}

	private DeleteResponse generateDeleteResponse(String urlKey,int count) {
		
		String result= count==0 ? "Failed to Delete":"Deleted";
		HttpStatus code= count ==0 ? HttpStatus.BAD_REQUEST: HttpStatus.OK;
		String status= code==HttpStatus.OK ? "success" : "fail";
		
		DeleteResponse response = new DeleteResponse().builder()
				.shortUrl(urlKey)
				.code(code)
				.status(status)
				.response(result)
				.build();
		return response;
	}

	@GetMapping("/redirectUrl")
	public ResponseEntity<String> redirectUrl(@Valid @NotBlank @RequestParam String apiDevKey,
			@Valid @NotBlank @RequestParam String shortUrl) {

		// call validator here to validate data fields
		validationService.validate(apiDevKey,shortUrl);	
	
		String redirectUrlPath = shortenerService.redirectUrl(apiDevKey, shortUrl);

		return new ResponseEntity<>("Redirecting to URL: " + redirectUrlPath, HttpStatus.OK);

	}

	@GetMapping("/GetAll")
	public List<UrlItem> GetAll() {
		
		List<UrlItem> response= urlRepository.findAll();
		return response;
	}
	
	
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
}
