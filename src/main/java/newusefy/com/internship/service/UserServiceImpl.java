package newusefy.com.internship.service;

import newusefy.com.internship.dto.UserRegistrationDto;
import newusefy.com.internship.model.User;
import newusefy.com.internship.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Dependency Injection
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegistrationDto userData) {
        // Проверка: Если пользователь с таким именем уже существует, бросаем исключение
        if (userRepository.findByUsername(userData.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }

        // 1. Создаём нового User
        User user = new User();
        user.setUsername(userData.getUsername());

        // 2. Хэшируем пароль перед сохранением
        String hashedPassword = passwordEncoder.encode(userData.getPassword());
        user.setPasswordHash(hashedPassword);

        // 3. Сохраняем в базу
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}