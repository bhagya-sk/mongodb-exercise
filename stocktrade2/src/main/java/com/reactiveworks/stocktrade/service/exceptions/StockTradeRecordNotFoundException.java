package com.reactiveworks.stocktrade.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class StockTradeRecordNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StockTradeRecordNotFoundException() {
		super();

	}

	public StockTradeRecordNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public StockTradeRecordNotFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	public StockTradeRecordNotFoundException(String message) {
		super(message);

	}

	public StockTradeRecordNotFoundException(Throwable cause) {
		super(cause);
	}

}
