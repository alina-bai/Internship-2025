package newusefy.com.internship.controller;

import newusefy.com.internship.dto.UserLoginDto;
import newusefy.com.internship.dto.UserRegistrationDto;
import newusefy.com.internship.model.User;
import newusefy.com.internship.security.JwtUtil;
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
    private JwtUtil jwtUtil;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtUtil = Mockito.mock(JwtUtil.class);
        authController = new AuthController(userService, passwordEncoder, jwtUtil);
    }

    @Test
    void testRegisterSuccess() {
        UserRegistrationDto dto = new UserRegistrationDto("Alice", "alice@example.com", "password123");
        User newUser = new User("alice@example.com", "encodedPassword");

        when(userService.registerUser(dto)).thenReturn(newUser);

        ResponseEntity<Map<String, String>> response = authController.register(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody().get("message"));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        UserRegistrationDto dto = new UserRegistrationDto("Alice", "alice@example.com", "password123");
        when(userService.registerUser(dto)).thenThrow(new IllegalArgumentException("User already exists"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.register(dto);
        });

        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void testLoginSuccess() {
        UserLoginDto loginDto = new UserLoginDto("alice@example.com", "password123");
        User user = new User("alice@example.com", "encodedPassword");

        when(userService.findByUsername("alice@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("alice@example.com")).thenReturn("fake-jwt-token");

        ResponseEntity<Map<String, String>> response = authController.login(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody().get("message"));
        assertEquals("fake-jwt-token", response.getBody().get("token"));
    }

    @Test
    void testLoginInvalidCredentials() {
        UserLoginDto loginDto = new UserLoginDto("bob@example.com", "wrong");
        when(userService.findByUsername("bob@example.com")).thenReturn(null);

        ResponseEntity<Map<String, String>> response = authController.login(loginDto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody().get("message"));
    }
}
