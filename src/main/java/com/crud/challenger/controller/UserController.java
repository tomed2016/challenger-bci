/**
 * 
 */
package com.crud.challenger.controller;


import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.challenger.dto.SaveUser;
import com.crud.challenger.dto.UserError;
import com.crud.challenger.exception.UserNotFoundException;
import com.crud.challenger.persistence.entities.User;
import com.crud.challenger.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	private UserService userService;
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping(produces = "application/json")
	public ResponseEntity<User> createUser(@Valid @RequestBody SaveUser saveUser) {
		User createdUser = userService.createUser(saveUser);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}	
	
	@PutMapping(produces = "application/json")
	public ResponseEntity<User> updateUser(@Valid @RequestBody SaveUser saveUser) {
		User updatedUser = userService.updateUser(saveUser);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
	
	@PutMapping(value ="/user_uuid/disabled", produces = "application/json")
	public ResponseEntity<UserError> disabledUser(@PathVariable("user_uuid") UUID userUuid) {
		User disableUser = null;
		UserError userError = new UserError();
		try {
			disableUser = userService.disableUser(userUuid);
		} catch (UserNotFoundException e) {
			log.error("Error disabling user: {}", e.getMessage());
			userError.setMessage(String.valueOf(HttpStatus.NOT_FOUND.value()));
			userError.setDescription("User not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userError);	
		}
		userError.setMessage(String.valueOf(HttpStatus.OK.value()));
		userError.setDescription("User disabled successfully " + disableUser.getUuid());
		return ResponseEntity.ok(userError);
	}
	
	
	
	

}
