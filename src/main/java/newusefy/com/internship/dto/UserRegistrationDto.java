package newusefy.com.internship.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotEmpty(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // ===== Конструкторы =====
    public UserRegistrationDto() {
    }

    // ✅ Конструктор, который используется в старых тестах (2 аргумента)
    public UserRegistrationDto(String username, String email) {
        this.username = username;
        this.email = email;
        this.password = "default123"; // чтобы не было null
    }

    // ✅ Конструктор, который используется в новых тестах (3 аргумента)
    public UserRegistrationDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // ===== Геттеры и сеттеры =====
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
