package com.groom.manvsclass.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Admin")
public class Admin {
    private String nome;
    private String cognome;
    private String username;

    //MODIFICA (15/02/2024) : Aggiunta campo email
    @Id
    private String email;

    //MODIFICA (15/02/2024) : Aggiunta campo resetToken
    private String resetToken;

    //MODIFICA (16/02/2024) : Aggiunta campo invitationToken
    private String invitationToken;

    //FINE MODIFICA (15/02/2024)
    private String password;


    public Admin(String nome, String cognome, String username, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter e setter per il campo resetToken
    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    //Getter e setter per il campo invitationToken
    public String getInvitationToken() {
        return invitationToken;
    }

    public void setInvitationToken(String invitationToken) {
        this.invitationToken = invitationToken;
    }


    @Override
    public String toString() {
        return "Admin{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", resetToken='" + resetToken + '\'' +
                ", invitationToken='" + invitationToken + '\'' +
                '}';
    }
}