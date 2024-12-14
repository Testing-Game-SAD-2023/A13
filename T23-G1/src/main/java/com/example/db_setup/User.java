package com.example.db_setup;

import javax.persistence.*;
import lombok.Data;

//Updated by Gabman 09/12
@Table(name = "students", schema = "studentsrepo")
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;

    @Column(name = "name", nullable = false, length = 100) // Nome non nullo, lunghezza massima 100 caratteri
    private String name;

    @Column(name = "surname", nullable = false, length = 100) // Cognome non nullo, lunghezza massima 100 caratteri
    private String surname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    // MODIFICA: Registrazione via social
    public boolean isRegisteredWithFacebook;

    public boolean isRegisteredWithGoogle;

    // Campo per gli studi, se applicabile
    @Enumerated(EnumType.STRING)
    public Studies studies;

    // Nickname univoco
    @Column(name = "nickname", unique = true, nullable = false, length = 50)
    private String nickname;

    // Biografia con lunghezza limitata
    @Column(name = "biography", length = 500)
    private String biography;

    // Percorso o URL per l'immagine avatar
    @Column(name = "avatar", nullable = true, length = 255)
    private String avatar;

    @Lob
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    // Token per il reset della password
    @Column(name = "reset_token")
    private String resetToken;

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    // Getter e Setter per i campi aggiornabili
    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
