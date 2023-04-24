package com.store.bookStore.service.exception;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice{
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<CustomErrorResponse>handleNotFoundException(NotFoundException e) {		
		CustomErrorResponse error = new CustomErrorResponse("NOT_FOUND_ERROR", e.getMessage());
		error.setTimestamp(LocalDateTime.now());
		error.setStatus((HttpStatus.NOT_FOUND.value()));
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<CustomErrorResponse>handleDuplicateRequestException(BadRequestException e) {
		CustomErrorResponse error = new CustomErrorResponse("BAD_REQUEST_ERROR", e.getMessage());
		error.setTimestamp(LocalDateTime.now());
		error.setStatus((HttpStatus.BAD_REQUEST.value()));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<CustomErrorResponse>handleRestClientException(RestClientException e) {
		CustomErrorResponse error = new CustomErrorResponse("CONNECTION_ERROR", e.getMessage());
		error.setTimestamp(LocalDateTime.now());
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
