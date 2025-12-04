package com.groom.manvsclass.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when class validation fails during upload.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClassValidationException extends RuntimeException {
    
    public ClassValidationException(String message) {
        super(message);
    }

    public ClassValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
