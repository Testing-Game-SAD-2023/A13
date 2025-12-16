package com.groom.manvsclass.dto;

import com.groom.manvsclass.model.InteractionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InteractionDTO {

    @NotBlank
    private String className;

    @NotNull
    private InteractionType type;

    @NotBlank
    private String description;
}