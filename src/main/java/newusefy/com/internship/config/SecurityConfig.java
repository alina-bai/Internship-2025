package newusefy.com.internship.config;

import newusefy.com.internship.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // Разрешаем CORS
                .cors(Customizer.withDefaults())

                // Отключаем управление сессиями для REST API с JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Настраиваем доступ к эндпоинтам
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Регистрация и логин открыты

                        // ✅ ФИНАЛЬНОЕ ИСПРАВЛЕНИЕ 403: Упрощаем до минимального требования:
                        // Требуем, чтобы пользователь был просто аутентифицирован (имел валидный токен)
                        .requestMatchers("/api/chat/**").authenticated()

                        // Защищаем все остальные пути
                        .anyRequest().authenticated()
                );

        // Регистрируем ваш JWT-фильтр
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
