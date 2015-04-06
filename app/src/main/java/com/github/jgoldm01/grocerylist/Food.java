package com.github.jgoldm01.grocerylist;

import java.util.ArrayList;

/**
 * Created by jeremy on 2/12/15.
 */
public class Food {
    String name;
    String supply;
    String notes;
    long createTime;
    long editTime;
    ArrayList<GList> gLists;

    public Food(String name) {
        this.name = name;
        supply = "Good";
        notes = "";
        createTime = System.currentTimeMillis();
        editTime = System.currentTimeMillis();
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

    public long getCreateTime() {
        return createTime;
    }

    //called at opening of app, when retrieving stored objects from sharedPreferences
    public void setCreateTime(long storedTime) {
        createTime = storedTime;
    }

    public long getEditTime() {
        return editTime;
    }

    public void setEditTime(long storedTime) {
        editTime = storedTime;
    }

    public void updateEditTime() {
        editTime = System.currentTimeMillis();
    }

    // adds GList to gLists. called by GListActivity
    public void addGList(GList gList) {
        gLists.add(gList);
    }

    public void deleteGList(String gListName) {
        for (int i = 0; i < gLists.size(); i++) {
            if (gLists.get(i).getName().equalsIgnoreCase(gListName))  {
                gLists.remove(i);
            }
        }
    }

    //delete function called from activity page, ensures that this food is removed from gList object as well
    public void deleteGList(int position) {
        GList g = gLists.get(position);
        g.deleteFood(name);
        gLists.remove(position);
    }

    public void setSupply(String supplyLevel) {
        supply = supplyLevel;
    }

    public String[] getStringGLists() {
        String[] gListNames = new String[gLists.size()];
        int index = 0;
        for (GList l : gLists) {
            gListNames[index] = l.name;
            index++;
        }
        return gListNames;
    }

    public ArrayList<GList> getGLists() {
        return gLists;
    }

}
