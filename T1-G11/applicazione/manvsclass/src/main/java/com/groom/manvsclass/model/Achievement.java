package com.groom.manvsclass.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(collection = "achievements")
public class Achievement {
    @Id @GeneratedValue // Auto-Increment
    private String ID;

    private String name;
    private String description;
    private int category;
    private float progressRequired;

    public Achievement() {

    }

    public Achievement(String ID, String name, String description, int category, float progressRequired) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.category = category;
        this.progressRequired = progressRequired;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public float getProgressRequired() {
        return progressRequired;
    }

    public void setProgressRequired(float progressRequired) {
        this.progressRequired = progressRequired;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", progressRequired=" + progressRequired +
                '}';
    }
}
