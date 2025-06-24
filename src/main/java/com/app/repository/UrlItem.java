package com.app.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "urlItems")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UrlItem {

	@Id
	private Long id;
	
	private String originalUrl;
	
	private String shortUrl;
	
	private LocalDate expiryDate;
	
//	public UrlItem(String id, String originalUrl, String shortUrl, int expiryTime) {
//		super();
//		this.id=id;
//		this.originalUrl=originalUrl;
//		this.shortUrl=shortUrl;
//		this.expiryTime=expiryTime;	
//	}
	
	
}
