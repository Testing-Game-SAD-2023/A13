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

    public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRegisteredWithFacebook() {
		return isRegisteredWithFacebook;
	}

	public void setRegisteredWithFacebook(boolean isRegisteredWithFacebook) {
		this.isRegisteredWithFacebook = isRegisteredWithFacebook;
	}

	public boolean isRegisteredWithGoogle() {
		return isRegisteredWithGoogle;
	}

	public void setRegisteredWithGoogle(boolean isRegisteredWithGoogle) {
		this.isRegisteredWithGoogle = isRegisteredWithGoogle;
	}

	public Studies getStudies() {
		return studies;
	}

	public void setStudies(Studies studies) {
		this.studies = studies;
	}

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
    
    @Column(name = "reset_token")
    private String resetToken;
    
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    
    public String getResetToken() {
        return resetToken;
    }

}
