package com.example.fitfolio;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fitfolio.component.JwtUtil;
import com.example.fitfolio.controllers.UserController;
import com.example.fitfolio.dto.LoginDto;
import com.example.fitfolio.dto.UserDto;
import com.example.fitfolio.entities.Role;
import com.example.fitfolio.entities.Utilisateur;
import com.example.fitfolio.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    void getUsers_Success() {
        List<Utilisateur> users = Arrays.asList(new Utilisateur());
        when(userService.listUsers()).thenReturn(users);

        List<Utilisateur> result = userController.GetUsers();
        assertEquals(users.size(), result.size());
    }

    @Test
    void login_Success() {
        UserDto userDto = new UserDto();
        when(userService.login("test@test.com", "password"))
                .thenReturn(ResponseEntity.ok(userDto));

        ResponseEntity<UserDto> response = userController.login("test@test.com", "password");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void saveUser_Success() {
        Utilisateur user = new Utilisateur();
        UserDto userDto = new UserDto();
        when(userService.addNewUser(user)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.saveUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void saveRole_Success() {
        Role role = new Role();
        when(userService.addNewRole(role)).thenReturn(role);

        Role result = userController.saveRole(role);
        assertNotNull(result);
    }

    @Test
    void addRoleToUser_Success() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUser("user");
        loginDto.setRole("ROLE_USER");

        userController.addRoleToUser(loginDto);
        verify(userService).addRoleToUser(loginDto.getUser(), loginDto.getRole());
    }

    @Test
    void refreshToken_NullAuthToken() {
        when(request.getHeader("Authorization")).thenReturn(null);

        userController.refresh(request, response);
        verify(response, never()).setContentType(any());
    }


    @Test
    void getUserBMI_Success() {
        Long userId = 1L;
        Double expectedBMI = 22.5;
        when(userService.calculateBMI(userId)).thenReturn(expectedBMI);

        ResponseEntity<Map<String, Object>> response = userController.getUserBMI(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBMI, response.getBody().get("bmi"));
    }

    @Test
    void getUserBMI_UserNotFound() {
        Long userId = 1L;
        when(userService.calculateBMI(userId))
                .thenThrow(new RuntimeException("User not found"));

        ResponseEntity<Map<String, Object>> response = userController.getUserBMI(userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
    }

    @Test
    void login_InvalidCredentials() {
        when(userService.login("invalid@test.com", "wrong"))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

        ResponseEntity<UserDto> response = userController.login("invalid@test.com", "wrong");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


}