package com.groom.manvsclass.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SingleScalataDTO {

    private String className;
    private int level;
    private int timeLimit;

}