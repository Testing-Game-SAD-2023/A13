package com.example.db_setup.model.dto.business;

public record ServiceResponse<T>(boolean created, T entity) {
}
