package com.app.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.app.Repository.UrlItem;
import com.app.Repository.UrlItemRepository;

@Service
public class ShortenerService {
	
	@Autowired
	UrlItemRepository urlItemRepository;
	
	@Autowired
	HashingService hashingService;
	
	final int defaultExpiryTime= 5000; 
	
	public String createURL(String apiDevKey, String originalUrl, String customAlias) {
		
		Optional<UrlItem> exists = urlItemRepository.findItemByOriginalUrl(originalUrl);
		if(exists.isPresent()) {
			throw new RuntimeException("URL already exists");
		}
		

		UrlItem newItem = createUrlItem(originalUrl, customAlias);	
		
		urlItemRepository.save(newItem);
		
		return newItem.getShortUrl();
	}

	private UrlItem createUrlItem(String originalUrl, String customAlias) {
		SecureRandom secureRandom = new SecureRandom();
		long id = Math.abs(secureRandom.nextLong());
		
		//hash the URL and make a custom url
		UrlItem newItem=  UrlItem.builder()
				.id(id)
				.originalUrl(originalUrl)
				.expiryTime(defaultExpiryTime)
				.build();
		
		if(!Strings.isBlank(customAlias)){
			boolean isValidAlias= validateAlias(customAlias);
			if (isValidAlias) {
				newItem.setShortUrl(customAlias);
			}
		}else {
			String shortUrl=hashingService.generateShortUrl(id,originalUrl);
			newItem.setShortUrl(shortUrl);
		}
		
		return newItem;
	}
	
	private Boolean validateAlias(String customAlias) {
		
		if (!Strings.isBlank(customAlias)) {
			Optional<UrlItem> itemByShortUrl = urlItemRepository.findItemByShortUrl(customAlias);
			if (itemByShortUrl.isPresent()) {
				throw new RuntimeException("Alias already exists, please try again with a different alias or allow us to create one for you");
			}
		}
		
		return true;
	}

	public String redirectUrl(String apiDevKey, String shortUrl) {
		
//		UUID test=UUID.fromString("68504fdbc8a4668910dd462b");
		
		List<UrlItem> response= urlItemRepository.findAll();
		
		if (!response.isEmpty()){
			return response.get(0).getShortUrl();
		}
		
		return "empty";
	}

}
