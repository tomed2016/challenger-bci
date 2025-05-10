package com.crud.challenger.controller;import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.crud.challenger.exception.EmailAlreadyExistException;
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
    void testCreateUser_Success() throws EmailAlreadyExistException {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setName("Test User");

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUserUuid(UUID.randomUUID());
        registeredUser.setEmail(userDTO.getEmail());

        when(userService.registerUser(any(UserDTO.class))).thenReturn(registeredUser);

        ResponseEntity<Object> response = userController.createUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(registeredUser, response.getBody());
    }

    @Test
    void testCreateUser_EmailAlreadyExists() throws EmailAlreadyExistException {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setName("Test User");

        when(userService.registerUser(any(UserDTO.class))).thenThrow(new EmailAlreadyExistException("Email already exists"));

        ResponseEntity<Object> response = userController.createUser(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserError);
        assertEquals("Email ya existe", ((UserError) response.getBody()).getDescription());
    }

    @Test
    void testUpdateUser_Success() throws UserNotFoundException, EmailAlreadyExistException {
        UUID userUuid = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("updated@example.com");
        userDTO.setName("Updated User");

        RegisteredUser updatedUser = new RegisteredUser();
        updatedUser.setUserUuid(userUuid);
        updatedUser.setEmail(userDTO.getEmail());

        when(userService.updateUser(any(UserDTO.class), eq(userUuid))).thenReturn(updatedUser);

        ResponseEntity<Object> response = userController.updateUser(userDTO, userUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void testUpdateUser_UserNotFound() throws UserNotFoundException, EmailAlreadyExistException {
        UUID userUuid = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("updated@example.com");
        userDTO.setName("Updated User");

        when(userService.updateUser(any(UserDTO.class), eq(userUuid))).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<Object> response = userController.updateUser(userDTO, userUuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserError);
        assertEquals("User not found", ((UserError) response.getBody()).getDescription());
    }

    @Test
    void testDisabledUser_Success() throws UserNotFoundException {
        UUID userUuid = UUID.randomUUID();
        User user = new User();
        user.setUserUuid(userUuid);

        when(userService.disableUser(userUuid)).thenReturn(user);

        ResponseEntity<UserError> response = userController.disabledUser(userUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User disabled successfully " + userUuid, response.getBody().getDescription());
    }

    @Test
    void testDisabledUser_UserNotFound() throws UserNotFoundException {
        UUID userUuid = UUID.randomUUID();

        when(userService.disableUser(userUuid)).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<UserError> response = userController.disabledUser(userUuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody().getDescription());
    }
}
