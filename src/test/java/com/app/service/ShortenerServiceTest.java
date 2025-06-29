package com.app.service;

import static org.mockito.Mockito.when;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.repository.UrlData;
import com.app.repository.UrlDataRepository;
import com.app.validation.ExpiredUrlException;

import jakarta.validation.ValidationException;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShortenerServiceTest {

	@Mock
	UrlDataRepository urlItemRepository;

	@InjectMocks
	ShortenerService shortenerService;

	String apiKey = "person";
	String shortUrl = "abc123test";
	LocalDate expiryDateDefault = LocalDate.now().plusYears(2);

	@Test
	void testCreateUrlAlreadyPresent() {

		String apiKey = "person";
		String originalUrl = "google.com";
		String customAlias = "abc123test";

		UrlData urlResponse = UrlData.builder().id(generateId()).originalUrl(originalUrl).shortUrl(customAlias)
				.expiryDate(expiryDateDefault).build();

		when(urlItemRepository.findItemByOriginalUrl(Mockito.any())).thenReturn(Optional.of(urlResponse));

		Assertions.assertThrows(ValidationException.class,
				() -> shortenerService.createURL(apiKey, originalUrl, customAlias, expiryDateDefault));

	}

	@Test
	void testCreateUrlSuccess() {

		String apiKey = "person";
		String originalUrl = "google.com";
		String customAlias = "abc123test";

		UrlData expected = UrlData.builder().id(generateId()).originalUrl(originalUrl).shortUrl(customAlias)
				.expiryDate(expiryDateDefault).build();

		when(urlItemRepository.findItemByOriginalUrl(Mockito.any())).thenReturn(Optional.empty());

		when(urlItemRepository.save(Mockito.any())).thenReturn(expected);

		UrlData actual = shortenerService.createURL(apiKey, originalUrl, customAlias, expiryDateDefault);

		Assertions.assertTrue(actual != null);
		Assertions.assertEquals(expected.getOriginalUrl(), actual.getOriginalUrl());
		Assertions.assertEquals(expected.getExpiryDate(), actual.getExpiryDate());
		Assertions.assertEquals(expected.getShortUrl(), actual.getShortUrl());

	}

	@Test
	void testSuccessfullyDeletedUrl() {

		when(urlItemRepository.deleteByShortUrl(shortUrl)).thenReturn(1);

		int result = shortenerService.deleteUrl(apiKey, shortUrl);

		Assertions.assertEquals(1, result);

	}

	@Test
	void testDidNotDeletedUrl() {

		when(urlItemRepository.deleteByShortUrl(shortUrl)).thenReturn(0);

		int result = shortenerService.deleteUrl(apiKey, shortUrl);

		Assertions.assertEquals(0, result);

	}

	@Test
	void testRedirectUrlNotFoundError() {

		when(urlItemRepository.findItemByShortUrl(Mockito.any())).thenReturn(Optional.empty());

		Assertions.assertThrows(RuntimeException.class, () -> shortenerService.redirectUrl(apiKey, shortUrl));
	}

	@Test
	void testRedirectUrlExpiredError() {
		String apiKey = "person";
		String originalUrl = "google.com";
		String customAlias = "abc123test";
		UrlData expected = UrlData.builder().id(generateId()).originalUrl(originalUrl).shortUrl(customAlias)
				.expiryDate(LocalDate.now().minusDays(5)).build();

		when(urlItemRepository.findItemByShortUrl(Mockito.any())).thenReturn(Optional.of(expected));

		Assertions.assertThrows(ExpiredUrlException.class, () -> shortenerService.redirectUrl(apiKey, shortUrl));
	}

	@Test
	void testRedirectUrlSuccess() {

		String apiKey = "person";
		String originalUrl = "google.com";
		String customAlias = "abc123test";

		UrlData expected = UrlData.builder().id(generateId()).originalUrl(originalUrl).shortUrl(customAlias)
				.expiryDate(expiryDateDefault).build();

		when(urlItemRepository.findItemByShortUrl(Mockito.any())).thenReturn(Optional.of(expected));

		String actual = shortenerService.redirectUrl(apiKey, shortUrl);

		Assertions.assertTrue(actual != null);
		Assertions.assertEquals(expected.getOriginalUrl(), actual);

	}

	public long generateId() {
		SecureRandom secureRandom = new SecureRandom();
		long id = Math.abs(secureRandom.nextLong());
		return id;
	}

}
