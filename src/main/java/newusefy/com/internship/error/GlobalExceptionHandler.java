package newusefy.com.internship.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // This class applies its exception handling methods globally
public class GlobalExceptionHandler {

    // 1. Handles validation errors (JSR 303/380)
    // This resolves the error from Task 1/8 and ensures the test passes.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Map to store field names (key) and error messages (value)
        Map<String, String> errors = new HashMap<>();

        // Iterate over all validation errors found in the DTO
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        // Return HTTP 400 Bad Request with the map of errors.
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2. Handles custom errors like 'Username already exists' (IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

        // Build the structured ErrorResponse
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage() // e.g., "Username already exists"
        );
        // Return HTTP 400 Bad Request with the structured response.
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}