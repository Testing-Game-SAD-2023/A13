package com.g2.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScalataDTO {
    @JsonProperty("scalataName")
    private String name;
    
    @JsonProperty("numberOfLevels")
    private Integer totalLevels;
    
    @JsonProperty("scalataDescription")
    private String description;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("levels")
    private Integer[] levels;
    
    // Costruttore vuoto per Jackson
    public ScalataDTO() {}
    
    // Costruttore con parametri
    public ScalataDTO(String name, Integer totalLevels, String description) {
        this.name = name;
        this.totalLevels = totalLevels;
        this.description = description;
    }
    
    // Getters e Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getTotalLevels() {
        return totalLevels;
    }
    
    public void setTotalLevels(Integer totalLevels) {
        this.totalLevels = totalLevels;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Integer[] getLevels() {
        return levels;
    }
    
    public void setLevels(Integer[] levels) {
        this.levels = levels;
    }
    
    @Override
    public String toString() {
        return "ScalataDTO{" +
                "name='" + name + '\'' +
                ", totalLevels=" + totalLevels +
                ", description='" + description + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
