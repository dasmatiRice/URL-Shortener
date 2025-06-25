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
public class UrlData {

	@Id
	private Long id;
	
	private String originalUrl;
	
	private String shortUrl;
	
	private LocalDate expiryDate;
	
}
