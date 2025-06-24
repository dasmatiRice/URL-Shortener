package com.app.model;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateResponse {
	
	private HttpStatus code;
	
	private String originalUrl;
	
	private String shortUrl;
	
	private LocalDate expiration;

	private String response;

}
