package newusefy.com.internship.model;

import jakarta.persistence.*; // импортируем JPA аннотации

@Entity // помечаем класс как JPA Entity (будет таблицей в базе)
@Table(name = "users") // имя таблицы в базе данных
public class User {

    @Id // уникальный идентификатор (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // автоинкремент id
    private Long id;

    @Column(nullable = false, unique = true) // username не может быть null и должен быть уникальным
    private String username;

    //@Column(nullable = false) // пароль не может быть пустым
    //private String passwordHash;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // ===== Конструкторы =====
    public User() {
        // пустой конструктор нужен JPA
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}

