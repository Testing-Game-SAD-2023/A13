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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class User {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("isRegisteredWithFacebook")
    private boolean isRegisteredWithFacebook;
    @JsonProperty("studies")
    private String studies;
    @JsonProperty("userProfile")
    private UserProfile userProfile;
    @JsonProperty("resetToken")
    private String resetToken;
    @JsonProperty("name")
    private String name;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("nickname")
    private String nickname;

    public User(Long id, String email, String password,
                boolean isRegisteredWithFacebook, String studies, UserProfile userProfile, String resetToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isRegisteredWithFacebook = isRegisteredWithFacebook;
        this.studies = studies;
        this.userProfile = userProfile;
        this.resetToken = resetToken;
    }

    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isRegisteredWithFacebook=" + isRegisteredWithFacebook +
                ", studies='" + studies + '\'' +
                ", userProfile=" + userProfile +
                ", resetToken='" + resetToken + '\'' +
                '}';

    }
}