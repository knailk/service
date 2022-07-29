package com.audit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private final String message;

    public BadRequestException(String message) {
        // super("Bad Request");
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
