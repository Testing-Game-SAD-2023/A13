package com.example.t9profileservice.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UpdateBioRequest {

    @Size(max = 1000)
    private String bio;
}
