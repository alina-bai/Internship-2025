package newusefy.com.internship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ⭐ ДОБАВЛЯЕМ ПУТИ ДЛЯ HTML-ШАБЛОНОВ: /, /register и /login (если добавите)
                        .requestMatchers("/", "/register", "/login").permitAll()
                        // ⭐ ДОБАВЛЯЕМ ПУТИ ДЛЯ СТАТИЧЕСКИХ РЕСУРСОВ (CSS, JS, Images)
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        // Разрешение фреймов для H2-консоли (если вы ее используете)
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}