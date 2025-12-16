package com.groom.manvsclass.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassUTScalataDTO {

    @NotBlank
    private String className;

    @Min(1)
    @Max(10)
    private int level;

    @Min(1)
    private int timeLimit;

}