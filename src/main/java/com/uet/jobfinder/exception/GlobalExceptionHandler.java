package com.uet.jobfinder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            IllegalArgumentException ex) {
//        ex.printStackTrace();
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({LockedException.class})
    public ResponseEntity<Map<String, String>> handleLoginExceptions(
            LockedException e
    ) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DisabledException.class})
    public ResponseEntity<Map<String, String>> handleLoginExceptions(
            DisabledException e
    ) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Map<String, String>> handleSystemException(Exception e) {
        //  Logging
        e.printStackTrace();
        Map<String, String> errors = new HashMap<>();

        if (e instanceof AccessDeniedException) {
            errors.put("error", "Bạn không có thẩm quyền.");
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        }

        if (e instanceof BadCredentialsException) {
            errors.put("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        }

        errors.put("error", "Invalid request.");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
