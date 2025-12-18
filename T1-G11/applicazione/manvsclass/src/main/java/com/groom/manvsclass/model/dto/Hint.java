package com.groom.manvsclass.model.dto;

import com.groom.manvsclass.model.enums.HintTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hint {

    private String classUTName;

    @NotNull
    private String name;

    @NotNull
    private String content;

    private String imageUri;

    @NotNull
    private HintTypeEnum type;

    private Integer order;

}
