package com.app.controller;

import java.net.URI;
import java.time.LocalDate;
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

import com.app.model.CreateResponse;
import com.app.model.DeleteResponse;
import com.app.repository.UrlData;
import com.app.repository.UrlDataRepository;
import com.app.service.ShortenerService;
import com.app.validation.ValidationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
public class Controller {

	// The URL shortener should support 3 methods creation, deletion, and redirect for the URLs
	@Autowired
	ShortenerService shortenerService;
	
	@Autowired
	ValidationService validationService;
	
	@Autowired
	UrlDataRepository urlRepository;


	@PostMapping("/create")
	public ResponseEntity<CreateResponse> createUrl(
			@Valid @NotBlank @RequestParam String apiDevKey,
			@Valid @NotBlank @RequestParam String originalUrl,
			@RequestParam(required = false)  String customAlias,
			@RequestParam(required = false)  LocalDate expiryDate) {

		validationService.validateCreateUrl(apiDevKey,originalUrl,customAlias,expiryDate);

		UrlData createdUrl= shortenerService.createURL(apiDevKey, originalUrl,customAlias, expiryDate);

		CreateResponse response = CreateResponse.builder().
				originalUrl(originalUrl)
				.shortUrl(createdUrl.getShortUrl())
				.code(HttpStatus.OK)
				.expiration(createdUrl.getExpiryDate())
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
		
		DeleteResponse response =  DeleteResponse.builder()
				.shortUrl(urlKey)
				.code(code)
				.status(status)
				.response(result)
				.build();
		return response;
	}

	@GetMapping("/redirectUrl")
	public ResponseEntity<Void> redirectUrl(@Valid @NotBlank @RequestParam String apiDevKey,
			@Valid @NotBlank @RequestParam String shortUrl) {

		validationService.validateRedirectUrl(apiDevKey,shortUrl);	
	
		String redirectUrlPath = shortenerService.redirectUrl(apiDevKey, shortUrl);
		
		URI redirectUri = URI.create(redirectUrlPath);
	   
		return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();

	}

	@GetMapping("/getAll")
	public List<UrlData> GetAll() {
		
		List<UrlData> response= urlRepository.findAll();
		return response;
	}
	
	
	@GetMapping("/health")
	public String healthCheck() {
		return "Server is up and running";
	}
}
