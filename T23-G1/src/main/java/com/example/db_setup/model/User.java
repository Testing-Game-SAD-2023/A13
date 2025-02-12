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

package com.example.db_setup.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Table (name = "Students", schema = "studentsrepo")
@Data
@Entity
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer ID;
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;


    @Column(name = "reset_token")
    private String resetToken;

    public User(){
        this.userProfile=new UserProfile();
        this.userProfile.setUser(this);
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void getUserProfile(UserProfile userProfile){
        this.userProfile = userProfile;
    }

    public Integer getID(){
        return this.ID;
    }

    public String getName(){
        return this.userProfile.getName();
    }

    public void setName(String name){
        this.userProfile.setName(name);
    }

    public String getSurname(){
        return this.userProfile.getSurname();
    }

    public void setSurname(String surname){
        this.userProfile.setSurname(surname);
    }

    public String getNickname(){
        return this.userProfile.getNickname();
    }

    public void setNickname(String nickname){
        this.userProfile.setNickname(nickname);
    }

    public String getEmail(){
        return this.userProfile.getEmail();
    }

    public void setEmail(String email){
        this.userProfile.setEmail(email);
    }
}
