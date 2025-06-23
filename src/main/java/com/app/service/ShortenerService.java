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

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;

@Service
public class ShortenerService {

	@Autowired
	UrlItemRepository urlItemRepository;

	@Autowired
	HashingService hashingService;

	final int defaultExpiryTime = 5000;

	public String createURL(String apiDevKey, String originalUrl, String customAlias) {

		Optional<UrlItem> existingUrl = urlItemRepository.findItemByOriginalUrl(originalUrl);
		if (existingUrl.isPresent()) {
			throw new ValidationException("URL already exists");
		}

		SecureRandom secureRandom = new SecureRandom();
		long id = Math.abs(secureRandom.nextLong());

		// hash the URL and make a custom url
		UrlItem newItem = UrlItem.builder().id(id).originalUrl(originalUrl).expiryTime(defaultExpiryTime).build();

		if (!Strings.isBlank(customAlias)) {
			boolean isValid = isValidAlias(customAlias);
			if (isValid) {
				newItem.setShortUrl(customAlias);
			}
		} else {
			String shortUrl = hashingService.generateShortUrl(id, originalUrl);
			newItem.setShortUrl(shortUrl);
		}

		System.out.println("new short url is= " + newItem.getShortUrl());
		urlItemRepository.save(newItem);

		return newItem.getShortUrl();
	}

	private Boolean isValidAlias(String customAlias) {

		if (!Strings.isBlank(customAlias)) {
			Optional<UrlItem> itemByShortUrl = urlItemRepository.findItemByShortUrl(customAlias);
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

//		UUID test=UUID.fromString("68504fdbc8a4668910dd462b");

		Optional<UrlItem> response = urlItemRepository.findItemByShortUrl(shortUrl);

		if (response.isEmpty()) {
			throw new RuntimeException("Shortened Url could not redirect or does not exist. Please try creating again");
		}

		return response.get().getOriginalUrl();
	}

	public int deleteURL(String apiDevKey, String urlKey) {

		int countDeletedItems = urlItemRepository.deleteByShortUrl(urlKey);

		return countDeletedItems;
	}

}
