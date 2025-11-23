package com.groom.manvsclass.exception;

public class OpponentNotFoundException extends RuntimeException {
    private final String field;

    public OpponentNotFoundException(String field) {
        this.field = field;
    }

    public OpponentNotFoundException() {
        this.field = "none";
    }
}
