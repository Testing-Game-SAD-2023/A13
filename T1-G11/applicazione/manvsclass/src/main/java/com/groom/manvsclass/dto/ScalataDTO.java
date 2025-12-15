package com.groom.manvsclass.dto;

import com.groom.manvsclass.dto.ClassUTScalataDTO;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScalataDTO {

    @NotBlank
    private String scalataName;

    @NotBlank
    private String description;

    @Valid
    @NotEmpty
    @Size(min = 2, max = 10)
    private List<ClassUTScalataDTO> classUTScalataDTOs;

}