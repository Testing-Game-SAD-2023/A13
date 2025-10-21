/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile {

    // ID del oggetto UserProfile
    @JsonProperty("id")
    private Integer id;
    // ID dell'oggetto USER
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("bio")
    private String bio;
    @JsonProperty("profilePicturePath")
    private String profilePicturePath;
    @JsonProperty("name")
    private String name;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("nickname")
    private String nickname;

    @JsonCreator
    public UserProfile(
            @JsonProperty("id") Integer id,
            @JsonProperty("userId") Integer userId,
            @JsonProperty("bio") String bio,
            @JsonProperty("profilePicturePath") String profilePicturePath,
            @JsonProperty("name") String name,
            @JsonProperty("surname") String surname,
            @JsonProperty("nickname") String nickname) {
        this.id = id;
        this.userId = userId;
        this.bio = bio;
        this.profilePicturePath = profilePicturePath;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
    }


    //Costruttore vuoto necessario per thymeleaf
    public UserProfile() {
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "UserProfile{" +
                "ID=" + id +
                ", bio='" + bio + '\'' +
                ", profilePicturePath='" + profilePicturePath + '\'' +
                '}';
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public Integer getuserId() {
        return userId;
    }

    public void setuserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}


