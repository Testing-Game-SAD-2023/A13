package com.example.t9profileservice.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "profiles", schema = "studentsrepo")
@Data
@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // --------------------------
    // DATI BASE DEL PROFILO
    // --------------------------

    @Column(nullable = false, length = 30)
    private String name;          // Nome reale

    @Column(nullable = false, length = 30)
    private String surname;       // Cognome

    @Column(nullable = true, length = 45)
    private String email;         // Email principale dell'utente (da T23)

    @Column(nullable = false, length = 30)
    private String nickname;      // Nome visualizzato / nickname

    @Column(length = 500)
    private String bio;           // Biografia testuale

    // --------------------------
    // AVATAR / IMMAGINE PROFILO
    // --------------------------

    @Column(name = "profile_picture_path", length = 255)
    private String profilePicturePath;  // URL o path dell’immagine avatar

    // --------------------------
    // PERSONALIZZAZIONE PROFILO
    // --------------------------

    @Column(name = "theme_color", length = 20)
    private String themeColor;    // es: "blue", "dark", "#FFAA00"

    @Column(name = "accent_color", length = 20)
    private String accentColor;   // colore secondario

    @Column(name = "language", length = 10)
    private String language;      // es: "it", "en"

    @Column(name = "public_profile")
    private Boolean publicProfile = Boolean.TRUE; // profilo pubblico o privato

    // --------------------------
    // COLLEGAMENTI A PLAYER / USER
    // --------------------------

    // Colonna già usata da T23, FK verso players.id
    @Column(name = "player_id")
    private Long playerId;

    // Colonna usata da T9 per mappare l'utente (stesso valore di players.id)
    @Column(name = "user_id", unique = true)
    private Long userId;
}
