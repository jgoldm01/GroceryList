package com.github.jgoldm01.grocerylist;

import java.util.ArrayList;

/**
 * Created by jeremy on 2/12/15.
 */
public class Food {
    String name;
    String supply;
    String notes;
    ArrayList<GList> gLists;

    public Food(String name) {
        this.name = name;
        supply = "Good";
        notes = "";
        gLists = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getSupply() {
        return supply;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String newNotes) {
        notes = newNotes;
    }

    // adds GList to gLists. called by GListActivity
    public void addGList(GList gList) {
        gLists.add(gList);
    }



    public void setSupply(String supplyLevel) {
        supply = supplyLevel;
    }

    public String[] getGLists() {
        String[] gListNames = new String[gLists.size()];
        int index = 0;
        for (GList l : gLists) {
            gListNames[index] = l.name;
            index++;
        }
        return gListNames;
    }

}
