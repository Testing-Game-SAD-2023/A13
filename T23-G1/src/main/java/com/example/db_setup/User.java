
package com.example.db_setup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table (name = "Students", schema = "studentsrepo")
@Data 
@Entity
public class User {
 
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    public Integer ID;

    public String name;

    public String surname;
    
    public String email;

    public String password;

    //MODIFICA
    public boolean isRegisteredWithFacebook;
    //FINE MODIFICA
    //MODIFICA 18/06/2024
    public boolean isRegisteredWithGoogle;
    
    @Enumerated (EnumType.STRING)
    public Studies studies;

    /* Informazioni Personali utente da aggiungere per il profilo
    public String bio;
    public List<User> friendsList;
    public String profilePicturePath; -> questa potrebbe essere un percorso in un volume che contiene tutte le propic (T23)
    */
    
    // INIZIO MODIFICHE cami - Campi aggiuntivi per la gestione delle informazioni profilo
    @Column(name = "biography", length = 500)
    private String biography; // Campo per la descrizione personale

    @Column(name = "avatar")
    private String avatar; // Campo per il percorso dell'immagine profilo o URL
    // FINE MODIFICHE

    @Column(name = "reset_token")
    private String resetToken;
    
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    
    public String getResetToken() {
        return resetToken;
    }

     // INIZIO MODIFICHE - Getter e Setter per i nuovi campi
    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    // FINE MODIFICHE

}
