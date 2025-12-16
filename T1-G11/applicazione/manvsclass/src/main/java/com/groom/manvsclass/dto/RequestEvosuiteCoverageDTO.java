package com.groom.manvsclass.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestEvosuiteCoverageDTO {
    @JsonProperty("classUTName")
    private String classUTName;

    @JsonProperty("classUTPackage")
    private String classUTPackage;

}
