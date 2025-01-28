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
package com.example.db_setup.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.db_setup.OAuthUserGoogle;
import com.example.db_setup.User;
import com.example.db_setup.UserProfile;
import com.example.db_setup.UserProfileRepository;
import com.example.db_setup.UserRepository;
import com.example.db_setup.Authentication.AuthenticatedUser;
import com.example.db_setup.Authentication.AuthenticatedUserRepository;

//import com.T8.social.temp.Authentication.AuthenticatedUser;
//import com.T8.social.temp.Authentication.AuthenticatedUserRepository;
//import com.T8.social.temp.User;
//import com.T8.social.temp.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Questa classe è un servizio che gestisce le operazioni relative agli utenti
// è usato per creare un nuovo utente, recuperare un utente esistente e generare un token JWT per l'utente
@Service
public class UserService {

    // Usa la dipendenza UserRepository per accedere ai dati dell'utente sul DB
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private UserProfile userProfile;

    @Autowired
    private NotificationService notificationService;
    // Stessa cosa di sopra
    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;

    // Recupera dal DB l'utente con l'email specificata
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Crea un nuovo utente con i dettagli forniti da OAuthUserGoogle, recuperati dall'accesso OAuth2
    // e lo salva nel DB
    public User createUserFromOAuth(OAuthUserGoogle oauthUser) {
        User newUser = new User();
        newUser.setEmail(oauthUser.getEmail());
        newUser.setName(oauthUser.getName());
        //Istanzio il profilo
        newUser.setUserProfile(userProfile);
        newUser.getUserProfile().setUser(newUser);

        newUser.setRegisteredWithGoogle(true);
        String[] nameParts = oauthUser.getName().split(" ");
        if (nameParts.length > 1) {
            newUser.setSurname(nameParts[nameParts.length - 1]);
        }
        // Set other user properties as needed

        return userRepository.save(newUser);
    }

    public UserProfile findProfileByEmail(String email) {
        // Recupera l'utente con l'email specificata
        User user = userRepository.findByEmail(email);

        //Controlla se l'utente esiste
        if (user == null) {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }

        // Restituisce il profilo dell'utente
        return user.getUserProfile();
    }

    // Genera un token JWT per l'utente specificato e lo salva nel DB
    public String saveToken(User user) {
        // Genera un token JWT per l'utente
        String token = generateToken(user);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, token);
        authenticatedUserRepository.save(authenticatedUser);
        return token;
    }

    // Genera un token JWT per l'utente specificato, forse si deve cambiare
    public static String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);
        // usa per generare il token email, data di creazione, data di scadenza, ID utente e ruolo
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .claim("userId", user.getID())
                .claim("role", "user")
                .signWith(SignatureAlgorithm.HS256, "mySecretKey")
                .compact();

        return token;
    }

    public void saveProfile(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("Profile not found");
        }
        userProfileRepository.save(userProfile);
    }

    //Modifica 04/12/2024 Giuleppe
    public ResponseEntity<?> getStudentiTeam(List<String> idUtenti) {
        System.out.println("Inizio metodo getStudentiTeam. ID ricevuti: " + idUtenti);

        // Controlla se la lista di ID è vuota
        if (idUtenti == null || idUtenti.isEmpty()) {
            System.out.println("La lista degli ID è vuota.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lista degli ID vuota.");
        }

        try {
            // Converte gli ID in interi
            List<Integer> idIntegerList = idUtenti.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            // Recupera gli utenti dal database
            List<User> utenti = userRepository.findAllById(idIntegerList);

            // Verifica se sono stati trovati utenti
            if (utenti == null || utenti.isEmpty()) {
                System.out.println("Nessun utente trovato per gli ID forniti.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nessun utente trovato.");
            }

            System.out.println("Utenti trovati: " + utenti);

            // Restituisce la lista di utenti trovati
            return ResponseEntity.ok(utenti);

        } catch (NumberFormatException e) {
            System.out.println("Errore durante la conversione degli ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Formato degli ID non valido. Devono essere numeri interi.");
        } catch (Exception e) {
            System.out.println("Errore durante il recupero degli utenti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore interno del server.");
        }
    }

    @Transactional
    public ResponseEntity<?> toggleFollow(String UserId, String AuthUserId) {
        try {
            //Converto gli id in interi
            Integer userId = Integer.parseInt(UserId);
            Integer authUserId = Integer.parseInt(AuthUserId);
            // Recupero gli utenti dal db
            User autUser = userRepository.findById(authUserId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            User followUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            //Recupera i profili dal db
            Integer autUserProfileId = autUser.getUserProfile().getID();
            Integer followUserProfileId = followUser.getUserProfile().getID();
            //Controlla se l'utente è già seguito
            // Controllo nella li
            boolean wasFollowing = autUser.getUserProfile().getFollowingIds().stream().anyMatch(u -> u.equals(followUserProfileId));
            //Se l'utente è già seguito, lo rimuove dalla lista dei follower
            if (wasFollowing) {
                //Unfollow
                followUser.getUserProfile().getFollowerIds().remove(autUserProfileId);
                autUser.getUserProfile().getFollowingIds().remove(followUserProfileId);
            } else {
                //Altrimenti lo aggiunge - Follow
                followUser.getUserProfile().getFollowerIds().add(autUserProfileId);
                //userProfile.getFollowerIds().add(authUserProfile);
                autUser.getUserProfile().getFollowingIds().add(followUserProfileId);
                String titolo = "Hai un nuovo follower";
                String messaggio = autUser.name + " " + autUser.surname + " ha iniziato a seguirti!";
                notificationService.saveNotification((int) userId, titolo, messaggio);
            }
            //Salva le modifiche
            userProfileRepository.save(autUser.getUserProfile());
            userProfileRepository.save(followUser.getUserProfile());
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Follow status changed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal server error"));
        }
    }

    public List<User> getFollowers(String userId) {
        Integer userId_int = Integer.parseInt(userId);
        User user = userRepository.findById(userId_int)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    
        List<Integer> followerIds = user.getUserProfile().getFollowerIds();
        if (followerIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<UserProfile> followerProfiles = userProfileRepository.findAllById(followerIds);
        return userRepository.findUsersByProfiles(followerProfiles);
    }
    
    public List<User> getFollowing(String userId) {
        Integer userId_int = Integer.parseInt(userId);
        User user = userRepository.findById(userId_int)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Integer> followingIds = user.getUserProfile().getFollowingIds();
        if (followingIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<UserProfile> followingProfiles = userProfileRepository.findAllById(followingIds);
        return userRepository.findUsersByProfiles(followingProfiles);
    }
}
