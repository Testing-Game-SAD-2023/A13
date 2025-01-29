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
    @JsonProperty("followingIds")
    private List<Integer> followingIds;
    @JsonProperty("followerIds")
    private List<Integer> followerIds;

    public UserProfile(Integer ID, String bio, String profilePicturePath, List<Integer> followingIds, List<Integer> followerIds) {
        this.ID = ID;
        this.bio = bio;
        this.profilePicturePath = profilePicturePath;
        this.followingIds = followingIds;
        this.followerIds = followerIds;
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

    public List<Integer> getFollowingList() {
        return followingIds;
    }

    public void setFollowingList(List<Integer> followingIds) {
        this.followingIds = followingIds;
    }

    public List<Integer> getFollowersList() {
        return followerIds;
    }

    public void setFollowersList(List<Integer> followerIds) {
        this.followerIds = followerIds;
    }

    @Override
    public String toString(){
        return "UserProfile{" +
                "ID=" + ID +
                ", bio='" + bio + '\'' +
                ", profilePicturePath='" + profilePicturePath + '\'' +
                ", followingList=" + followingIds +
                ", followersList=" + followerIds +
                '}';
    }
}


