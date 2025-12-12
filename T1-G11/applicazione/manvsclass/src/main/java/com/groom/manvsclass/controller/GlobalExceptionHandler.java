package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.dto.ErrorResponseDTO;
import com.groom.manvsclass.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.util.Locale;

/**
 * Global exception handler for REST controllers.
 * Provides uniform error responses across the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Handle file upload exceptions (file empty, invalid format, etc.)
     */
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponseDTO> handleFileUploadException(FileUploadException ex, WebRequest request, Locale locale) {
        // Log the detailed error message
        logger.error("File upload error: {}", ex.getMessage(), ex.getCause());
        
        // Get localized message using message key if available, otherwise use detailed message
        String localizedMessage = getLocalizedMessage(ex, locale);
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "File Upload Error",
                localizedMessage
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle class validation exceptions (class name mismatch, no class declaration, etc.)
     */
    @ExceptionHandler(ClassValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleClassValidationException(ClassValidationException ex, WebRequest request, Locale locale) {
        // Log the detailed error message
        logger.error("Class validation error: {}", ex.getMessage(), ex.getCause());
        
        // Get localized message using message key if available, otherwise use detailed message
        String localizedMessage = getLocalizedMessage(ex, locale);
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Class Validation Error",
                localizedMessage
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle robot processing exceptions (invalid robot tests, processing failures, etc.)
     */
    @ExceptionHandler(RobotProcessingException.class)
    public ResponseEntity<ErrorResponseDTO> handleRobotProcessingException(RobotProcessingException ex, WebRequest request, Locale locale) {
        // Log the detailed error message
        logger.error("Robot processing error: {}", ex.getMessage(), ex.getCause());
        
        // Get localized message using message key if available, otherwise use detailed message
        String localizedMessage = getLocalizedMessage(ex, locale);
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Robot Processing Error",
                localizedMessage
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle external service exceptions (API calls to other microservices)
     */
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponseDTO> handleExternalServiceException(ExternalServiceException ex, WebRequest request, Locale locale) {
        logger.error("External service error: {}", ex.getMessage(), ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "External Service Error",
                messageSource.getMessage("error.external.service", null, locale)
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handle opponent not found exceptions
     */
    @ExceptionHandler(OpponentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleOpponentNotFoundException(OpponentNotFoundException ex, WebRequest request, Locale locale) {
        logger.error("Opponent not found: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Opponent Not Found",
                messageSource.getMessage("error.opponent.notFound", null, locale)
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle score not found exceptions
     */
    @ExceptionHandler(ScoreNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleScoreNotFoundException(ScoreNotFoundException ex, WebRequest request, Locale locale) {
        logger.error("Score not found: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Score Not Found",
                messageSource.getMessage("error.score.notFound", null, locale)
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle coverage not found exceptions
     */
    @ExceptionHandler(CoverageNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCoverageNotFoundException(CoverageNotFoundException ex, WebRequest request, Locale locale) {
        logger.error("Coverage not found: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Coverage Not Found",
                messageSource.getMessage("error.coverage.notFound", null, locale)
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle file size exceeded exceptions
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, WebRequest request, Locale locale) {
        logger.error("File size exceeded: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "File Size Exceeded",
                messageSource.getMessage("error.file.sizeExceeded", null, locale)
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle illegal argument exceptions (validation errors)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request, Locale locale) {
        logger.error("Illegal argument: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Argument",
                ex.getMessage() != null ? ex.getMessage() : messageSource.getMessage("error.argument.invalid", null, locale)
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle null pointer exceptions (missing required data)
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponseDTO> handleNullPointerException(NullPointerException ex, WebRequest request, Locale locale) {
        logger.error("Null pointer error: {}", ex.getMessage(), ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Missing Required Data",
                messageSource.getMessage("error.data.missing", null, locale)
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle IO exceptions
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponseDTO> handleIOException(IOException ex, WebRequest request, Locale locale) {
        logger.error("IO error: {}", ex.getMessage(), ex);
        
        // Check if it's a more specific IO error we can provide better feedback for
        String exMessage = ex.getMessage();
        String message;
        if (exMessage != null && exMessage.contains("coverage")) {
            message = String.format(messageSource.getMessage("error.io.coverage", null, locale), exMessage);
        } else if (exMessage != null && exMessage.contains("ZIP")) {
            message = String.format(messageSource.getMessage("error.io.zip", null, locale), exMessage);
        } else {
            message = String.format(messageSource.getMessage("error.io.generic", null, locale), 
                exMessage != null ? exMessage : "Unknown error");
        }
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "IO Error",
                message
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handle generic runtime exceptions
     * NOTE: Only handles RuntimeException for REST controllers, not view controllers
     * This won't interfere with existing ResponseEntity returns since those are successful responses
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex, WebRequest request, Locale locale) {
        // Don't log full stack trace for known exceptions to avoid noise
        if (ex instanceof OpponentNotFoundException || 
            ex instanceof ScoreNotFoundException || 
            ex instanceof CoverageNotFoundException) {
            logger.warn("Runtime error: {}", ex.getMessage());
        } else {
            logger.error("Runtime error: {}", ex.getMessage(), ex);
        }
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                messageSource.getMessage("error.runtime.generic", null, locale)
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handle all other exceptions
     * This is the last resort handler for unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest request, Locale locale) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                messageSource.getMessage("error.generic", null, locale)
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Helper method to get localized message from LocalizedException
     * If the exception has a message key, use it to get the localized message with parameters.
     * Otherwise, return the detailed message from the exception.
     */
    private String getLocalizedMessage(LocalizedException ex, Locale locale) {
        if (ex.hasMessageKey()) {
            String messageKey = ex.getMessageKey();
            Object[] params = ex.getMessageParams();
            
            try {
                return messageSource.getMessage(messageKey, params, locale);
            } catch (Exception e) {
                logger.warn("Could not resolve message key '{}', using default message", messageKey);
                return ex.getMessage();
            }
        }
        
        // Fallback to the detailed message
        return ex.getMessage();
    }
}

