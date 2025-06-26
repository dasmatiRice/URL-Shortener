package com.app.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.app.repository.UrlData;
import com.app.repository.UrlDataRepository;
import com.app.validation.ExpiredUrlException;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;

@Service
public class ShortenerService {

	@Autowired
	UrlDataRepository urlItemRepository;

	@Autowired
	HashingService hashingService;

	final LocalDate defaultExpiryTime = LocalDate.now().plusYears(2);

	public UrlData createURL(String apiDevKey, String originalUrl, String customAlias, LocalDate expiryDate) {

		Optional<UrlData> existingUrl = urlItemRepository.findItemByOriginalUrl(originalUrl);
		if (existingUrl.isPresent()) {
			throw new ValidationException("URL already exists");
		}

		SecureRandom secureRandom = new SecureRandom();
		long id = Math.abs(secureRandom.nextLong());

		String shortUrl=determineShortUrl(originalUrl, customAlias, id);	
		LocalDate expiration=determineExpiryDate(expiryDate);
		
		UrlData newItem = UrlData.builder().id(id).originalUrl(originalUrl).shortUrl(shortUrl).expiryDate(expiration).build();
		
		urlItemRepository.save(newItem);
		
		return newItem;
	}

	private LocalDate determineExpiryDate(LocalDate expiryDate) {
		
		if (expiryDate==null) {
			return defaultExpiryTime;
		}else {
			return expiryDate;
		}
		
	}

	private String determineShortUrl(String originalUrl, String customAlias, long id) {
		if (!Strings.isBlank(customAlias)) {
			boolean isValid = isValidAlias(customAlias);
			if (isValid) {
				return customAlias;
			}
		} 
		
		return hashingService.generateShortUrl(id, originalUrl);
		

	}

	private Boolean isValidAlias(String customAlias) {

		if (!Strings.isBlank(customAlias)) {
			Optional<UrlData> itemByShortUrl = urlItemRepository.findItemByShortUrl(customAlias);
			if (itemByShortUrl.isPresent()) {
				throw new ValidationException(
						"Alias already exists, please try again with a different alias or allow us to create one for you");
			} else {
				return true;
			}
		}

		return true;
	}

	public String redirectUrl(String apiDevKey, String shortUrl) {

		Optional<UrlData> response = urlItemRepository.findItemByShortUrl(shortUrl);

		if (response.isEmpty()) {
			throw new RuntimeException("Shortened Url could not redirect or does not exist. Please try creating again");
		}else if(response.get().getExpiryDate().isBefore(LocalDate.now())) {
			
			//Delete the expired item and throw an exception
			urlItemRepository.delete(response.get());
			
			String ex= "The Url you are attempting to hit has expired %s, please create the shortened URL again";
			String exceptionMessage= String.format(ex, response.get().getExpiryDate());
			throw new ExpiredUrlException(exceptionMessage);
		}

		return response.get().getOriginalUrl();
	}

	public int deleteUrl(String apiDevKey, String urlKey) {

		int countDeletedItems = urlItemRepository.deleteByShortUrl(urlKey);

		return countDeletedItems;
	}

}
