package com.groom.manvsclass.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @Min(value = 2)
    private int numStudents;
}