package com.groom.manvsclass.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when external API calls fail.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExternalServiceException extends RuntimeException {
    
    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
