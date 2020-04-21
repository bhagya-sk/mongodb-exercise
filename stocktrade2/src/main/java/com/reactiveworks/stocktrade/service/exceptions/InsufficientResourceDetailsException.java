package com.reactiveworks.stocktrade.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InsufficientResourceDetailsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientResourceDetailsException() {
		super();
	}

	public InsufficientResourceDetailsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InsufficientResourceDetailsException(String message, Throwable cause) {
		super(message, cause);
	}

	public InsufficientResourceDetailsException(String message) {
		super(message);
	}

	public InsufficientResourceDetailsException(Throwable cause) {
		super(cause);
	}

}
