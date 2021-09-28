package com.api.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TradeValidationHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		Map<String, String> errors = new HashMap<String, String>();
		
		ex.getBindingResult().getAllErrors().forEach((error)->{
			FieldError fieldError = ( FieldError )error;
			String fieldString = fieldError.getField();
			String message = error.getDefaultMessage();
			errors.put(fieldString, message);
		});
		System.out.print(errors);
		errors.put("status", "failed");
		return new ResponseEntity<Object>(errors,HttpStatus.BAD_REQUEST);
	}
}
