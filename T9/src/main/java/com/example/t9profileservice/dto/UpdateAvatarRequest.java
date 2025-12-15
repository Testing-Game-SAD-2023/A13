package com.example.t9profileservice.dto;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Aggiorna solo l'avatar (path dell'immagine).
 */
@Data
public class UpdateAvatarRequest {

    @Size(max = 255)
    private String profilePicturePath;
}
