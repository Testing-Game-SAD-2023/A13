package com.groom.manvsclass.dto;

import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.model.SuggestionLevel;
import jakarta.validation.constraints.NotNull;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionDTO extends GuidelineDTO {

    @NotNull(message = "{validation.suggestion.level.mandatory}")
    private SuggestionLevel level;
}