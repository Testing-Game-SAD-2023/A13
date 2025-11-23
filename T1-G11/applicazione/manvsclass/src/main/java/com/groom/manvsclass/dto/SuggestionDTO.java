package com.groom.manvsclass.dto;

import com.groom.manvsclass.dto.SingleSuggestionDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionDTO {

    private String className;
    private List<SingleSuggestionDTO> suggestions;
}