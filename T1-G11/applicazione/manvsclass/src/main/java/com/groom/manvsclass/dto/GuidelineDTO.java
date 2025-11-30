package com.groom.manvsclass.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuidelineDTO {

    @NotBlank(message = "Il titolo è obbligatorio")
    @Size(max = 100, message = "Il titolo è troppo lungo")
    private String title;

    @NotBlank(message = "L'hint è obbligatorio")
    @Size(max = 255, message = "L'hint è troppo lungo")
    private String hint;

    private String image;
}