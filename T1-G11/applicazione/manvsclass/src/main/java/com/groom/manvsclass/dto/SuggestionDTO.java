package com.groom.manvsclass.dto;

import com.groom.manvsclass.model.SuggestionLevel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SuggestionDTO extends GuidelineDTO {

    private SuggestionLevel level;
}