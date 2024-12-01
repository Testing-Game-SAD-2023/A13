package com.g2.Model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Integer ID;
    private String name;
    private String surname;
    private String email;
    private String password;
    private List<User> following;
    private List<User> followers;
    private boolean isRegisteredWithFacebook;
    private boolean isRegisteredWithGoogle;
    private String studies;
    private String resetToken;

    public User(Integer ID, String name, String surname, String email, String password,
    boolean isRegisteredWithFacebook, boolean isRegisteredWithGoogle, String studies, String resetToken, List<User> following, List<User> followers) {
        this.ID = ID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.following = (following != null) ? following : new ArrayList<>();
        this.followers = (followers != null) ? followers : new ArrayList<>();
        this.isRegisteredWithGoogle = isRegisteredWithGoogle;
        this.isRegisteredWithFacebook = isRegisteredWithFacebook;
        this.studies = studies;
        this.resetToken = resetToken;
    }
    
    // Costruttore vuoto necessario per thymeleaf
    public User() {
        this.following = new ArrayList<>();
        this.followers = new ArrayList<>();
    }


    // Getters and Setters
    public Integer getId() {
        return ID;
    }

    public void setId(Integer ID) {
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

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public List<User> getFollowing(){
        return following;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowers(){
        return followers;
    }

    public boolean getisRegisteredWithGoogle() {
        return isRegisteredWithGoogle;
    }

    public void setRegisteredWithGoogle(boolean isRegisteredWithGoogle) {
        this.isRegisteredWithGoogle = isRegisteredWithGoogle;
    }

    public boolean getisRegisteredWithFacebook() {
        return isRegisteredWithFacebook;
    }

    public void setRegisteredWithFacebook(boolean isRegisteredWithFacebook) {
        this.isRegisteredWithFacebook = isRegisteredWithFacebook;
    }

    public String getStudies() {
        return studies;
    }

    public void setStudies(String studies) {
        this.studies = studies;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    @Override
    public String toString() {
    return "User{" +
            "ID=" + ID +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", following=" + (following != null ? following.size() + " users" : "null") +
            ", followers=" + (followers != null ? followers.size() + " users" : "null") +
            ", isRegisteredWithFacebook=" + isRegisteredWithFacebook +
            ", isRegisteredWithGoogle=" + isRegisteredWithGoogle +
            ", studies='" + studies + '\'' +
            ", resetToken='" + resetToken + '\'' +
            '}';
}


}