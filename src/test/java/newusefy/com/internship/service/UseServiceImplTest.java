package newusefy.com.internship.service;

import newusefy.com.internship.dto.UserRegistrationDto;
import newusefy.com.internship.model.User;
import newusefy.com.internship.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_shouldSaveUser_whenUsernameIsNew() {
        // given
        UserRegistrationDto dto = new UserRegistrationDto("alice", "alice@example.com", "password123");


        // Оборачиваем в Optional.empty(), потому что пользователя нет
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded123");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User result = userService.registerUser(dto);

        // then
        assertEquals("alice", result.getUsername());
        assertEquals("encoded123", result.getPasswordHash());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowException_whenUsernameExists() {
        // given
        UserRegistrationDto dto = new UserRegistrationDto("bob", "bob@example.com", "secret");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(new User("bob", "hash")));

        // when + then
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        assertEquals("Username already exists.", ex.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByUsername_shouldReturnUser_whenUserExists() {
        // given
        User user = new User("john", "encoded");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        // when
        User result = userService.findByUsername("john");

        // then
        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("encoded", result.getPasswordHash());
    }

    @Test
    void findByUsername_shouldReturnNull_whenUserNotFound() {
        // given
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // when
        User result = userService.findByUsername("nonexistent");

        // then
        assertNull(result);
    }
}
