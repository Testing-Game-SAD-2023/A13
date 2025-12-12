package com.groom.manvsclass.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when file upload operations fail.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileUploadException extends LocalizedException {
    
    /**
     * Constructor with detailed message only (legacy support)
     */
    public FileUploadException(String message) {
        super(message, null);
    }

    /**
     * Constructor with detailed message and cause (legacy support)
     */
    public FileUploadException(String message, Throwable cause) {
        super(message, null, cause);
    }
    
    /**
     * Constructor with detailed message and message key for localization
     */
    public FileUploadException(String detailedMessage, String messageKey, Object... params) {
        super(detailedMessage, messageKey, params);
    }
    
    /**
     * Constructor with detailed message, message key, cause, and parameters
     */
    public FileUploadException(String detailedMessage, String messageKey, Throwable cause, Object... params) {
        super(detailedMessage, messageKey, cause, params);
    }
}
