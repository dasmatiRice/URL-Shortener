package com.app.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.security.SecureRandom;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.app.model.CreateResponse;
import com.app.model.DeleteResponse;
import com.app.repository.UrlData;
import com.app.repository.UrlDataRepository;
import com.app.service.ShortenerService;
import com.app.validation.ValidationService;

import jakarta.validation.ValidationException;

@ExtendWith(MockitoExtension.class)
public class ControllerTests {
	
	@Mock
	ShortenerService shortenerService;
	
	@Mock
	ValidationService validationService;
	
	@Mock
	UrlDataRepository urlRepository;
	
	@InjectMocks
	Controller controller;
	
	
	LocalDate expiryDateToday=LocalDate.now();
	LocalDate expiryDateDefault=LocalDate.now().plusYears(2);
	
	String aliasExistsException= "Alias already exists, please try again with a different alias or allow us to create one for you";
	
	String apiDevKey= "Admin";
	String originalUrl="youtube.com";
	
	@Test
	void createUrlNoOptionalFields() {
		
		UrlData createItem= UrlData.builder().id(null).originalUrl(originalUrl).shortUrl("test123").expiryDate(expiryDateDefault).build();
		
		doNothing().when(validationService).validateCreateUrl(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		when(shortenerService.createURL(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
		.thenReturn(createItem);
		
		CreateResponse cr= CreateResponse.builder().
				originalUrl(originalUrl)
				.shortUrl("test123")
				.code(HttpStatus.OK)
				.expiration(expiryDateDefault)
				.response("succeeded")
				.build();
		
		ResponseEntity<CreateResponse> expected = new ResponseEntity<>(cr,cr.getCode());

		ResponseEntity<CreateResponse> actual= controller.createUrl(apiDevKey, originalUrl, null, null);
		
		validateCreateUrl(expected, actual);			
	}
	
	@Test
	void createUrlWithCustomAliasNoException() {
		String customAlias= "test456";
		
		UrlData createItem= UrlData.builder().id(null).originalUrl(originalUrl).shortUrl(customAlias).expiryDate(expiryDateDefault).build();
		
		doNothing().when(validationService).validateCreateUrl(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		when(shortenerService.createURL(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
		.thenReturn(createItem);
		
		CreateResponse cr= CreateResponse.builder().
				originalUrl(originalUrl)
				.shortUrl(customAlias)
				.code(HttpStatus.OK)
				.expiration(expiryDateDefault)
				.response("succeeded")
				.build();
		
		ResponseEntity<CreateResponse> expected = new ResponseEntity<>(cr,cr.getCode());
		ResponseEntity<CreateResponse> actual= controller.createUrl(apiDevKey, originalUrl, null, null);
		
		validateCreateUrl(expected, actual);
					
	}
	
	@Test
	void createUrlWithCustomAliasDuplicateException() {
		String customAlias= "test456";
		
		doNothing().when(validationService).validateCreateUrl(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		when(shortenerService.createURL(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenThrow(new ValidationException("aliasExistsException"));
		
		Assertions.assertThrows(ValidationException.class, ()-> controller.createUrl(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()));
							
	}
	
	@Test
	void createUrlCustomExpirationDate() {
		
		LocalDate customExpiryDate=LocalDate.now().plusYears(1);
	
		UrlData createItem= UrlData.builder().id(null).originalUrl(originalUrl).shortUrl("test123").expiryDate(customExpiryDate).build();
		
		doNothing().when(validationService).validateCreateUrl(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		when(shortenerService.createURL(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
		.thenReturn(createItem);
		
		CreateResponse cr= CreateResponse.builder().
				originalUrl(originalUrl)
				.shortUrl("test123")
				.code(HttpStatus.OK)
				.expiration(customExpiryDate)
				.response("succeeded")
				.build();
		
		ResponseEntity<CreateResponse> expected = new ResponseEntity<>(cr,cr.getCode());

		ResponseEntity<CreateResponse> actual= controller.createUrl(apiDevKey, originalUrl, null, null);
		
		validateCreateUrl(expected, actual);
		
	}

	private void validateCreateUrl(ResponseEntity<CreateResponse> expected, ResponseEntity<CreateResponse> actual) {
		Assertions.assertTrue(actual.getBody()!=null);
		Assertions.assertEquals(expected.getStatusCode(),actual.getStatusCode());
		Assertions.assertEquals(expected.getBody().getOriginalUrl(),actual.getBody().getOriginalUrl());
		Assertions.assertEquals(expected.getBody().getExpiration(),actual.getBody().getExpiration());
		Assertions.assertEquals(expected.getBody().getShortUrl(),actual.getBody().getShortUrl());
	}
	
	
	@Test
	void deleteUrlNothingDeleted() {
		
//		doNothing().when(validationService).validateCreateUrl(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		when(shortenerService.deleteUrl(Mockito.any(),Mockito.any()))
		.thenReturn(0);
			
		DeleteResponse dr =  DeleteResponse.builder()
				.shortUrl("test123")
				.code(HttpStatus.BAD_REQUEST)
				.status("fail")
				.response("Failed to Delete")
				.build();
			
		ResponseEntity<DeleteResponse> expected = new ResponseEntity<>(dr,dr.getCode());
		
		ResponseEntity<DeleteResponse> actual= controller.deleteUrl(apiDevKey, originalUrl);
		
		Assertions.assertTrue(actual.getBody()!=null);
		Assertions.assertEquals(expected.getStatusCode(),actual.getStatusCode());
		Assertions.assertEquals(expected.getBody().getCode(),actual.getBody().getCode());
		Assertions.assertEquals(expected.getBody().getStatus(),actual.getBody().getStatus());
		Assertions.assertEquals(expected.getBody().getResponse(),actual.getBody().getResponse());

	}
	
	@Test
	void deleteUrlSuccess() {
		
//		doNothing().when(validationService).validateCreateUrl(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		when(shortenerService.deleteUrl(Mockito.any(),Mockito.any()))
		.thenReturn(1);
				
		DeleteResponse dr =  DeleteResponse.builder()
				.shortUrl("test123")
				.code(HttpStatus.OK)
				.status("success")
				.response("Deleted")
				.build();
				
		ResponseEntity<DeleteResponse> expected = new ResponseEntity<>(dr,dr.getCode());
		
		ResponseEntity<DeleteResponse> actual= controller.deleteUrl(apiDevKey, originalUrl);
		
		Assertions.assertTrue(actual.getBody()!=null);
		Assertions.assertEquals(expected.getStatusCode(),actual.getStatusCode());
		Assertions.assertEquals(expected.getBody().getCode(),actual.getBody().getCode());
		Assertions.assertEquals(expected.getBody().getStatus(),actual.getBody().getStatus());
		Assertions.assertEquals(expected.getBody().getResponse(),actual.getBody().getResponse());
	
	}
	

}
