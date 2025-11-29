package com.groom.manvsclass.dto;

import com.groom.manvsclass.model.InteractionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InteractionDTO {

    private String className;
    private InteractionType type;
    private String description;
}