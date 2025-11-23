package com.groom.manvsclass.dto;

import com.groom.manvsclass.model.SuggestionLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SingleSuggestionDTO {

    private String title;
    private String hint;
    private String image;
    private SuggestionLevel level;
}