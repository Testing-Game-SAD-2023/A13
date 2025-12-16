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
package com.example.db_setup.service;

import com.example.db_setup.model.Player;
import com.example.db_setup.model.Studies;
import com.example.db_setup.model.UserProfile;
import com.example.db_setup.model.repository.PlayerRepository;
import com.example.db_setup.model.repository.UserProfileRepository;
import com.example.db_setup.service.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


// Questa classe è un servizio che gestisce le operazioni relative agli utenti
@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);
    private final PlayerProgressService playerProgressService;
    private final PlayerRepository playerRepository;
    private final UserProfileRepository userProfileRepository;

    public PlayerService(PlayerProgressService playerProgressService, PlayerRepository playerRepository, UserProfileRepository userProfileRepository) {
        this.playerProgressService = playerProgressService;
        this.playerRepository = playerRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public Player addNewPlayer(String name, String surname, String email, String password, Studies studies) {
        // Creo il nuovo account utente
        Player player = new Player(name, surname, email, password, studies);
        player.setPlayerProgress(playerProgressService.createNewPlayerProgress(player));

        return playerRepository.save(player);
    }

    // Recupera dal DB l'utente con l'email specificata
    public Player getUserByEmail(String email) {
        return playerRepository.findByUserProfileEmail(email).orElse(null);
    }

    public Player getUserByID(Long id) {
        Optional<Player> player = playerRepository.findById(id);
        if (player.isEmpty())
            throw new UserNotFoundException();

        return player.get();
    }

    public List<Player> getUserListByEmail(String email) {
        return playerRepository.findByUserProfileEmailLike(email);
    }

    // Modifica 06/12/2024: Aggiunta end-point per restituire solo i campi non sensibili dello USER
    public ResponseEntity<?> getStudentByEmail(String email) {
        Optional<Player> userOpt = playerRepository.findByUserProfileEmail(email);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato per email: " + email);
        }

        Player player = userOpt.get();
        // Creazione della mappa JSON con i campi desiderati
        Map<String, Object> response = new HashMap<>();
        response.put("id", player.getID());
        response.put("name", player.getName());
        response.put("surname", player.getSurname());
        response.put("email", player.getEmail());
        return ResponseEntity.ok(response);
    }


    public UserProfile findProfileByEmail(String email) {
        // Recupera l'utente con l'email specificata
        Optional<Player> userOpt = playerRepository.findByUserProfileEmail(email);

        //Controlla se l'utente esiste
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }

        // Restituisce il profilo dell'utente
        return userOpt.get().getUserProfile();
    }

    public void saveProfile(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("Profile not found");
        }
        userProfileRepository.save(userProfile);
    }

    //Modifica 04/12/2024 Giuleppe
    public ResponseEntity<?> getStudentiTeam(List<String> idUtenti) {
        logger.info("Inizio metodo getStudentiTeam. ID ricevuti: {}", idUtenti);

        // Controlla se la lista di ID è vuota
        if (idUtenti == null || idUtenti.isEmpty()) {
            logger.info("La lista degli ID è vuota.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lista degli ID vuota.");
        }

        try {
            // Converte gli ID in interi
            List<Long> idIntegerList = idUtenti.stream()
                    .map(Long::valueOf)
                    .toList();
            // Recupera gli utenti dal database
            List<Player> utenti = playerRepository.findAllById(idIntegerList);
            // Verifica se sono stati trovati utenti
            if (utenti.isEmpty()) {
                logger.info("Nessun utente trovato per gli ID forniti.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nessun utente trovato.");
            }
            logger.info("Utenti trovati: {}", utenti);
            // Restituisce la lista di utenti trovati
            return ResponseEntity.ok(utenti);

        } catch (NumberFormatException e) {
            logger.info("Errore durante la conversione degli ID: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Formato degli ID non valido. Devono essere numeri interi.");
        } catch (Exception e) {
            logger.info("Errore durante il recupero degli utenti: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore interno del server.");
        }
    }

    // Modifica 04/12/2024 - Aggiunta gestione ID studenti
    public ResponseEntity<Object> getStudentsByIds(List<String> idUtenti) {
        logger.info("Inizio metodo getStudentsByIds. ID ricevuti: {}", idUtenti);

        // Controlla se la lista di ID è vuota
        if (idUtenti == null || idUtenti.isEmpty()) {
            logger.info("La lista degli ID è vuota.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lista degli ID vuota.");
        }

        try {
            // Converte gli ID in interi (utilizzando Collectors.toList() invece di toList())
            List<Long> idIntegerList = idUtenti.stream()
                    .map(Long::valueOf)
                    .toList();

            // Recupera gli utenti dal database
            List<Player> utenti = playerRepository.findAllById(idIntegerList);

            // Verifica se sono stati trovati utenti
            if (utenti.isEmpty()) {
                logger.info("Nessun utente trovato per gli ID forniti.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nessun utente trovato.");
            }

            logger.info("Utenti trovati: {}", utenti);

            // Mappa i dati degli utenti nei campi desiderati
            List<Map<String, Object>> response = utenti.stream().map(user -> {
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("id", user.getID());
                jsonMap.put("name", user.getName());
                jsonMap.put("surname", user.getSurname());
                jsonMap.put("email", user.getEmail());
                return jsonMap;
            }).toList();

            // Restituisce la lista di utenti filtrati
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            logger.info("Errore durante la conversione degli ID: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Formato degli ID non valido. Devono essere numeri interi.");
        } catch (Exception e) {
            logger.info("Errore durante il recupero degli utenti: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore interno del server.");
        }
    }

    //Modifica 12/12/2024
    public List<Map<String, Object>> getStudentsBySurnameAndName(Map<String, String> request) {
        String surname = request.get("surname");
        String name = request.get("name");
        List<Player> players;

        // Verifica se surname è "null" o vuoto
        if (isNullOrEmpty(surname) && !isNullOrEmpty(name)) {
            players = playerRepository.findByUserProfileName(name);
        } // Verifica se name è "null" o vuoto
        else if (!isNullOrEmpty(surname) && isNullOrEmpty(name)) {
            players = playerRepository.findByUserProfileSurname(surname);
        } // Se entrambi i parametri sono forniti e non vuoti
        else if (!isNullOrEmpty(surname) && !isNullOrEmpty(name)) {
            players = playerRepository.findByUserProfileSurnameAndUserProfileName(surname, name);
        } // Gestisci caso in cui entrambi i parametri sono nulli o vuoti
        else {
            // Ad esempio, puoi restituire tutti gli utenti se entrambi i parametri sono nulli o vuoti
            players = playerRepository.findAll();
        }

        // Restituisci la lista degli utenti mappati in formato JSON
        return mapUsersToResponseList(players);
    }

    // Metodo di utilità per verificare se una stringa è null o vuota
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Metodo di utilità per mappare una lista di utenti in una lista di mappe
    private List<Map<String, Object>> mapUsersToResponseList(List<Player> players) {
        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Player player : players) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", player.getID());
            response.put("name", player.getName());
            response.put("surname", player.getSurname());
            response.put("email", player.getEmail());
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
