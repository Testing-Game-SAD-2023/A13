package com.t4.gamerepo.controller.advices;

import com.t4.gamerepo.service.exceptions.*;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import testrobotchallenge.commons.models.dto.api.ApiErrorBackend;

import java.util.Locale;


@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ApiErrorBackend> gameNotFound(GameNotFoundException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorBackend(messageSource.getMessage("errors.game.notFound", null, locale)));
    }

    @ExceptionHandler(PlayerNotInGameException.class)
    public ResponseEntity<ApiErrorBackend> playerNotInGame(GameNotFoundException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorBackend(messageSource.getMessage("errors.players.notFoundInGame", null, locale)));
    }

    @ExceptionHandler(DuplicatedPlayersInGameException.class)
    public ResponseEntity<ApiErrorBackend> duplicatedPlayersInGame(DuplicatedPlayersInGameException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorBackend(messageSource.getMessage("errors.players.duplicated", null, locale)));
    }

    @ExceptionHandler(FoundRoundNotClosedException.class)
    public ResponseEntity<ApiErrorBackend> foundRoundNotClosed(FoundRoundNotClosedException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorBackend(messageSource.getMessage("errors.rounds.foundOpen", null, locale)));
    }

    @ExceptionHandler(GameAlreadyClosedException.class)
    public ResponseEntity<ApiErrorBackend> gameAlreadyClosed(GameAlreadyClosedException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorBackend(messageSource.getMessage("errors.games.alreadyClosed", null, locale)));
    }

    @ExceptionHandler(RoundAlreadyClosedException.class)
    public ResponseEntity<ApiErrorBackend> foundRoundNotClosed(RoundAlreadyClosedException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorBackend(messageSource.getMessage("errors.rounds.alreadyClosed", null, locale)));
    }

    @ExceptionHandler(TurnAlreadyClosedException.class)
    public ResponseEntity<ApiErrorBackend> turnAlreadyClosed(TurnAlreadyClosedException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorBackend(messageSource.getMessage("errors.turns.alreadyClosed", null, locale)));
    }

    @ExceptionHandler(NotPlayerTurnException.class)
    public ResponseEntity<ApiErrorBackend> notPlayerTurn(NotPlayerTurnException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorBackend(messageSource.getMessage("errors.players.notInTurn", null, locale)));
    }

    @ExceptionHandler(RoundNotFoundException.class)
    public ResponseEntity<ApiErrorBackend> roundNotFound(RoundNotFoundException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorBackend(messageSource.getMessage("errors.rounds.notFound", null, locale)));
    }

    @ExceptionHandler(TurnNotFoundException.class)
    public ResponseEntity<ApiErrorBackend> turnNotFound(RoundNotFoundException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorBackend(messageSource.getMessage("errors.turns.notFound", null, locale)));
    }
}
