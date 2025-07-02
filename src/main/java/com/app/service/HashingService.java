package com.app.service;


import org.springframework.stereotype.Service;

@Service
public class HashingService {
	
	public String generateShortUrl(long id,String originalUrl) {
		
		String shortUrl = encodeUrlToBase58(id);
		return shortUrl;
	}
	
	//I chose base  58 encoding because users reading a shortened URL might have confusion when using the i,l,o,0,+,- characters
	public String encodeUrlToBase58(long decimal) {
		   String characters = "123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";
		   int base = characters.length();
		   String result = "";
		   
		   while(decimal>0) {
			   result = characters.charAt((int) (decimal % base)) + result;
			   decimal = decimal / base;
		   }
		return result;
	}
}
	
