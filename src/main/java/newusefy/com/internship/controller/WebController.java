package newusefy.com.internship.controller;

import newusefy.com.internship.dto.UserRegistrationDto;
import newusefy.com.internship.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private final UserService userService;

    // Внедряем UserService через конструктор
    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ⭐ 1. Метод для отображения формы (GET)
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    // ⭐ 2. Метод для обработки формы (POST)
    @PostMapping("/register") // Принимаем POST-запрос с формы
    public String registerUser(
            // Spring автоматически связывает параметры формы с этим DTO
            @ModelAttribute UserRegistrationDto registrationDto,
            RedirectAttributes redirectAttributes) {

        try {
            userService.registerUser(registrationDto);

            // Если успех, добавляем сообщение и перенаправляем на главную
            redirectAttributes.addFlashAttribute("message", "Регистрация прошла успешно! Теперь можете войти.");
            return "redirect:/"; // Перенаправляем на главную страницу

        } catch (IllegalArgumentException e) {

            // Если ошибка (например, имя уже занято), добавляем сообщение и остаемся на форме
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register"; // Перенаправляем обратно на форму регистрации
        }
    }

    // Добавьте метод для login.html, когда будете готовы
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
}