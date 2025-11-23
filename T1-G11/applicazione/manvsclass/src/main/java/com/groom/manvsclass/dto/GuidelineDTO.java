package com.groom.manvsclass.dto;

import com.groom.manvsclass.model.SuggestionLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GuidelineDTO {

    private List<SingleGuidelineDTO> guidelines;
}