package com.groom.manvsclass.controller.advices;

import com.groom.manvsclass.service.exception.*;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import testrobotchallenge.commons.models.dto.api.ApiErrorBackend;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ScalataLevelException.class)
    public ResponseEntity<ApiErrorBackend> scalataLevel(ScalataLevelException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorBackend(messageSource.getMessage("errors.scalata.level", null, locale))
        );
    }

    @ExceptionHandler(NegativeTempoMaxException.class)
    public ResponseEntity<ApiErrorBackend> negativeTempoMax(NegativeTempoMaxException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorBackend(messageSource.getMessage("errors.level.tempomax.negative", null, locale))
        );
    }

    @ExceptionHandler(ScalataAlreadyExistsException.class)
    public ResponseEntity<ApiErrorBackend> scalataAlreadyExists(ScalataAlreadyExistsException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorBackend(
                        messageSource.getMessage("errors.scalata.exists", null, locale)
                )
        );
    }

    @ExceptionHandler(ScalataNotFoundException.class)
    public ResponseEntity<ApiErrorBackend> scalataNotFound(ScalataNotFoundException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorBackend(
                        messageSource.getMessage("errors.scalata.notfound", null, locale)
                )
        );
    }

    @ExceptionHandler(LevelsSameClassException.class)
    public ResponseEntity<ApiErrorBackend> levelSameClass(LevelsSameClassException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorBackend(
                        messageSource.getMessage("errors.level.sameclass", null, locale)
                )
        );
    }

    @ExceptionHandler(LevelNotFoundException.class)
    public ResponseEntity<ApiErrorBackend> levelNotFound(LevelNotFoundException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorBackend(
                        messageSource.getMessage("errors.level.notfound", null, locale)
                )
        );
    }

}
