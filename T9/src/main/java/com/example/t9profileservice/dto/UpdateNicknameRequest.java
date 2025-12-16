package com.example.t9profileservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateNicknameRequest {

    @NotBlank
    @Size(max = 50)
    private String nickname;
}
