package com.groom.manvsclass.dto;

import jakarta.validation.constraints.NotBlank;
import com.groom.manvsclass.validation.ValidOrder;
import jakarta.validation.Valid;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ClassUTSuggestionDTO {

    @NotBlank
    private String className;

    @Valid
    @ValidOrder
    private List<SuggestionDTO> suggestions;
}