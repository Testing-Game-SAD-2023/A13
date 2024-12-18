package com.groom.manvsclass.model;

public class User {
    private Integer ID;
    private String name;
    private String surname;
    private String email;
    private String password;
    private boolean isRegisteredWithFacebook;
    private boolean isRegisteredWithGoogle;
    private Studies studies;  // Enum
    private String resetToken;

    // Costruttori, getter e setter
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
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

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
}
