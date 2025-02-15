package com.g2.Game.Service.Exceptions;

public class GameAlreadyExistsException extends Exception {
    public GameAlreadyExistsException(String message) {
        super(message);
    }
}