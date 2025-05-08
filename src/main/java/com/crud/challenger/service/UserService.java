/**
 * 
 */
package com.crud.challenger.service;



import java.util.UUID;

import com.crud.challenger.dto.SaveUser;
import com.crud.challenger.exception.UserNotFoundException;
import com.crud.challenger.persistence.entities.User;

import jakarta.validation.Valid;

/**
 * 
 */
public interface UserService {

	User createUser(@Valid SaveUser user);

	User updateUser(@Valid SaveUser saveUser);

	User disableUser(UUID userUuid) throws UserNotFoundException;

}
