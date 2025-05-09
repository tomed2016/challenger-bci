package com.crud.challenger.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.crud.challenger.dto.RegisteredUser;
import com.crud.challenger.dto.UserDTO;
import com.crud.challenger.dto.UserError;
import com.crud.challenger.exception.UserNotFoundException;
import com.crud.challenger.persistence.entities.User;
import com.crud.challenger.service.UserService;

class UserControllerTest {

    @Mock
    private UserService userService;
    

    @InjectMocks
    private UserController userController;
    

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserDTO saveUser = new UserDTO();
        RegisteredUser registeredUser = new RegisteredUser();
        when(userService.registerUser(saveUser)).thenReturn(registeredUser);

        ResponseEntity<RegisteredUser> response = userController.createUser(saveUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(registeredUser, response.getBody());
        verify(userService, times(1)).registerUser(saveUser);
    }

	/*
	 * @Test void testUpdateUser() { UserDTO saveUser = new UserDTO();
	 * RegisteredUser registeredUser = new RegisteredUser();
	 * when(userService.updateUser(saveUser)).thenReturn(registeredUser);
	 * 
	 * ResponseEntity<RegisteredUser> response =
	 * userController.updateUser(saveUser);
	 * 
	 * assertEquals(HttpStatus.OK, response.getStatusCode());
	 * assertEquals(registeredUser, response.getBody()); verify(userService,
	 * times(1)).updateUser(saveUser); }
	 */
    @Test()
    void testDisabledUser_Success() throws UserNotFoundException {
        UUID userUuid = UUID.randomUUID();
        User user = new User();
        user.setUserUuid(userUuid);
        when(userService.disableUser(userUuid)).thenReturn(user);

        ResponseEntity<UserError> response = userController.disabledUser(userUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("200", response.getBody().getMessage());
        assertTrue(response.getBody().getDescription().contains("User disabled successfully"));
        verify(userService, times(1)).disableUser(userUuid);
    }

    @Test
    void testDisabledUser_UserNotFound() throws UserNotFoundException {
        UUID userUuid = UUID.randomUUID();
        when(userService.disableUser(userUuid)).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<UserError> response = userController.disabledUser(userUuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("404", response.getBody().getMessage());
        assertEquals("User not found", response.getBody().getDescription());
        verify(userService, times(1)).disableUser(userUuid);
    }
}