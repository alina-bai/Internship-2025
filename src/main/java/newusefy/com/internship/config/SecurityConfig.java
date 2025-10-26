package newusefy.com.internship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Для шифрования и проверки паролей
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF (для REST API это нормально)
                .csrf(csrf -> csrf.disable())

                // Разрешаем CORS (чтобы React мог делать запросы)
                .cors(Customizer.withDefaults())

                // Настраиваем доступ к эндпоинтам
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Регистрация и логин открыты
                        .requestMatchers("/api/chat/**").authenticated() // Чат доступен только авторизованным
                        .anyRequest().authenticated() // Остальные тоже защищены
                )

                // Используем HTTP Basic для тестирования (позже заменим JWT)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
