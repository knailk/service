package com.audit.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Object error = new ObjectMapper().convertValue(errors, Object.class);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseErrorDetail(HttpStatus.BAD_REQUEST,
                LocalDateTime.now(), error.toString()));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> handleDateValidationExceptions(InvalidFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseErrorDetail(HttpStatus.BAD_REQUEST,
                LocalDateTime.now(), "Wrong date time format. The true format is YYYY-MM-DD HH:MM"));
    }
}

