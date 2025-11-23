package com.groom.manvsclass.dto;

import com.groom.manvsclass.dto.SingleScalataDTO;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScalataDTO {

    private String scalataName;
    private String description;
    private List<SingleScalataDTO> singleScalataDTOList;

}