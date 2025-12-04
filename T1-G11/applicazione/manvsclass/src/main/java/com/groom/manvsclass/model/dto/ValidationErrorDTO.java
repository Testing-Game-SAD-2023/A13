package com.groom.manvsclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Error response structure for validation errors with multiple field errors.
 * Provides a consistent format for validation error messages to the frontend.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> errors;

    public ValidationErrorDTO(int status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = "Validation Failed";
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public void addFieldError(String field, String message) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new FieldError(field, message));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }
}
