package com.example.db_setup.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSearchProfileDTO {
    private Integer id;           // ID del profilo UserProfile
    private Integer userId;       // ID dell'utente Player
    private String name;
    private String surname;
    private String email;
    private String profilePicturePath;
    private boolean following;

    public UserSearchProfileDTO(Integer id, Integer userId, String name, String surname,
                                String email, String profilePicturePath,
                                boolean following) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.profilePicturePath = profilePicturePath;
        this.following = following;
    }

    public UserSearchProfileDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfilePicturePath() { return profilePicturePath; }
    public void setProfilePicturePath(String profilePicturePath) { this.profilePicturePath = profilePicturePath; }

    public boolean isFollowing() { return following; }
    public void setFollowing(boolean following) { this.following = following; }
}
