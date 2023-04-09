package com.uet.jobfinder.exception;

import com.uet.jobfinder.error.LoginError;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.model.ErrorMessageModel;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        List<Object> errorList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String code = "undefined";
            String message = error.getDefaultMessage();

            for (ServerError serverError : ServerError.values()) {
                if (error.getDefaultMessage().equals(serverError.getCode())) {
                    code = serverError.getCode();

                    if (error.getDefaultMessage().equals("SVERR3") ||
                            error.getDefaultMessage().equals("SVERR4")) {
                        message = ((FieldError) error).getField() + " " + serverError.getMessage();
                    } else {
                        message = serverError.getMessage();
                    }
                }
            }
            errorList.add(
                    Map.of("code", code, "message", message)
            );
        });

        return new ResponseEntity<>(
                Map.of("errors", errorList), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            BindException e
    ) {
        e.printStackTrace();
        List<Object> errorList = new ArrayList<>();
        e.getAllErrors().forEach((error) -> {
            String code = "undefined";
            String message = error.getDefaultMessage();

            for (ServerError serverError : ServerError.values()) {
                if (error.getDefaultMessage().equals(serverError.getCode())) {
                    code = serverError.getCode();

                    if (error.getDefaultMessage().equals("SVERR3") ||
                            error.getDefaultMessage().equals("SVERR4")) {
                        message = ((FieldError) error).getField() + " " + serverError.getMessage();
                    } else {
                        message = serverError.getMessage();
                    }
                }
            }
            errorList.add(
                    Map.of("code", code, "message", message)
            );
        });

        return new ResponseEntity<>(
                Map.of("errors", errorList), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<Map<String, Object>> handleInvalidContentType(
            HttpMediaTypeNotSupportedException e
    ) {
        return new ResponseEntity<>(
                Map.of("errors", List.of(ServerError.INVALID_REQUEST)), HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<Object> handleMultipartException(MultipartException e) {
        e.printStackTrace();
        if (e instanceof MaxUploadSizeExceededException) {
            return ResponseEntity.badRequest()
                    .body(
                            new ErrorMessageModel(List.of(ServerError.MAXIMUM_FILE_EXCEEDED))
                    );
        }

        return ResponseEntity.badRequest()
                .body(
                        new ErrorMessageModel(List.of(ServerError.SERVER_ERROR))
                );
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleSystemException(Exception e) {
        //  Logging
        e.printStackTrace();

        if (e instanceof HttpRequestMethodNotSupportedException) {
            return ResponseEntity.badRequest()
                    .body(new ErrorMessageModel(List.of(ServerError.INVALID_REQUEST)));
        }

        if (e instanceof MaxUploadSizeExceededException ||
            e instanceof SizeLimitExceededException) {
            return ResponseEntity.badRequest()
                    .body(
                            new ErrorMessageModel(List.of(ServerError.MAXIMUM_FILE_EXCEEDED))
                    );
        }

        if (e instanceof AccessDeniedException) {
            return new ResponseEntity<>(
                    Map.of("errors",
                            List.of(ServerError.ACCESS_DENIED))
                    , HttpStatus.UNAUTHORIZED);
        }

        if (e instanceof BadCredentialsException) {
            return new ResponseEntity<>(
                Map.of("errors", List.of(
                    LoginError.WRONG_PASSWORD_OR_USERNAME)), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(
                Map.of("errors", List.of(ServerError.SERVER_ERROR)),
                HttpStatus.BAD_REQUEST);
    }

}
