package com.app.controller;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.app.service.GeminiService;
import com.app.service.ShortenerService;
import com.app.validation.ValidationService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
public class Controller {

	// The URL shortener should support 3 methods creation, deletion, and redirect
	// for the URLs
	@Autowired
	ShortenerService shortenerService;

	@Autowired
	ValidationService validationService;

	@Autowired
	UrlDataRepository urlRepository;

	private final Bucket createDeleteBucket;

	private static final Logger log = LoggerFactory.getLogger(Controller.class);
	
	 public Controller() {
	        Bandwidth limit = Bandwidth.classic(50, Refill.greedy(50, Duration.ofMinutes(1)));
	        this.createDeleteBucket = Bucket.builder()
	            .addLimit(limit)
	            .build();
	    }

	@PostMapping("/createUrl")
	public ResponseEntity<CreateResponse> createUrl(@Valid @NotBlank @RequestParam String apiDevKey,
			@Valid @NotBlank @RequestParam String originalUrl, @RequestParam(required = false) String customAlias,
			@RequestParam(required = false) LocalDate expiryDate) {
		
		if(createDeleteBucket.tryConsume(1)) {
			log.info("Calling Create URL with params: " + " apiDevKey= " + " originalUrl= " + " customAlias= "
					+ " expiryDate= ", apiDevKey, originalUrl, customAlias, expiryDate);
			
			validationService.validateCreateUrl(apiDevKey, originalUrl, customAlias, expiryDate);

			UrlData createdUrl = shortenerService.createURL(apiDevKey, originalUrl, customAlias, expiryDate);

			CreateResponse response = CreateResponse.builder().originalUrl(originalUrl).shortUrl(createdUrl.getShortUrl())
					.code(HttpStatus.OK).expiration(createdUrl.getExpiryDate()).response("succeeded").build();

			log.info("Created Url Response: ", response);

			return new ResponseEntity<>(response, response.getCode());
			
		}
		
		 return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();

	}

	@GetMapping("/deleteUrl")
	public ResponseEntity<DeleteResponse> deleteUrl(@Valid @NotBlank @RequestParam String apiDevKey,
			@Valid @NotBlank @RequestParam String urlKey) {

		if(createDeleteBucket.tryConsume(1)) {
			int count = shortenerService.deleteUrl(apiDevKey, urlKey);

			DeleteResponse response = generateDeleteResponse(urlKey, count);

			log.info("Deleting URL Response: ", response);

			return new ResponseEntity<>(response, response.getCode());
		}
		
		 return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
	}

	private DeleteResponse generateDeleteResponse(String urlKey, int count) {

		String result = count == 0 ? "Failed to Delete" : "Deleted";
		HttpStatus code = count == 0 ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		String status = code == HttpStatus.OK ? "success" : "fail";

		DeleteResponse response = DeleteResponse.builder().shortUrl(urlKey).code(code).status(status).response(result)
				.build();

		return response;
	}

	@GetMapping("/redirectUrl")
	public ResponseEntity<Void> redirectUrl(@Valid @NotBlank @RequestParam String apiDevKey,
			@Valid @NotBlank @RequestParam String shortUrl) {

		log.info("Redirecting URL: ", shortUrl);

		validationService.validateRedirectUrl(apiDevKey, shortUrl);

		String redirectUrlPath = shortenerService.redirectUrl(apiDevKey, shortUrl);

		URI redirectUri = URI.create(redirectUrlPath);

		log.info("Redirection URL Response: ", redirectUri);
		return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();

	}

	@GetMapping("/getAll")
	public List<UrlData> GetAll() {

		log.info("Getting All Items");

		List<UrlData> response = urlRepository.findAll();
		return response;
	}

	@GetMapping("/health")
	public String healthCheck() {
		log.info("Running Health Check");
		return "Server is up and running";
	}
	
	@GetMapping("/testAI")
	public String testAI() {
		Client client = new Client();

	    GenerateContentResponse response =
	        client.models.generateContent(
	            "gemini-2.5-flash",
	            "Can you tell me if the following link is malicious or not?: mp3raid.com/music/krizz_kaliko.html",
	            null);

	   return response.text();
	}

}
