package com.audit.exception;

import java.time.LocalDateTime;
import java.util.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new BaseErrorDetail(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage())
        );
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<?> handleInternalServerErrorException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new BaseErrorDetail(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), ex.getMessage())
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new BaseErrorDetail(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage())
        );
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new BaseErrorDetail(HttpStatus.BAD_REQUEST, LocalDateTime.now(), 
                ex.getName()+" should be type "+ex.getRequiredType().getName())
        );
    }
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, 
      HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(
            new BaseErrorDetail(HttpStatus.BAD_REQUEST, LocalDateTime.now(), 
            ex.getParameterName() + " parameter is missing"),
            new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request ){
        return new ResponseEntity<>(
            new BaseErrorDetail(HttpStatus.BAD_REQUEST, LocalDateTime.now(), 
                "Not find handler for this path: "+ex.getHttpMethod()+" "+ex.getRequestURL()),
            new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, 
      HttpHeaders headers, 
      HttpStatus status, 
      WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
          " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
    
        return new ResponseEntity<>(
            new BaseErrorDetail(HttpStatus.BAD_REQUEST, LocalDateTime.now(), builder.toString()),
            new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class}) 
    public ResponseEntity<?> handleAllException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            new BaseErrorDetail(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), 
                "Something went wrong. Please try again later.")
        );
    }

}

class BaseErrorDetail {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;

    public BaseErrorDetail(HttpStatus status, LocalDateTime timestamp, String message) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

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
