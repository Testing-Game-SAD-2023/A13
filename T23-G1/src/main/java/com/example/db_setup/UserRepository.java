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

package com.example.db_setup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.db_setup.model.User;
import com.example.db_setup.model.UserProfile;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUserProfileEmail(String email);

    @Query("SELECT u FROM UserProfile u WHERE u.email LIKE %:searchTerm%")
    List<User> findByUserProfileEmailLike(@Param("searchTerm") String searchTerm);

    List<User> findByUserProfileName(String name);
    User findByResetToken(String resetToken);
    User findByID(Integer ID);
    //MODIFICA
    User findByisRegisteredWithFacebook(boolean isRegisteredWithFacebook);
    //FINE MODIFICA
    //Modifica 18/06/2024
    User findByisRegisteredWithGoogle(boolean isRegisteredWithGoogle);
    List<User> findAll();
    @Query("SELECT u FROM User u WHERE u.userProfile = :userProfile")
    User findByUserProfile(@Param("userProfile") UserProfile userProfile);
    @Query("SELECT u FROM User u WHERE u.userProfile IN :userProfiles")
    List<User> findUsersByProfiles(@Param("userProfiles") List<UserProfile> userProfiles);
    
    //Modifica 12/12/2024
    List<User> findByUserProfileSurnameAndUserProfileName(String surname,String name); //SELECT * FROM User WHERE surname = ? AND name = ?
    List<User> findByUserProfileSurname(String surname);

}