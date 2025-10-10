package newusefy.com.internship.service;

import newusefy.com.internship.model.User;
import newusefy.com.internship.dto.UserRegistrationDto;

public interface UserService {
    // Регистрирует нового пользователя, принимая DTO
    User registerUser(UserRegistrationDto userData);

    // Находит пользователя по username
    User findByUsername(String username);
}
