package newusefy.com.internship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    // Define a Bean to configure CORS rules globally
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allows all paths (/**) in this application to accept requests
                // from the React development server (http://localhost:3000).
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173") // ⭐ IMPORTANT: Replace with your React app's address
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Needed for cookies, session, or basic auth
            }
        };
    }
}