package com.app.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateResponse {
	
	private String name;

	private int ID;

	private String response;

}
