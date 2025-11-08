package newusefy.com.internship.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Добавляем Logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Лог 1: Проверка заголовка
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("Authorization header missing or does not start with Bearer. Continuing chain.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = null;

        try {
            username = jwtUtil.validateAndGetUsername(token);

            // Лог 2: Успешная валидация
            logger.info("Token successfully validated for user: {}", username);

            // Если контекст аутентификации еще не установлен
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                // кладём аутентификацию в SecurityContext
                var auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.info("Security context set for user: {}", username);
            }

        } catch (SignatureException ex) {
            logger.error("JWT validation failed: Invalid Signature. Check secret key!", ex);
            SecurityContextHolder.clearContext();
        } catch (ExpiredJwtException ex) {
            logger.error("JWT validation failed: Token is expired.", ex);
            SecurityContextHolder.clearContext();
        } catch (MalformedJwtException ex) {
            logger.error("JWT validation failed: Malformed token structure.", ex);
            SecurityContextHolder.clearContext();
        } catch (Exception ex) {
            logger.error("JWT validation failed: General error.", ex);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
