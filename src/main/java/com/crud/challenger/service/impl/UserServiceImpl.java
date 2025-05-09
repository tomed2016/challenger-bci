/**
 * 
 */
package com.crud.challenger.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.crud.challenger.dto.RegisteredUser;
import com.crud.challenger.dto.UserDTO;
import com.crud.challenger.exception.InvalidPasswordException;
import com.crud.challenger.exception.UserNotFoundException;
import com.crud.challenger.persistence.entities.Phone;
import com.crud.challenger.persistence.entities.User;
import com.crud.challenger.persistence.repositories.UserRepository;
import com.crud.challenger.service.UserService;
import com.crud.challenger.service.auth.JwtService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tomás González
 * @date 2025-05-08
 * @apiNote Service para la gestión de usuarios. 
 *  */
@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

	@Value("${password.pattern}")
	private String passwordPattern;
	private PasswordEncoder passwordEncoder;
	
	private UserRepository userRepository;
	private JwtService jwtService;
	
	
	public UserServiceImpl(UserRepository userRepository,
						   PasswordEncoder passwordEncoder, 
						   JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}
	
	
	@Override
	public RegisteredUser registerUser(UserDTO saveUser) {
		User user = saveUserTOuser(saveUser);
		Date now = Date.from(Instant.now());
		user.setCreatedAt(now);
		user.setLastLogin(now);
		validatePassword(saveUser);
		user.setPassword(passwordEncoder.encode(saveUser.getPassword()));
		userRepository.save(user);
		RegisteredUser registeredUser = saveUserTORegisteredUser(user);
		registeredUser.setCreatedAt(user.getCreatedAt());
		registeredUser.setLastLogin(user.getLastLogin());

		return registeredUser;
	}
	
	@Override
	public RegisteredUser updateUser(@Valid UserDTO saveUser, UUID userUuid) throws UserNotFoundException {
		User user = saveUserTOuser(saveUser);
		Date now = Date.from(Instant.now());
		user.setUpdatedAt(now);
		user.setLastLogin(now);
		RegisteredUser registeredUser = saveUserTORegisteredUser(user);
		userRepository.save(user);
		return registeredUser;
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
	private Map<String, Object> extractAllClaims(User user) {
		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("name", user.getName());
		extraClaims.put("role", "user");
		extraClaims.put("authorities", user.getAuthorities());
		return extraClaims;
	}
	
	
	private User saveUserTOuser(UserDTO saveUser) {
		User user = new User();
		user.setName(saveUser.getName());
		user.setEmail(saveUser.getEmail());
		user.setUserName(saveUser.getUserName());
		user.setActive(User.UserStatus.ACTIVE);

		Phone[] phones = new Phone[saveUser.getPhones().length];
		int i = 0;
		 for (Phone phone : saveUser.getPhones()) {
			 Phone phoneEntity = new Phone();
			 phoneEntity.setNumber(phone.getNumber());
			 phoneEntity.setCityCode(phone.getCityCode());
			 phoneEntity.setCountryCode(phone.getCountryCode());
			 phoneEntity.setUser(user);
			 phones[i] = phoneEntity;
			 i++;
		 }
		user.setPhones(phones);
		return user;
	}

	private void validatePassword(UserDTO user) {
		if (!StringUtils.hasText(user.getPassword()) || !StringUtils.hasText(user.getPasswordConfirm())) {
			throw new InvalidPasswordException("Passwords no coinciden");
		}
		
		if (!user.getPassword().equals(user.getPasswordConfirm())) {
			throw new InvalidPasswordException("Passwords no coinciden");
		}
		
		Matcher matcher = Pattern.compile(passwordPattern).matcher(user.getPassword());
		if (!matcher.find()) {
			log.debug("patron password: [{}]", passwordPattern);
			throw new InvalidPasswordException("Password no cumple requisitos: Una mayúscula, una minúscula, un número y un carácter especial");
		}	
		
	}
	
	private RegisteredUser saveUserTORegisteredUser(User user) {
		RegisteredUser registeredUser = new RegisteredUser();
		registeredUser.setUserUuid(user.getUserUuid());
		registeredUser.setName(user.getName());
		registeredUser.setUserName(user.getUsername());
		registeredUser.setJwtToken(jwtService.generateToken(user.getUsername(), extractAllClaims(user)));
		registeredUser.setLastLogin(user.getLastLogin());
		registeredUser.setIsActive(user.isActive());
		
		return registeredUser;
	}
	
	

}
