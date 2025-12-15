package com.groom.manvsclass.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GuidelineDTO {

    @Positive(message = "{validation.guideline.order.positive}")
    private int order;

    @NotBlank(message = "{validation.guideline.hint.mandatory}")
    @Size(max = 255, message = "{validation.guideline.hint.tooLong}")
    private String hint;

    private LocalDate date;
    private String image;
}