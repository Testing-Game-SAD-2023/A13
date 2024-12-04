package com.g2.Model;

public class Mission {

    private Integer ID;
    private String name;
    private String description;
    private Integer numToken;

    public Mission(Integer ID, String name, String description, Integer numToken) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.numToken = numToken;
    }
    
    // Costruttore vuoto necessario per thymeleaf
    public Mission() {
    }


    // Getters and Setters
    public Integer getId() {
        return ID;
    }

    public void setId(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumToken() {
        return numToken;
    }

    public void setNumToken(Integer numToken) {
        this.numToken = numToken;
    }


}