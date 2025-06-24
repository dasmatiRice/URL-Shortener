package com.app.service;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.repository.UrlItem;
import com.app.repository.UrlItemRepository;

import jakarta.validation.ValidationException;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShortenerServiceTest {
	
	
	@Mock
	UrlItemRepository urlItemRepository;
	
	@InjectMocks
	ShortenerService shortenerService;

	String apiKey="person";
	String shortUrl="abc123test";
	
	
	
	@Test
	void testCreateUrlAlreadyPresent(){
		
		String apiKey="person";
		String originalUrl="google.com";
		String customAlias="abc123test";
		LocalDate expiryDate=LocalDate.now();
		
		UrlItem urlResponse= UrlItem.builder().id((long) 123344).shortUrl(customAlias).expiryDate(expiryDate).build();
		
		Optional<UrlItem> present = Optional.of(urlResponse);
		
		System.out.println(present.isPresent());
		
		when(urlItemRepository.findItemByOriginalUrl(Mockito.any())).thenReturn(present);
		
		Assertions.assertThrows(ValidationException.class, ()->shortenerService.createURL(apiKey, originalUrl, customAlias, expiryDate));
		
		
		
	
	
	}
	
	@Test
	void testSuccessfullyDeletedUrl(){

		when(urlItemRepository.deleteByShortUrl(shortUrl)).thenReturn(1);
		
		int result=shortenerService.deleteURL(apiKey, shortUrl);
		
		Assertions.assertEquals(1, result);
		
	}
	
	@Test
	void testDidNotDeletedUrl(){
		
		when(urlItemRepository.deleteByShortUrl(shortUrl)).thenReturn(0);
		
		int result=shortenerService.deleteURL(apiKey, shortUrl);
		
		Assertions.assertEquals(0, result);
		
	}
	

}
