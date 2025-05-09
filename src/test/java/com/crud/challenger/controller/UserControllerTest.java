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

import com.crud.challenger.dto.SaveUser;
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
        SaveUser saveUser = new SaveUser();
        User user = new User();
        when(userService.createUser(saveUser)).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(saveUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).createUser(saveUser);
    }

    @Test
    void testUpdateUser() {
        SaveUser saveUser = new SaveUser();
        User user = new User();
        when(userService.updateUser(saveUser)).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(saveUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).updateUser(saveUser);
    }

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