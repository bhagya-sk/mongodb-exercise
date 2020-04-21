package com.reactiveworks.stocktrade.service.exceptions.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.reactiveworks.stocktrade.service.exceptions.InsufficientResourceDetailsException;
import com.reactiveworks.stocktrade.service.exceptions.StockTradeRecordNotFoundException;
import com.reactiveworks.stocktrade.service.exceptions.StockTrdRecordAlreadyExistsException;
import com.reactiveworks.stocktrade.service.exceptions.response.ErrorResponse;

/**
 * handler class for the eceptions.
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Exception handler method for StockTradeRecordNotFoundException.
	 * 
	 * @param exp StockTradeRecordNotFoundException.
	 * @return the response for the handled exception.
	 */
	@ExceptionHandler(StockTradeRecordNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleStockTradeRecordNotFoundException(StockTradeRecordNotFoundException exp) {
		List<String> details = new ArrayList<>();
		details.add(exp.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse("Record(s) Not Found", details);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Exception handler method for InsufficientResourceDetailsException.
	 * 
	 * @param exp InsufficientResourceDetailsException.
	 * @return the response for the handled exception.
	 */
	@ExceptionHandler(InsufficientResourceDetailsException.class)
	public final ResponseEntity<ErrorResponse> handleInsufficientResourceDetailsException(InsufficientResourceDetailsException exp) {
		List<String> details = new ArrayList<>();
		details.add(exp.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse("id, security, date, open, high, low, close, volume, adjClose are required fields", details);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Exception handler method for StockTrdRecordAlreadyExistsException.
	 * 
	 * @param exp StockTrdRecordAlreadyExistsException.
	 * @return the response for the handled exception.
	 */
	@ExceptionHandler(StockTrdRecordAlreadyExistsException.class)
	public final ResponseEntity<ErrorResponse> handleStockTrdRecordAlreadyExistsException(StockTrdRecordAlreadyExistsException exp) {
		List<String> details = new ArrayList<>();
		details.add(exp.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse("duplicate record", details);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

}
