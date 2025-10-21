package com.robotchallenge.t8.service.exception;

public class ProcessingExceptionWrapper extends RuntimeException {
    public ProcessingExceptionWrapper(String message, Throwable cause) {
        super(message, cause);
    }
}
