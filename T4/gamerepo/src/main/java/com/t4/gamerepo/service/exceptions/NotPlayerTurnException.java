package com.t4.gamerepo.service.exceptions;

public class NotPlayerTurnException extends RuntimeException {
    public NotPlayerTurnException(String message) {
        super(message);
    }
}
