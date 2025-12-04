package com.groom.manvsclass.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when robot test processing fails during upload.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RobotProcessingException extends RuntimeException {
    
    public RobotProcessingException(String message) {
        super(message);
    }

    public RobotProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
