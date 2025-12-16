package com.groom.manvsclass.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamModificationRequest {

    @NotBlank
    private String oldName;

    @NotBlank
    private String newName;

}
