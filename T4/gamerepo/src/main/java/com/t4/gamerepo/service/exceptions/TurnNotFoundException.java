package com.t4.gamerepo.service.exceptions;

public class TurnNotFoundException extends RuntimeException {
    public TurnNotFoundException(String message) {
        super(message);
    }
}
