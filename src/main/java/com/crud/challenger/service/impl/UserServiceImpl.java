/**
 * 
 */
package com.crud.challenger.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.crud.challenger.dto.SaveUser;
import com.crud.challenger.exception.UserNotFoundException;
import com.crud.challenger.persistence.entities.Phone;
import com.crud.challenger.persistence.entities.User;
import com.crud.challenger.persistence.repositories.UserRepository;
import com.crud.challenger.service.UserService;

import jakarta.validation.Valid;

/**
 * 
 */
@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public User createUser(@Valid SaveUser saveUser) {
		User user = saveUserTOuser(saveUser);
		userRepository.save(user);
		return null;
	}
	
	

	@Override
	public User updateUser(@Valid SaveUser saveUser) {
		User user = saveUserTOuser(saveUser);
		userRepository.save(user);
		return null;
	}

	@Override
	public User disableUser(UUID userUuid) throws UserNotFoundException {
		User user = userRepository.findById(userUuid).orElse(null);
		if (user != null) {
			user.setActive(User.UserStatus.INACTIVE);
			userRepository.save(user);
			return user;
		}
		throw new UserNotFoundException("User not found");
	}
	
	private User saveUserTOuser(SaveUser saveUser) {
		User user = new User();
		user.setName(saveUser.getName());
		user.setEmail(saveUser.getEmail());
		user.setPassword(saveUser.getPassword());
		
		saveUser.getPhones().forEach(phone -> {
			Phone phoneEntity = new Phone();
			phoneEntity.setNumber(phone.getNumber());
			phoneEntity.setCityCode(phone.getCityCode());
			phoneEntity.setContryCode(phone.getCountryCode());	
			user.getPhones().add(phoneEntity);
		});
		return user;
	}

}
