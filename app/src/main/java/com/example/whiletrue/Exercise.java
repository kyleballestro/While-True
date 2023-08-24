package com.example.whiletrue;

import java.io.Serializable;
import java.util.ArrayList;

public class Exercise implements Serializable {

    private String name;
    private ArrayList<Set> sets;

    public Exercise(String name, ArrayList<Set> sets) {
        this.name = name;
        this.sets = sets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Set> getSets() {
        return sets;
    }

    public void setSets(ArrayList<Set> sets) {
        this.sets = sets;
    }

    public void addSet(Set set){
        sets.add(set);
    }
}
