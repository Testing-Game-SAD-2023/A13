package com.g2.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO per i dati di una Scalata ricevuti da T1.
 * 
 * NOTA: T1 invia anche i campi 'date' e 'listOfLevels' che vengono ignorati automaticamente da Jackson.
 * I livelli vengono recuperati singolarmente tramite endpoint /getLevel/{name}/{number}
 */

@Getter
@Setter
public class ScalataDTO {

    @JsonProperty("name")
    private String name;
    
    @JsonProperty("numberOfLevels")
    private int totalLevels;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("username")
    private String username;

    /* logica di lista dei livelli rimossa
    @JsonProperty("levels")
    private List<LevelDataDTO> levels;
     */
    
    // Costruttore vuoto per Jackson
    public ScalataDTO() {}
    
    // Costruttore con parametri
    public ScalataDTO(String name, Integer totalLevels, String description) {
        this.name = name;
        this.totalLevels = totalLevels;
        this.description = description;
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
