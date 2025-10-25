package newusefy.com.internship.service;

import newusefy.com.internship.dto.UserRegistrationDto;
import newusefy.com.internship.model.User;

public interface UserService {
    // Регистрирует нового пользователя
    User registerUser(UserRegistrationDto userData);

    // Находит пользователя по username (email)
    User findByUsername(String username);
}
