package com.groom.manvsclass.dto;

import com.groom.manvsclass.model.SuggestionLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionResponseDTO {

    private Long id;
    private String title;
    private String hint;
    private byte[] image;
    private SuggestionLevel level;
    private LocalDate date;
}