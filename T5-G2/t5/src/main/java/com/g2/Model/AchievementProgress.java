package com.g2.Model;

public class AchievementProgress {
    public String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String Description;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public float ProgressRequired;

    public float getProgressRequired() {
        return ProgressRequired;
    }

    public void setProgressRequired(float progressRequired) {
        ProgressRequired = progressRequired;
    }

    public float Progress;

    public float getProgress() {
        return Progress;
    }

    public void setProgress(float progress) {
        Progress = progress;
    }

    public AchievementProgress(String id, String name, String description, float progressRequired, float progress)
    {
        this.ID = id;
        this.Name = name;
        this.Description = description;
        this.ProgressRequired = progressRequired;
        this.Progress = progress;
    }

    public AchievementProgress()
    {

    }

    @Override
    public String toString() {
        return "AchievementProgress{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", ProgressRequired=" + ProgressRequired +
                ", Progress=" + Progress +
                '}';
    }
}
