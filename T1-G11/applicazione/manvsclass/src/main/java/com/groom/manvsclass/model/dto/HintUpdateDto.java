package com.groom.manvsclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HintUpdateDto {

    private String content;

    private String imageUri;

    private Integer order;
}
