package com.g2.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile {

    @JsonProperty("id")
    private Integer ID;
    @JsonProperty("bio")
    private String bio;
    @JsonProperty("profilePicturePath")
    private String profilePicturePath;
    @JsonProperty("followingList")
    private List<UserProfile> followingList;
    @JsonProperty("followersList")
    private List<UserProfile> followersList;

    public UserProfile(Integer ID, String bio, String profilePicturePath, List<UserProfile> followingList, List<UserProfile> followersList) {
        this.ID = ID;
        this.bio = bio;
        this.profilePicturePath = profilePicturePath;
        this.followingList = followingList;
        this.followersList = followersList;
    }


    //Costruttore vuoto necessario per thymeleaf
    public UserProfile(){}

    // Getters and Setters
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public List<UserProfile> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<UserProfile> followingList) {
        this.followingList = followingList;
    }

    public List<UserProfile> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(List<UserProfile> followersList) {
        this.followersList = followersList;
    }

    @Override
    public String toString(){
        return "UserProfile{" +
                "ID=" + ID +
                ", bio='" + bio + '\'' +
                ", profilePicturePath='" + profilePicturePath + '\'' +
                ", followingList=" + followingList +
                ", followersList=" + followersList +
                '}';
    }
}


