package newusefy.com.internship.controller;

import newusefy.com.internship.dto.UserLoginDto;
import newusefy.com.internship.dto.UserRegistrationDto;
import newusefy.com.internship.model.User;
import newusefy.com.internship.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authController = new AuthController(userService, passwordEncoder);
    }

    @Test
    void testRegisterSuccess() {
        UserRegistrationDto dto = new UserRegistrationDto("alice", "alice@example.com", "password123");
        when(userService.registerUser(dto)).thenReturn(new User("alice@example.com", "encodedPass"));

        ResponseEntity<Map<String, String>> response = authController.register(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().get("message").contains("User registered successfully"));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        UserRegistrationDto dto = new UserRegistrationDto("alice", "alice@example.com", "password123");
        when(userService.registerUser(dto)).thenThrow(new IllegalArgumentException("User already exists"));

        ResponseEntity<Map<String, String>> response;
        try {
            response = authController.register(dto);
        } catch (IllegalArgumentException e) {
            // Симулируем поведение GlobalExceptionHandler
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().get("message").contains("User already exists"));
    }

    @Test
    void testLoginSuccess() {
        UserLoginDto dto = new UserLoginDto("alice@example.com", "password123");
        User user = new User("alice@example.com", "encodedPassword");

        when(userService.findByUsername("alice@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        ResponseEntity<Map<String, String>> response = authController.login(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("message").contains("Login successful"));
    }

    @Test
    void testLoginWrongPassword() {
        UserLoginDto dto = new UserLoginDto("alice@example.com", "wrong");
        User user = new User("alice@example.com", "encodedPassword");

        when(userService.findByUsername("alice@example.com")).thenReturn(user);
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        ResponseEntity<Map<String, String>> response = authController.login(dto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().get("message").contains("Invalid credentials"));
    }

    @Test
    void testLoginUserNotFound() {
        UserLoginDto dto = new UserLoginDto("bob@example.com", "password123");
        when(userService.findByUsername("bob@example.com")).thenReturn(null);

        ResponseEntity<Map<String, String>> response = authController.login(dto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().get("message").contains("Invalid credentials"));
    }
}
