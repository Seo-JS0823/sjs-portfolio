package com.jubu.pofol.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jubu.pofol.domain.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class Advice {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Map<String, String>> handleCustomException(CustomException e) {
		Map<String, String> response = Map.of(
				"message", e.getMessage()
		);
		
		return ResponseEntity.badRequest().body(response);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleException(Exception e) {
		Map<String, String> response = Map.of(
				"message", "처리되지 않은 예외 발생"
		);
		
		log.error("처리되지 않은 예외 발생", e);
		
		return ResponseEntity.badRequest().body(response);
	}
}
