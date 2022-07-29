package com.audit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {
    private final String message;

    public InternalServerException(String message) {
        // super("Internal Server Error");
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
}
