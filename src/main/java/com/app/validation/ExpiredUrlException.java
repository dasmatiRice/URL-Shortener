package com.app.validation;

public class ExpiredUrlException extends RuntimeException {

	public ExpiredUrlException (String errorMessage) {
		super(errorMessage);
	}
}
