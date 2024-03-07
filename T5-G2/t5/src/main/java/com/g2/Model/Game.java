package com.g2.Model;

import java.time.*;

public class Game {
    private int playerId;
    private long id;
    private String description;
    private String name;
    private String difficulty;
    private LocalDate data_creazione;
    private String ora_creazione;
    private String classe;

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Game(int playerId, String description, String name, String difficulty) {
        this.playerId = playerId;
        this.description = description;
        this.name = name;
        this.difficulty = difficulty;
        this.classe = "";
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDate getData_creazione() {
        return data_creazione;
    }

    public void setData_creazione(LocalDate data_creazione) {
        this.data_creazione = data_creazione;
    }

    public String getOra_creazione() {
        return ora_creazione;
    }

    public void setOra_creazione(String ora_creazione) {
        this.ora_creazione = ora_creazione;
    }

    public String getClasse() {
        return classe;
    }

}
