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

package com.g2.session;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.g2.game.gameMode.GameLogic;
import lombok.Setter;
import testrobotchallenge.commons.models.opponent.GameMode;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Classe che rappresenta una sessione di un utente. Contiene una mappa delle
 * diverse modalità di gioco associate a un utente.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Sessione implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("id_user")
    private final String userId;
    // Timestamp di creazione della sessione (immutabile)
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private final Instant createdAt;
    @Setter
    @JsonProperty("id_sess")
    private Long idSessione;
    // Timestamp aggiornato ad ogni update della sessione
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    // Mappa che contiene le modalità di gioco associate a un wrapper
    @JsonProperty("modalita")
    private Map<GameMode, ModalitaWrapper> modalita;

    /**
     * Costruttore per inizializzare una sessione.
     *
     * @param idSessione Identificativo univoco della sessione
     * @param userId     Identificativo dell'utente proprietario della sessione
     */
    public Sessione(
            Long idSessione,
            String userId
    ) {
        this.idSessione = Objects.requireNonNull(idSessione, "idSessione non può essere null");
        this.userId = Objects.requireNonNull(userId, "userId non può essere null");
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.modalita = new HashMap<GameMode, ModalitaWrapper>();
    }

    /*
     * Costruttore per serializzare
     */
    @JsonCreator
    public Sessione(
            @JsonProperty("id_sess") Long idSessione,
            @JsonProperty("id_user") String userId,
            @JsonProperty("created_at") Instant createdAt,
            @JsonProperty("updated_at") Instant updatedAt,
            @JsonProperty("modalita") Map<GameMode, ModalitaWrapper> modalita
    ) {
        this.idSessione = idSessione;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.modalita = modalita != null ? modalita : new HashMap<GameMode, ModalitaWrapper>();
    }

    // Costruttore vuoto per deserializzazione
    public Sessione() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.modalita = new HashMap<GameMode, ModalitaWrapper>();
        this.userId = "";
    }

    // Getters e setters
    public Long getIdSessione() {
        return idSessione;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Map<GameMode, ModalitaWrapper> getModalita() {
        return modalita;
    }

    public void setModalita(Map<GameMode, ModalitaWrapper> modalita) {
        this.modalita = Objects.requireNonNull(modalita, "modalita non può essere null");
        this.updatedAt = Instant.now(); // Aggiorna il timestamp di update
    }

    /**
     * Rimuove una modalità di gioco dalla sessione.
     *
     * @param key Nome della modalità da rimuovere
     * @return true se la modalità è stata rimossa, false se la modalità non
     * esisteva
     */
    public boolean removeModalita(GameMode key) {
        if (this.modalita.containsKey(key)) {
            this.modalita.remove(key);
            this.updatedAt = Instant.now(); // Aggiorna il timestamp di update
            return true; // Modalità rimossa con successo
        }
        return false; // La modalità non esisteva nella sessione
    }

    /**
     * Aggiunge (o aggiorna) una modalità di gioco nella sessione.
     *
     * @param key  Nome della modalità (es. "Sfida", "Allenamento")
     * @param game Oggetto GameLogic associato alla modalità
     */
    public void addModalita(GameMode key, GameLogic game) {
        Objects.requireNonNull(key, "La chiave della modalità non può essere null");
        Objects.requireNonNull(game, "L'oggetto GameLogic non può essere null");
        this.modalita.put(key, new ModalitaWrapper(game));
        this.updatedAt = Instant.now(); // Aggiorna il timestamp di update
    }

    /**
     * Controlla se una determinata modalità è presente nella sessione.
     *
     * @param key Nome della modalità da cercare
     * @return true se la modalità esiste, false altrimenti
     */
    public boolean hasModalita(GameMode key) {
        return this.modalita.containsKey(key);
    }

    public GameLogic getGame(GameMode mode) {
        return this.modalita.get(mode).gameobject;
    }


    @Override
    public String toString() {
        return "Sessione{"
                + "idSessione='" + idSessione + '\''
                + ", userId='" + userId + '\''
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", modalita=" + modalita
                + '}';
    }

    /**
     * Record per rappresentare una modalità di gioco con timestamp di creazione
     * e aggiornamento.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    public record ModalitaWrapper(
            @JsonProperty("gameobject")
            GameLogic gameobject,
            @JsonProperty("created_at")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
            Instant createdAt,
            @JsonProperty("updated_at")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
            Instant updatedAt) implements Serializable {
        private static final long serialVersionUID = 1L;

        public ModalitaWrapper(GameLogic gameobject) {
            this(gameobject, Instant.now(), Instant.now());
        }
    }
}
