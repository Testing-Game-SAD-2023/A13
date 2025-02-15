package com.g2.Game.Service.Exceptions;

public class GameDontExistsException extends Exception {
        public GameDontExistsException(String message) {
            super(message);
        }
}