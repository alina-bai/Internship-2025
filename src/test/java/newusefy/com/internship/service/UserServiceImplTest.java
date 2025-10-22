package newusefy.com.internship.service;

import newusefy.com.internship.dto.UserRegistrationDto;
import newusefy.com.internship.model.User;
import newusefy.com.internship.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void testRegisterUserSuccess() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("alice@example.com");
        dto.setPassword("password123");

        when(userRepository.existsByUsername(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User(dto.getEmail(), "encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(dto);

        assertNotNull(result);
        assertEquals("alice@example.com", result.getUsername());
        assertEquals("encodedPassword", result.getPasswordHash());
    }

    @Test
    void testRegisterUserAlreadyExists() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("alice@example.com");
        dto.setPassword("password123");

        when(userRepository.existsByUsername(dto.getEmail())).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(dto));

        assertEquals("User already exists", thrown.getMessage());
    }

    @Test
    void testFindByUsernameFound() {
        User user = new User("alice@example.com", "hash");
        when(userRepository.findByUsername("alice@example.com")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("alice@example.com");

        assertNotNull(result);
        assertEquals("alice@example.com", result.getUsername());
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("bob@example.com")).thenReturn(Optional.empty());

        User result = userService.findByUsername("bob@example.com");

        assertNull(result);
    }
}
