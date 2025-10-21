package com.robotchallenge.t8.controller.advice;

import com.robotchallenge.t8.service.exception.ProcessingExceptionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import testrobotchallenge.commons.models.dto.api.ApiErrorBackend;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Controller advice che intercetta e gestisce le eccezioni sollevate
 * durante l'esecuzione dei task con EvoSuite.
 */
@ControllerAdvice
public class TaskExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(TaskExceptionHandler.class);

    @ExceptionHandler(ProcessingExceptionWrapper.class)
    ResponseEntity<ApiErrorBackend> handleProcessingException(ProcessingExceptionWrapper e) {
        Throwable cause = e.getCause();
        return switch (cause) {
            case InterruptedException interruptedException -> handleInterruptedException(interruptedException);
            case TimeoutException timeoutException -> handleTimeoutException(timeoutException);
            case RejectedExecutionException rejectedExecutionException ->
                    handleRejectedExecutionException(rejectedExecutionException);
            case ExecutionException executionException -> handleExecutionException(executionException);
            case IOException ioException -> handleIOException(ioException);
            default -> handleGenericException((Exception) cause);
        };
    }

    private ResponseEntity<ApiErrorBackend> handleInterruptedException(InterruptedException e) {
        Thread.currentThread().interrupt();
        logger.error("[CoverageControllerAdvice] Il processo è stato interrotto", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorBackend("Il processo è stato interrotto."));
    }

    private ResponseEntity<ApiErrorBackend> handleTimeoutException(TimeoutException e) {
        logger.warn("[CoverageControllerAdvice] Timeout superato", e);
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                .body(new ApiErrorBackend("Il task ha superato il tempo massimo disponibile."));
    }

    private ResponseEntity<ApiErrorBackend> handleRejectedExecutionException(RejectedExecutionException e) {
        logger.warn("[CoverageControllerAdvice] Coda piena: impossibile accettare nuovi task", e);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ApiErrorBackend("Troppe richieste in coda. Riprova più tardi."));
    }

    private ResponseEntity<ApiErrorBackend> handleExecutionException(ExecutionException e) {
        logger.error("[CoverageControllerAdvice] Errore durante la compilazione o l'esecuzione", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorBackend("Errore durante la compilazione o l'esecuzione."));
    }

    private ResponseEntity<ApiErrorBackend> handleIOException(IOException e) {
        logger.error("[CoverageControllerAdvice] Errore di I/O", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorBackend("Si è verificato un errore imprevisto."));
    }

    private ResponseEntity<ApiErrorBackend> handleGenericException(Exception e) {
        logger.error("[CoverageControllerAdvice] Errore generico", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorBackend("Si è verificato un errore imprevisto."));
    }
}
