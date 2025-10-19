package newusefy.com.internship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import newusefy.com.internship.dto.UserLoginDto;
import newusefy.com.internship.dto.UserRegistrationDto;
import newusefy.com.internship.model.User;
import newusefy.com.internship.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ Тест регистрации успешной
    @Test
    void testRegisterUser_success() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto("alice", "alice@example.com", "secret");

        when(userService.registerUser(any(UserRegistrationDto.class)))
                .thenReturn(new User("alice", "encodedSecret"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        verify(userService, times(1)).registerUser(any(UserRegistrationDto.class));
    }

    // ✅ Тест регистрации — username уже существует
    @Test
    void testRegisterUser_existingUsername() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto("bob", "bob@example.com", "123456");

        when(userService.registerUser(any(UserRegistrationDto.class)))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username already exists"))
                .andExpect(jsonPath("$.status").value(400));
    }

    // ✅ Тест логина — успешный вход
    @Test
    void testLogin_success() throws Exception {
        UserLoginDto dto = new UserLoginDto("john", "password");
        User existingUser = new User("john", "encodedPassword");

        when(userService.findByUsername("john")).thenReturn(existingUser);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    // ✅ Тест логина — неверный пароль
    @Test
    void testLogin_wrongPassword() throws Exception {
        UserLoginDto dto = new UserLoginDto("john", "wrong");
        User existingUser = new User("john", "encodedPassword");

        when(userService.findByUsername("john")).thenReturn(existingUser);
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    // ✅ Тест логина — пользователь не найден
    @Test
    void testLogin_userNotFound() throws Exception {
        UserLoginDto dto = new UserLoginDto("ghost", "123");
        when(userService.findByUsername("ghost")).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    // ✅ Тест регистрации — невалидные поля
    @Test
    void testRegisterUser_invalidBody() throws Exception {
        UserRegistrationDto invalidDto = new UserRegistrationDto("", "invalid_email", "12");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.email").exists());

        verify(userService, never()).registerUser(any(UserRegistrationDto.class));
    }
}
