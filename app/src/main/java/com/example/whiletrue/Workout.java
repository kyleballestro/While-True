package com.example.whiletrue;

import java.io.Serializable;
import java.util.ArrayList;

public class Workout implements Serializable {

    private String date;
    private String workoutName;
    private ArrayList<Exercise> exercises;

    public Workout(String date, String workoutName, ArrayList<Exercise> exercises) {
        this.date = date;
        this.workoutName = workoutName;
        this.exercises = exercises;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }
}
