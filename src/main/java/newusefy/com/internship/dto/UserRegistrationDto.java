package newusefy.com.internship.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size; // <--- НОВЫЙ ИМПОРТ

/**
 * DTO used for user registration requests.
 * Contains raw fields coming from the client (plain password).
 */
public class UserRegistrationDto {

    @NotEmpty(message = "Username cannot be blank")
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;

    @NotEmpty(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password cannot be blank")
    // ⭐ КЛЮЧЕВОЕ ИСПРАВЛЕНИЕ: Добавление проверки минимальной длины
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public UserRegistrationDto() { }

    public UserRegistrationDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters & setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}