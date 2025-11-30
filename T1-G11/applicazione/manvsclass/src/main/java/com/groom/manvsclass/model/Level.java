package com.groom.manvsclass.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/*
 * the @Document annotation is used to indicate that an instance
 * of the class should be stored as a document in a MongoDB collection
 *
 * the @Id annotation is used to specify the primary key field of the entity
 */
@Document(collection = "level")
public class Level {
    
    @Id
    private int idLevel;
    private String scalataName; 
    private String className;
    private int tempoMax;
    private String opponentName;

    // Constructors
    public Level() {
    }

    public Level(int idLevel, String scalataName, String className, int tempoMax, String opponentName) {
        this.idLevel = idLevel;
        this.scalataName = scalataName;  
        this.className = className;
        this.tempoMax = tempoMax;
        this.opponentName = opponentName;
    }

    // Getters
    public int getIdLevel() {
        return idLevel;
    }

    public String getScalataName() {
        return scalataName;  // ← CORRETTO
    }

    public String getClassName() {
        return className;
    }

    public int getTempoMax() {
        return tempoMax;
    }

    public String getOpponentName() {
        return opponentName;
    }

    // Setters
    public void setIdLevel(int idLevel) {
        this.idLevel = idLevel;
    }

    public void setScalataName(String scalataName) {
        this.scalataName = scalataName;  // ← CORRETTO
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTempoMax(int tempoMax) {
        this.tempoMax = tempoMax;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    @Override
    public String toString() {
        return "Level [" +
                "idLevel=" + idLevel +
                ", scalataName='" + scalataName + '\'' +  // ← CORRETTO
                ", className='" + className + '\'' +
                ", tempoMax=" + tempoMax +
                ", opponentName='" + opponentName + '\'' +
                ']';
    }
}
