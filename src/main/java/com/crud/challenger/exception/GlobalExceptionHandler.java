/**
 * 
 */
package com.crud.challenger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.crud.challenger.dto.UserError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
														  HttpServletRequest request) {
		UserError error = new UserError();
		error.setMessage(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		error.setDescription("Error en la petición enviada " + ex.getAllErrors().stream().map(each -> each.getDefaultMessage() + " - ").reduce("", String::concat) + " - " + ex.getMessage());
		
		log.error("Error en la petición enviada: " + error.getDescription() + " - " + request.getRequestURL().toString() + " - " + ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
		UserError error = new UserError();
		error.setMessage(String.valueOf(HttpStatus.NOT_FOUND.value()));
		error.setDescription("Usuario no encontrado: " + ex.getMessage() + " - " + request.getRequestURL().toString());
		log.error("Usuario no encontrado: {}", ex.toString());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(EmailAlreadyExistException.class)
	public ResponseEntity<?> handleEmailAlreadyExistException(EmailAlreadyExistException ex, HttpServletRequest request) {
		UserError error = new UserError();
		error.setMessage(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		error.setDescription("El email ya existe: " + ex.getMessage() + " - " + request.getRequestURL().toString());
		log.error("El email ya existe: {}", ex.toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<?> handleInvalidPasswordException(InvalidPasswordException ex, HttpServletRequest request) {
		UserError error = new UserError();
		error.setMessage(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		error.setDescription("La contraseña no cumple con los requisitos: " + ex.getMessage() + " - " + request.getRequestURL().toString());
		log.error("La contraseña no cumple con los requisitos: {}", ex.toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleAllExceptions(Exception ex, HttpServletRequest request) {
		UserError error = new UserError();
		error.setMessage(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
		error.setDescription("Error Interno: " + ex.getMessage() + " - " + request.getRequestURL().toString());
		log.error( "Error Interno: {}", ex.toString());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
	
	

}
