package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.dto.ErrorResponseDTO;
import com.groom.manvsclass.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

/**
 * Global exception handler for REST controllers.
 * Provides uniform error responses across the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle file upload exceptions (file empty, invalid format, etc.)
     */
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponseDTO> handleFileUploadException(FileUploadException ex, WebRequest request) {
        logger.error("File upload error: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "File Upload Error",
                ex.getMessage()
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle class validation exceptions (class name mismatch, no class declaration, etc.)
     */
    @ExceptionHandler(ClassValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleClassValidationException(ClassValidationException ex, WebRequest request) {
        logger.error("Class validation error: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Class Validation Error",
                ex.getMessage()
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle robot processing exceptions (invalid robot tests, processing failures, etc.)
     */
    @ExceptionHandler(RobotProcessingException.class)
    public ResponseEntity<ErrorResponseDTO> handleRobotProcessingException(RobotProcessingException ex, WebRequest request) {
        logger.error("Robot processing error: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Robot Processing Error",
                ex.getMessage()
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle external service exceptions (API calls to other microservices)
     */
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponseDTO> handleExternalServiceException(ExternalServiceException ex, WebRequest request) {
        logger.error("External service error: {}", ex.getMessage(), ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "External Service Error",
                "Si è verificato un errore durante la comunicazione con un servizio esterno. Riprovare più tardi."
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handle opponent not found exceptions
     */
    @ExceptionHandler(OpponentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleOpponentNotFoundException(OpponentNotFoundException ex, WebRequest request) {
        logger.error("Opponent not found: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Opponent Not Found",
                "L'avversario richiesto non è stato trovato."
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle score not found exceptions
     */
    @ExceptionHandler(ScoreNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleScoreNotFoundException(ScoreNotFoundException ex, WebRequest request) {
        logger.error("Score not found: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Score Not Found",
                "Il punteggio richiesto non è stato trovato."
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle coverage not found exceptions
     */
    @ExceptionHandler(CoverageNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCoverageNotFoundException(CoverageNotFoundException ex, WebRequest request) {
        logger.error("Coverage not found: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Coverage Not Found",
                "La copertura richiesta non è stata trovata."
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle file size exceeded exceptions
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, WebRequest request) {
        logger.error("File size exceeded: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "File Size Exceeded",
                "Il file caricato supera la dimensione massima consentita. " +
                "Verificare la dimensione del file ZIP e del file .java."
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle illegal argument exceptions (validation errors)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.error("Illegal argument: {}", ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Argument",
                ex.getMessage()
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle null pointer exceptions (missing required data)
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponseDTO> handleNullPointerException(NullPointerException ex, WebRequest request) {
        logger.error("Null pointer error: {}", ex.getMessage(), ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Missing Required Data",
                "Dati obbligatori mancanti nella richiesta. " +
                "Verificare che tutti i campi richiesti siano compilati e che i file siano stati selezionati."
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle IO exceptions
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponseDTO> handleIOException(IOException ex, WebRequest request) {
        logger.error("IO error: {}", ex.getMessage(), ex);
        
        // Check if it's a more specific IO error we can provide better feedback for
        String message = ex.getMessage();
        if (message != null && message.contains("coverage")) {
            message = "Errore durante la lettura dei file di coverage: " + message;
        } else if (message != null && message.contains("ZIP")) {
            message = "Errore durante l'elaborazione del file ZIP: " + message;
        } else {
            message = "Si è verificato un errore durante l'operazione sul file system: " + message + ". Riprovare.";
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
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex, WebRequest request) {
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
                "Si è verificato un errore imprevisto. Riprovare più tardi."
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handle all other exceptions
     * This is the last resort handler for unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Si è verificato un errore imprevisto. Contattare l'amministratore del sistema."
        );
        error.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
