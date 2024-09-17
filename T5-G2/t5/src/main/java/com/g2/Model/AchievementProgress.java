package com.g2.Model;

public class AchievementProgress {
    public int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public AchievementProgress(int id, String name, float progressRequired, float progress)
    {
        this.ID = id;
        this.Name = name;
        this.ProgressRequired = progressRequired;
        this.Progress = progress;
    }

    public AchievementProgress()
    {

    }

    @Override
    public String toString() {
        return "AchievementProgress{" +
                "\n   ID:" + ID +
                "\n   Name:" + Name +
                "\n   ProgressRequired:" + ProgressRequired +
                "\n   Progress:" + Progress +
                "\n}";
    }
}
