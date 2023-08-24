package com.example.whiletrue;

import java.io.Serializable;

public class Set implements Serializable {

    private String exerciseName, weight, reps, notes;

    public Set() {
    }

    public Set(String exerciseName, String weight, String reps, String notes) {
        this.exerciseName = exerciseName;
        this.weight = weight;
        this.reps = reps;
        this.notes = notes;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Set{" +
                "exerciseName='" + exerciseName + '\'' +
                ", weight='" + weight + '\'' +
                ", reps='" + reps + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
