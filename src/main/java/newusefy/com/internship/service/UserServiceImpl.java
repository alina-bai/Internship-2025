package newusefy.com.internship.service;

import newusefy.com.internship.dto.UserRegistrationDto;
import newusefy.com.internship.model.User;
import newusefy.com.internship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User registerUser(UserRegistrationDto dto) {
        // Проверяем — не зарегистрирован ли уже пользователь с таким EMAIL
        if (userRepository.existsByUsername(dto.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }

}
