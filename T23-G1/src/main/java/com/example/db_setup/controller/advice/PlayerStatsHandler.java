package com.example.db_setup.controller.advice;

import com.example.db_setup.model.dto.exception.ApiErrorDTO;
import com.example.db_setup.service.exception.GameProgressNotFoundException;
import com.example.db_setup.service.exception.PlayerProgressNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class PlayerStatsHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(PlayerProgressNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handlePlayerProgressNotFoundException(PlayerProgressNotFoundException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorDTO(e.getField(), messageSource.getMessage("stats.playerProgress.notFound", null, locale))
        );
    }

    @ExceptionHandler(GameProgressNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleGameProgressNotFoundException(GameProgressNotFoundException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorDTO(e.getField(), messageSource.getMessage("stats.gameProgress.notFound", null, locale))
        );
    }
}
