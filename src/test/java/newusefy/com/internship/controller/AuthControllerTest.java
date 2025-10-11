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

    // ================== Тест регистрации ==================

    @Test
    void testRegisterUser_success() throws Exception {
        // ⭐ ИСПРАВЛЕНО: Объявляем и инициализируем DTO
        UserRegistrationDto dto = new UserRegistrationDto("alice", "secret");

        // Настраиваем поведение мока: userService.registerUser возвращает нового юзера
        when(userService.registerUser(any(UserRegistrationDto.class)))
                .thenReturn(new User("alice", "encodedSecret"));

        // Выполняем POST-запрос
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))

                // Проверяем статус 201 Created (для создания ресурса)
                .andExpect(status().isCreated())

                // ⭐ ИСПРАВЛЕНО: Проверяем строковое тело ответа,
                // если контроллер возвращает "User registered successfully"
                .andExpect(content().string("User registered successfully"));

        // Проверяем, что метод вызывался 1 раз
        verify(userService, times(1)).registerUser(any(UserRegistrationDto.class));
    }

    @Test
    void testRegisterUser_existingUsername() throws Exception {
        // Здесь DTO уже было, оставляем как есть
        UserRegistrationDto dto = new UserRegistrationDto("bob", "1234");

        when(userService.registerUser(any(UserRegistrationDto.class)))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    // ================== Тест логина ==================

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
                .andExpect(content().string("Login successful"));
    }

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
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testLogin_userNotFound() throws Exception {
        UserLoginDto dto = new UserLoginDto("ghost", "123");

        when(userService.findByUsername("ghost")).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }
}