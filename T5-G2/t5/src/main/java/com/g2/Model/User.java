package com.g2.Model;


public class User {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private boolean isRegisteredWithFacebook;
    private String studies;
    private String resetToken;
    
    //modifiche cami
    private String avatar;
    private String biography;
    private String nickname;  
    //fine modifiche

    public User(Long id, String name, String surname, String email, String password,
    boolean isRegisteredWithFacebook, String studies, String resetToken) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.isRegisteredWithFacebook = isRegisteredWithFacebook;
        this.studies = studies;
        this.resetToken = resetToken;
        //cami
        this.biography = "";
        this.avatar="" ;
        this.nickname = "";
        //fine
        
    }
    
    //Costruttore vuoto necessario per thymeleaf
    public User(){}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    //modifiche cami
    public String getAvatar() {
    return avatar;
    }

    public void setAvatar(String avatar) {
    this.avatar = avatar;
    }
   
    public String getBiography() { // Getter per biography
        return biography;
    }
    public void setBiography(String biography) { // Setter per biography
        this.biography = biography;
    }
    //fine

    public boolean isRegisteredWithFacebook() {
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
    //cami (02/12)
       public String getNickname() { // Getter per nickname
        return nickname;
    }

    public void setNickname(String nickname) { // Setter per nickname
        this.nickname = nickname;
    }
}