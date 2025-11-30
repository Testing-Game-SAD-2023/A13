package com.groom.manvsclass.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/*
 * the @Document annotation is used to indicate that an instance
 * of the class should be stored as a document in a MongoDB collection
 *
 * the @Id annotation is used to specify the primary key field of the entity
 */
@Document(collection = "scalate")
public class Scalata {

    @Id
    private String scalataName;

    private String username;
    private String scalataDescription;
    private int numberOfLevels;
    private List<Integer> Levels;

    //Void Constructor
    public Scalata() {

    }

    //Constructor
    public Scalata(String username, String scalataName, String scalataDescription, int numberOfLevels, List<Integer> levels) {

        this.username = username;
        this.scalataName = scalataName;
        this.scalataDescription = scalataDescription;
        this.numberOfLevels = numberOfLevels;
        this.Levels = levels;
    }
    //Getters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScalataName() {
        return scalataName;
    }

    public void setScalataName(String scalataName) {
        this.scalataName = scalataName;
    }

    public String getScalataDescription() {
        return scalataDescription;
    }
    //Setters

    public void setScalataDescription(String scalataDescription) {
        this.scalataDescription = scalataDescription;
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    public void setNumberOfLevels(int numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
    }

    public List<Integer> getLevels() {
        return Levels;
    }

    public void setLevels(List<Integer> levels) {
        this.Levels = levels;
    }

    //Overriding the toString() method
    @Override
    public String toString() {
        return "Scalata [" +
                "author=" + username + ", " +
                "scalataName=" + scalataName + "," +
                "scalataDescription=" + scalataDescription + ", " +
                "levels=" + numberOfLevels + ", " +
                "levelsList=" + Levels +
                "]";
    }


}
