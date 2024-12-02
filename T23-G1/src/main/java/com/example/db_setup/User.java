package com.example.db_setup;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    public String biography;

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

    // Relazione per la logica di "Follow"
    @ManyToMany
    @JoinTable(
        name = "Follows",
        schema = "studentsrepo",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    @JsonIgnoreProperties({"followers", "following"})
    public List<User> following;

    @ManyToMany(mappedBy = "following")
    @JsonIgnoreProperties({"followers", "following"})
    public List<User> followers;

    
    @Column(name = "reset_token")
    private String resetToken;
    
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    
    public String getResetToken() {
        return resetToken;
    }

}
