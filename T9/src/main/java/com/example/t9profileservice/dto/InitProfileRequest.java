package com.example.t9profileservice.dto;

import lombok.Data;

/**
 * Dati che T23 può inviare a T9 in fase di registrazione
 * per inizializzare il profilo con valori reali.
 */
@Data
public class InitProfileRequest {
    private String name;
    private String surname;
    private String email;
    private String nickname;
}
