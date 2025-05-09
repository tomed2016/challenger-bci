/**
 * 
 */
package com.crud.challenger.service.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.crud.challenger.dto.RegisteredUser;
import com.crud.challenger.dto.SaveUser;
import com.crud.challenger.persistence.entities.User;
import com.crud.challenger.service.UserService;

/**
 * 
 */
@Service
public class AuthService {
	
	private UserService userService;
	private JwtService jwtService;
	
	public AuthService(UserService userService, JwtService jwtService) {
		this.userService = userService;
		this.jwtService = jwtService;
	}
	
	public RegisteredUser registerUser(SaveUser saveUser) {
		User user = userService.createUser(saveUser);
		RegisteredUser registeredUser = new RegisteredUser();
		registeredUser.setName(user.getName());
		registeredUser.setUserName(user.getUsername());
		registeredUser.setJwtToken(jwtService.generateToken(user.getUsername(), extractAllClaims(user)));
		
		return registeredUser;
	}
	
	private Map<String, Object> extractAllClaims(User user) {
		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("name", user.getName());
		extraClaims.put("role", "user");
		extraClaims.put("authorities", user.getAuthorities());
		return extraClaims;
	}
	

}
