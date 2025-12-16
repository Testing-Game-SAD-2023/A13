package com.example.t9profileservice.dto;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Aggiorna le preferenze grafiche e di visibilità del profilo.
 */
@Data
public class UpdatePreferencesRequest {

    @Size(max = 20)
    private String themeColor;

    @Size(max = 20)
    private String accentColor;

    @Size(max = 10)
    private String language;

    private Boolean publicProfile;
}
