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

import com.crud.challenger.dto.RegisteredUser;
import com.crud.challenger.dto.UserDTO;
import com.crud.challenger.dto.UserError;
import com.crud.challenger.exception.UserNotFoundException;
import com.crud.challenger.persistence.entities.User;
import com.crud.challenger.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Endpoints para la getsión de usuarios")
@Slf4j
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping(produces = "application/json")
	@Operation(summary = "createUser", description = "Crea un nuevo usuario en el sistema")
	public ResponseEntity<RegisteredUser> createUser(@Valid @RequestBody UserDTO saveUser) {
		//User createdUser = userService.createUser(saveUser);
		RegisteredUser registeredUser = userService.registerUser(saveUser);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}	
	
	@PutMapping(produces = "application/json")
	@Operation(summary = "updateUser", description = "Actualiza un usuario existente en el sistema")
	public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO saveUser, 
													 @PathVariable("user_uuid") UUID userUuid) {
		
		log.info("====>Updating user: {}", userUuid);
		RegisteredUser updatedUser;
		UserError userError = new UserError();
		try {
			updatedUser = userService.updateUser(saveUser, userUuid);
		} catch (UserNotFoundException e) {
			log.error("Error disabling user: {}", e.getMessage());
			userError.setMessage(String.valueOf(HttpStatus.NOT_FOUND.value()));
			userError.setDescription("User not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userError);	
		}
		return new ResponseEntity<Object>(updatedUser, HttpStatus.OK);
	}
	
	@PutMapping(value ="/user_uuid/disabled", produces = "application/json")
	@Operation(summary = "disabledUser", description = "Desactiva un usuario existente en el sistema")
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
		userError.setDescription("User disabled successfully " + disableUser.getUserUuid());
		return ResponseEntity.ok(userError);
	}
	
	
	
	

}
