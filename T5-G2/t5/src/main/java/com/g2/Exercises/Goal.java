package com.g2.Exercises;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.g2.Exercises.Goals.ClassScoreGoal;
import com.g2.Game.GameLogic;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;


@Data
@Document(collection = "goals")

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Goal{
    @Id
    String id;
    String assignmentId;
    String playerId;

    Integer type;
    Integer completition;

    /**
     * Questa funzione va chiamata sui parametri della partita affinché
     * l'esercizio aggiorni il suo stato. I parametri sono esattamente
     * i dati fruibili a fine partita attraverso il GameController.
     */
    public abstract void match(Map<String, String> userData, GameLogic gameLogic);

    

    /**
     * Restituisce una specializzazione di Esercizio a partire da un json
     * 
     * @param obj è una stringa json
     * @return specializzazione di esercizio
     * @throws JacksonException
     */
    static Goal deserialize(String obj)
        throws JacksonException    
    {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        final JsonNode node = mapper.readTree(obj);
        int type=0;
        if(node.has("type")){
            type = node.get("type").asInt();
        }
        Goal retval;
        switch (type) {
            case 1:
                retval = mapper.readValue(obj, ClassScoreGoal.class);
                break;
            default:
                throw new IllegalArgumentException("Type not recognized");
        }
        return retval;
    }

}