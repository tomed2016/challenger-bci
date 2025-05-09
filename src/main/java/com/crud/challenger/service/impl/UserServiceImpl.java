/**
 * 
 */
package com.crud.challenger.service.impl;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.crud.challenger.dto.SaveUser;
import com.crud.challenger.exception.InvalidPasswordException;
import com.crud.challenger.exception.UserNotFoundException;
import com.crud.challenger.persistence.entities.Phone;
import com.crud.challenger.persistence.entities.User;
import com.crud.challenger.persistence.repositories.PhoneRepository;
import com.crud.challenger.persistence.repositories.UserRepository;
import com.crud.challenger.service.UserService;

import jakarta.validation.Valid;

/**
 * @author Tomás González
 * @date 2025-05-08
 * @apiNote Service para la gestión de usuarios. 
 *  */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	
	private PasswordEncoder passwordEncoder;
	
	private UserRepository userRepository;
	
	private PhoneRepository phoneRepository;
	
	public UserServiceImpl(UserRepository userRepository,
							PhoneRepository phoneRepository,	
						   PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public User createUser(@Valid SaveUser saveUser) {
		User user = saveUserTOuser(saveUser);
		validatePassword(saveUser);
		user.setPassword(passwordEncoder.encode(saveUser.getPassword()));
		userRepository.save(user);
		return user;
	}
	
	@Override
	public User updateUser(@Valid SaveUser saveUser) {
		User user = saveUserTOuser(saveUser);
		userRepository.save(user);
		return user;
	}

	@Override
	public User disableUser(UUID userUuid) throws UserNotFoundException {
		User user = userRepository.findById(userUuid).orElse(null);
		if (user != null) {
			user.setActive(User.UserStatus.INACTIVE);
			userRepository.save(user);
			phoneRepository.saveAll(user.getPhones());
			return user;
		}
		throw new UserNotFoundException("User not found");
	}
	
	private User saveUserTOuser(SaveUser saveUser) {
		User user = new User();
		user.setName(saveUser.getName());
		user.setEmail(saveUser.getEmail());
		
		saveUser.getPhones().forEach(phone -> {
			Phone phoneEntity = new Phone();
			phoneEntity.setNumber(phone.getNumber());
			phoneEntity.setCityCode(phone.getCityCode());
			phoneEntity.setContryCode(phone.getCountryCode());	
			user.getPhones().add(phoneEntity);
		});
		return user;
	}

	private void validatePassword(SaveUser user) {
		if (!StringUtils.hasText(user.getPassword()) || !StringUtils.hasText(user.getPasswordConfirm())) {
			throw new InvalidPasswordException("Passwords no coinciden");
		}
		
		if (!user.getPassword().equals(user.getPasswordConfirm())) {
			throw new InvalidPasswordException("Passwords no coinciden");
		}
	}
	
	

}
