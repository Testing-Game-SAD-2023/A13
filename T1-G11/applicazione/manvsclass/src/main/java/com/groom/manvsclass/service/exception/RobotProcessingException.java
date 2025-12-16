package com.groom.manvsclass.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when robot test processing fails during upload.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RobotProcessingException extends LocalizedException {
    
    /**
     * Constructor with message key only (no detailed message needed)
     */
    public RobotProcessingException(String messageKey) {
        super(messageKey, messageKey);
    }
    
    /**
     * Constructor with message key and parameters
     */
    public RobotProcessingException(String messageKey, Object... params) {
        super(messageKey, messageKey, params);
    }

    /**
     * Constructor with detailed message only (legacy support)
     */
    public RobotProcessingException(String message, boolean legacy) {
        super(message, null);
    }

    /**
     * Constructor with detailed message and cause (legacy support)
     */
    public RobotProcessingException(String message, Throwable cause) {
        super(message, null, cause);
    }
    
    /**
     * Constructor with detailed message and message key for localization (legacy)
     */
    public RobotProcessingException(String detailedMessage, String messageKey, boolean withDetail, Object... params) {
        super(detailedMessage, messageKey, params);
    }
    
    /**
     * Constructor with detailed message, message key, cause, and parameters (legacy)
     */
    public RobotProcessingException(String detailedMessage, String messageKey, Throwable cause, Object... params) {
        super(detailedMessage, messageKey, cause, params);
    }
}
