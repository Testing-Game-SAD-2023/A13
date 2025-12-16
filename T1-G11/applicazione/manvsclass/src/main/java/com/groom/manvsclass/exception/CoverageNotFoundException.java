package com.groom.manvsclass.exception;

public class CoverageNotFoundException extends RuntimeException {
    private final String field;

    public CoverageNotFoundException(String field) {
        this.field = field;
    }

    public CoverageNotFoundException() {
        this.field = "none";
    }
}
