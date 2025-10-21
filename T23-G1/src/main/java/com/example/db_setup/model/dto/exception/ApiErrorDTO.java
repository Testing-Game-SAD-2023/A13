package com.example.db_setup.model.dto.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class ApiErrorDTO {
    private List<Map<String, String>> errors;

    public ApiErrorDTO(String field, String message) {
        this.errors = List.of(Map.of("field", field,
                "message", message));
    }

    public ApiErrorDTO(List<Map<String, String>> errors) {
        this.errors = errors;
    }

}
