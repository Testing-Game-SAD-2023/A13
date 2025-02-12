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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.db_setup.Authentication.AuthenticatedUser;
import com.example.db_setup.Authentication.AuthenticatedUserRepository;
import com.example.db_setup.OAuthUserGoogle;
import com.example.db_setup.UserFollowRepository;
import com.example.db_setup.UserProfileRepository;
import com.example.db_setup.UserRepository;
import com.example.db_setup.model.User;
import com.example.db_setup.model.UserProfile;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Questa classe è un servizio che gestisce le operazioni relative agli utenti
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserFollowRepository userFollowRepository;

    private UserProfile userProfile;

    @Autowired
    private NotificationService notificationService;
    // Stessa cosa di sopra
    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Recupera dal DB l'utente con l'email specificata
    public User getUserByEmail(String email) {
        return userRepository.findByUserProfileEmail(email);
    }

    public User getUserByID(Integer ID) {
        return userRepository.findByID(ID);
    }

    public List<User> GetUserListByEmail(String email) {
        return userRepository.findByUserProfileEmailLike(email);
    }

    // Modifica 06/12/2024: Aggiunta end-point per restituire solo i campi non sensibili dello USER
    public ResponseEntity<?> getStudentByEmail(String email) {
        User user = userRepository.findByUserProfileEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato per email: " + email);
        }

        // Creazione della mappa JSON con i campi desiderati
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getID());
        response.put("name", user.getName());
        response.put("surname", user.getSurname());
        response.put("email", user.getEmail());
        return ResponseEntity.ok(response);
    }

    // Crea un nuovo utente con i dettagli forniti da OAuthUserGoogle, recuperati dall'accesso OAuth2
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
        return userRepository.save(newUser);
    }

    public UserProfile findProfileByEmail(String email) {
        // Recupera l'utente con l'email specificata
        User user = userRepository.findByUserProfileEmail(email);

        //Controlla se l'utente esiste
        if (user == null) {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }

        // Restituisce il profilo dell'utente
        return user.getUserProfile();
    }

    // Genera un token JWT per l'utente specificato e lo salva nel DB
    public String saveToken(User user) {
        String token = generateToken(user);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, token);
        authenticatedUserRepository.save(authenticatedUser);
        return token;
    }

    // Genera un token JWT per l'utente specificato
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

    // Modifica 04/12/2024 - Aggiunta gestione ID studenti
    public ResponseEntity<?> getStudentsByIds(List<String> idUtenti) {
        System.out.println("Inizio metodo getStudentsByIds. ID ricevuti: " + idUtenti);

        // Controlla se la lista di ID è vuota
        if (idUtenti == null || idUtenti.isEmpty()) {
            System.out.println("La lista degli ID è vuota.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lista degli ID vuota.");
        }

        try {
            // Converte gli ID in interi (utilizzando Collectors.toList() invece di toList())
            List<Integer> idIntegerList = idUtenti.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList()); // Utilizzo di Collectors.toList()

            // Recupera gli utenti dal database
            List<User> utenti = userRepository.findAllById(idIntegerList);

            // Verifica se sono stati trovati utenti
            if (utenti == null || utenti.isEmpty()) {
                System.out.println("Nessun utente trovato per gli ID forniti.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nessun utente trovato.");
            }

            System.out.println("Utenti trovati: " + utenti);

            // Mappa i dati degli utenti nei campi desiderati
            List<Map<String, Object>> response = utenti.stream().map(user -> {
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("id", user.getID());
                jsonMap.put("name", user.getName());
                jsonMap.put("surname", user.getSurname());
                jsonMap.put("email", user.getEmail());
                return jsonMap;
            }).collect(Collectors.toList()); // Utilizzo di Collectors.toList() per raccogliere i risultati

            // Restituisce la lista di utenti filtrati
            return ResponseEntity.ok(response);

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

    //Modifica 12/12/2024
    public List<Map<String, Object>> getStudentsBySurnameAndName(Map<String, String> request) {
        String surname = request.get("surname");
        String name = request.get("name");
        List<User> users = new ArrayList<>();

        // Verifica se surname è "null" o vuoto
        if (isNullOrEmpty(surname) && !isNullOrEmpty(name)) {
            users = userRepository.findByUserProfileName(name);
        } // Verifica se name è "null" o vuoto
        else if (!isNullOrEmpty(surname) && isNullOrEmpty(name)) {
            users = userRepository.findByUserProfileSurname(surname);
        } // Se entrambi i parametri sono forniti e non vuoti
        else if (!isNullOrEmpty(surname) && !isNullOrEmpty(name)) {
            users = userRepository.findByUserProfileSurnameAndUserProfileName(surname, name);
        } // Gestisci caso in cui entrambi i parametri sono nulli o vuoti
        else {
            // Ad esempio, puoi restituire tutti gli utenti se entrambi i parametri sono nulli o vuoti
            users = userRepository.findAll();
        }

        // Restituisci la lista degli utenti mappati in formato JSON
        return mapUsersToResponseList(users);
    }

    // Metodo di utilità per verificare se una stringa è null o vuota
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Metodo di utilità per mappare una lista di utenti in una lista di mappe
    private List<Map<String, Object>> mapUsersToResponseList(List<User> users) {
        List<Map<String, Object>> responseList = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getID());
            response.put("name", user.getName());
            response.put("surname", user.getSurname());
            response.put("email", user.getEmail());
            responseList.add(response);
        }
        return responseList;
    }

    public List<Map<String, Object>> searchStudents(Map<String, String> request) {
        String email = request.get("email");
        String surname = request.get("surname");
        String name = request.get("name");

        List<Map<String, Object>> responseList = new ArrayList<>();

        // Caso 1: Cerca per email
        if (email != null && !email.isEmpty()) {
            ResponseEntity<?> response = getStudentByEmail(email); // Chiama il metodo per ottenere lo studente

            // Controlla lo stato della risposta
            if (response.getStatusCode() == HttpStatus.OK) {
                // Recupera il corpo della risposta (la mappa JSON con i dati utente)
                @SuppressWarnings("unchecked")
                Map<String, Object> student = (Map<String, Object>) response.getBody();
                responseList.add(student);
            }

            return responseList;
        }

        // Caso 2: Cerca per nome o cognome
        if ((surname != null && !surname.isEmpty()) || (name != null && !name.isEmpty())) {
            return getStudentsBySurnameAndName(request); // Passa la ricerca al metodo che gestisce nome e cognome
        }

        // Caso 3: Nessun parametro valido fornito
        return responseList;
    }

}
