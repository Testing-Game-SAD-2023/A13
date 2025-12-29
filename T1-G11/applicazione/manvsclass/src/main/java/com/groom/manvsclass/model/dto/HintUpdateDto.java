package com.groom.manvsclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HintUpdateDto {

    private String content;

    private String imageUri;

    private Integer order;
}
