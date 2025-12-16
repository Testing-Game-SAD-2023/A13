package com.example.t9profileservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Dati base del profilo (nome, cognome, email, nickname, bio).
 */
@Data
public class UpdateUserProfileRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String surname;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 50)
    private String nickname;

    @Size(max = 1000)
    private String bio;
}
