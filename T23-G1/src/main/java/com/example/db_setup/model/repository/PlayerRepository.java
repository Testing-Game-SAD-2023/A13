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

package com.example.db_setup.model.repository;

import com.example.db_setup.model.Player;
import com.example.db_setup.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByUserProfileEmail(String email);

    @Query("SELECT u FROM UserProfile u WHERE u.email LIKE %:searchTerm%")
    List<Player> findByUserProfileEmailLike(@Param("searchTerm") String searchTerm);

    List<Player> findByUserProfileName(String name);

    //MODIFICA
    List<Player> findAll();

    @Query("SELECT u FROM Player u WHERE u.userProfile = :userProfile")
    Player findByUserProfile(@Param("userProfile") UserProfile userProfile);

    @Query("SELECT u FROM Player u WHERE u.userProfile IN :userProfiles")
    List<Player> findUsersByProfiles(@Param("userProfiles") List<UserProfile> userProfiles);

    //Modifica 12/12/2024
    List<Player> findByUserProfileSurnameAndUserProfileName(String surname, String name); //SELECT * FROM User WHERE surname = ? AND name = ?

    List<Player> findByUserProfileSurname(String surname);
}