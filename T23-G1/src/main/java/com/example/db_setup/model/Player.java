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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entità che rappresenta un utente di tipo giocatore registrato nel sistema.
 * <p>
 * Contiene le informazioni di autenticazione, il corso di studi e i collegamenti al profilo
 * {@link UserProfile} e ai progressi giocatore {@link PlayerProgress}.
 * </p>
 */
@Table(name = "players", schema = "studentsrepo")
@Data
@Entity
@Getter
@Setter
public class Player {

    /**
     * Identificatore univoco del giocatore.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long ID;

    /**
     * Password dell’utente hashata.
     */
    public String password;

    /**
     * Corso di studi dell’utente.
     */
    @Enumerated(EnumType.STRING)
    public Studies studies;

    /**
     * Profilo utente contenente informazioni personali (nome, cognome, nickname, email).
     */
    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    /**
     * Progressi di gioco del giocatore.
     * <p>
     * Annotato con {@link JsonIgnore} per evitare serializzazione ricorsiva nelle API.
     * </p>
     */
    @JsonIgnore
    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    private PlayerProgress playerProgress;

    /**
     * Costruttore completo per creare un giocatore con informazioni iniziali di profilo.
     *
     * @param name     il nome del giocatore
     * @param surname  il cognome del giocatore
     * @param email    l'email del giocatore
     * @param password la password del giocatore
     * @param studies  il corso di studi del giocatore
     */
    public Player(String name, String surname, String email, String password, Studies studies) {
        this();
        this.password = password;
        this.studies = studies;
        this.userProfile.setName(name);
        this.userProfile.setSurname(surname);
        this.userProfile.setEmail(email);
    }

    /**
     * Costruttore di default.
     * <p>
     * Inizializza l'{@link UserProfile} e lo associa al giocatore.
     * </p>
     */
    public Player() {
        this.userProfile = new UserProfile();
        this.userProfile.setPlayer(this);
    }

    public String getName() {
        return this.userProfile.getName();
    }

    public void setName(String name) {
        this.userProfile.setName(name);
    }

    public String getSurname() {
        return this.userProfile.getSurname();
    }

    public void setSurname(String surname) {
        this.userProfile.setSurname(surname);
    }

    public String getNickname() {
        return this.userProfile.getNickname();
    }

    public void setNickname(String nickname) {
        this.userProfile.setNickname(nickname);
    }

    public String getEmail() {
        return this.userProfile.getEmail();
    }

    public void setEmail(String email) {
        this.userProfile.setEmail(email);
    }
}
