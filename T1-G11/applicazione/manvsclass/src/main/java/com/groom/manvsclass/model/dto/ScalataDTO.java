package com.groom.manvsclass.model.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScalataDTO {

    private String date;
    private String username;
    private String name;
    private String description;
    private int numberOfLevels;
    private List<LevelDTO> listOfLevels;
}
