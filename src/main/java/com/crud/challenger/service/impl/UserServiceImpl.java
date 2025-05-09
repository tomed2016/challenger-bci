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
import com.crud.challenger.exception.EmailAlreadyExistException;
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
	
	private static final String PASSWORD_REGEX_MSG = "Password no cumple requisitos: Una mayúscula, una minúscula, un número y un carácter especial";

	private static final String PASSWORDS_NOT_MATCH_MSG = "Passwords no coinciden";

	private static final String USER_NOTE_FOUND_MSG = "Usuario no encontrado";

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
	public RegisteredUser registerUser(UserDTO saveUser) throws EmailAlreadyExistException {
		User user = saveUserTOuser(saveUser, new User());
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
	public RegisteredUser updateUser(@Valid UserDTO saveUser, UUID userUuid) throws UserNotFoundException, EmailAlreadyExistException {
		User user = userRepository.findById(userUuid).orElse(null);
		if (user == null) {
			throw new UserNotFoundException(USER_NOTE_FOUND_MSG);
		}
		
		validatePassword(saveUser);
		user = saveUserTOuser(saveUser, user);
		user.setUserUuid(userUuid);
		Date now = Date.from(Instant.now());
		user.setUpdatedAt(now);
		user.setLastLogin(now);
		userRepository.save(user);
		RegisteredUser registeredUser = saveUserTORegisteredUser(user);
		return registeredUser;
	}

	@Override
	public User disableUser(UUID userUuid) throws UserNotFoundException {
		User user = userRepository.findById(userUuid).orElse(null);
		if (user != null) {
			user.setActive(User.UserStatus.INACTIVE);
			Date now = Date.from(Instant.now());
			user.setLastLogin(now);
			user.setUpdatedAt(now);
			userRepository.save(user);
			return user;
		}
		throw new UserNotFoundException(USER_NOTE_FOUND_MSG);
	}
	private Map<String, Object> extractAllClaims(User user) {
		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("name", user.getName());
		extraClaims.put("role", "user");
		extraClaims.put("authorities", user.getAuthorities());
		return extraClaims;
	}
	
	
	private User saveUserTOuser(UserDTO saveUser, User user) throws EmailAlreadyExistException {
		validateEmail(saveUser.getEmail());
		user.setName(saveUser.getName());
		user.setEmail(saveUser.getEmail());
		user.setUserName(saveUser.getUserName());
		user.setActive(User.UserStatus.ACTIVE);
		user.setPassword(saveUser.getPassword());
		user.setJwtToken(saveUser.getJwtToken());

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
			throw new InvalidPasswordException(PASSWORDS_NOT_MATCH_MSG);
		}
		
		if (!user.getPassword().equals(user.getPasswordConfirm())) {
			throw new InvalidPasswordException(PASSWORDS_NOT_MATCH_MSG);
		}
		
		Matcher matcher = Pattern.compile(passwordPattern).matcher(user.getPassword());
		if (!matcher.find()) {
			log.debug("patron password: [{}]", passwordPattern);
			throw new InvalidPasswordException(PASSWORD_REGEX_MSG);
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
		registeredUser.setCreatedAt(user.getCreatedAt());
		registeredUser.setUpdatedAt(user.getUpdatedAt());
		registeredUser.setLastLogin(user.getLastLogin());
		
		return registeredUser;
	}
	
	private void validateEmail(String email) throws EmailAlreadyExistException {
		
		if (userRepository.findByEmail(email).isPresent()) { 
			throw new EmailAlreadyExistException("Email ya existe");
		}
	}
	
	

}
