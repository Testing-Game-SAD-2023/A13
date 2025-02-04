package com.groom.manvsclass.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private int numberOfRounds;
    private List<SelectedClasses> selectedClasses;

    //Void Constructor
    public Scalata() {
        
    }

    //Constructor
    public Scalata(String username, String scalataName, String scalataDescription, int numberOfRounds, List<SelectedClasses> selectedClasses) {
        
        this.username = username;
        this.scalataName = scalataName;
        this.scalataDescription = scalataDescription;
        this.numberOfRounds = numberOfRounds;
        this.selectedClasses = selectedClasses;
    }
    //Getters
    
    public String getUsername() {
        return username;
    }
    public String getScalataName() {
        return scalataName;
    }
    public String getScalataDescription() {
        return scalataDescription;
    }
    public int getNumberOfRounds() {
        return numberOfRounds;
    }
   
    public List<SelectedClasses> getSelectedClasses() {
        return selectedClasses;
    }
    
    public void setSelectedClasses(List<SelectedClasses> selectedClasses) {
        this.selectedClasses = selectedClasses;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    public void setScalataName(String scalataName) {
        this.scalataName = scalataName;
    }
    public void setScalataDescription(String scalataDescription) {
        this.scalataDescription = scalataDescription;
    }
    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    //Overriding the toString() method
    @Override
    public String toString() {
        return "Scalata [" +
                "author=" + username + ", " +
                "scalataName=" + scalataName + "," +
                "scalataDescription=" + scalataDescription + ", " +
                "rounds=" + numberOfRounds + ", " +
                "selectedClasses=" + selectedClasses + 
                "]";
    }

    
}
