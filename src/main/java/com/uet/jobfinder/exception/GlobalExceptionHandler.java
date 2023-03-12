package com.uet.jobfinder.exception;

import com.uet.jobfinder.error.Error;
import com.uet.jobfinder.error.LoginError;
import com.uet.jobfinder.error.ServerError;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<Object> errorList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            boolean isErrorDefined = false;
            for (ServerError serverError : ServerError.values()) {
                if (error.getDefaultMessage().equals(serverError.getCode())) {
                    errorList.add(serverError);
                }
            }

            if (!isErrorDefined) {
                errorList.add(new Object() {
                    public String code = "NotDefined";
                    public String message = error.getDefaultMessage();
                });
            }
        });

        return new ResponseEntity<>(
                Map.of("errors", errorList), HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler({CustomIllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            CustomIllegalArgumentException ex) {
        return new ResponseEntity<>(
                Map.of("errors", List.of(ex.getError()))
                , HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<Object> handleSystemException(Exception e) {
        //  Logging
        e.printStackTrace();
        Map<String, Object> errors = new HashMap<>();

        if (e instanceof AccessDeniedException) {
            errors.put("error", "Bạn không có thẩm quyền.");
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        }

        if (e instanceof BadCredentialsException) {
            return new ResponseEntity<>(LoginError.WRONG_PASSWORD_OR_USERNAME, HttpStatus.UNAUTHORIZED);
        }

        errors.put("error", "Invalid request.");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
