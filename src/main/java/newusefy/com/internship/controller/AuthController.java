package newusefy.com.internship.controller;

import newusefy.com.internship.dto.UserLoginDto;
import newusefy.com.internship.dto.UserRegistrationDto;
import newusefy.com.internship.model.User;
import newusefy.com.internship.security.JwtUtil;
import newusefy.com.internship.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // ✅ добавили зависимость

    // ✅ Правильный конструктор — все зависимости внедряются через параметры
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        userService.registerUser(registrationDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Авторизация (логин)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDto loginDto) {
        User user = userService.findByUsername(loginDto.getUsername());
        Map<String, String> response = new HashMap<>();

        if (user != null && passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            response.put("message", "Login successful");
            response.put("token", jwtUtil.generateToken(user.getUsername())); // <— JWT создаётся тут
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
