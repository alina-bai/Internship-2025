package newusefy.com.internship.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // ===== Конструкторы =====
    public User() {
    }

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // ===== Геттеры и сеттеры =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // ⭐ ИСХОДНЫЙ МЕТОД: возвращает хеш
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // ⭐ НОВЫЙ МЕТОД ДЛЯ СОВМЕСТИМОСТИ:
    // Метод, который ищет AuthController/PasswordEncoder (возвращает то же, что и getPasswordHash)
    public String getPassword() {
        return this.passwordHash;
    }
}