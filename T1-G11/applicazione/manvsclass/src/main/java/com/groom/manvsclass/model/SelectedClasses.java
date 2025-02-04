package com.groom.manvsclass.model;

public class SelectedClasses {
    private String className;
    private String robot;
    private String difficulty;

    // Costruttori, getter e setter
    public SelectedClasses() {}
    
    public SelectedClasses(String className, String robot, String difficulty) {
        this.className = className;
        this.robot = robot;
        this.difficulty = difficulty;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getRobot() {
        return robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}