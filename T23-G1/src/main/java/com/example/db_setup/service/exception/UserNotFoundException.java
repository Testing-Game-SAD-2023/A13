
package com.example.db_setup.service.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final String field;

    public UserNotFoundException(String field) {
        this.field = field;
    }

    public UserNotFoundException() {
        this.field = "none";
    }
}