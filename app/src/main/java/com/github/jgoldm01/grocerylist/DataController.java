package com.github.jgoldm01.grocerylist;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by jeremy on 2/12/15.
 * singleton dataController object.
 * single point of truth for data operations.
 * in truth, it's public functions interact with the list of grocery lists, and the inventory.
 * responsible for storing data on the local machine, outside of the app's running process
 * this is achieved with SharedPreferences
 */
public class DataController {
    private static DataController controller = null;
    Inventory inventory;
    ArrayList<GList> gLists;
    Context mainContext;
    SharedPreferences dataStore;
    SharedPreferences.Editor editor;
    //boolean as to whether the datacontroller has called getData at the beginning of the app opening
    Boolean isUpdated;

    protected DataController() {
        inventory = new Inventory();
        gLists = new ArrayList<GList>();
        isUpdated = false;
    }

    public static DataController getController() {
        if (controller == null) {
            controller = new DataController();
        }
        return controller;
    }

    public void setContext(Context c) {
        mainContext = c;
        dataStore = c.getSharedPreferences("data", 0);
        editor = dataStore.edit();
    }

    public ArrayList getGLists() {
        return gLists;
    }

    //checks that there is no GList of the same name
    //adds new GList to front of ArrayList (not alphabetically sorted)
    public Boolean addGList(GList newGList) {
        for (GList l : gLists) {
            if (l.name.equalsIgnoreCase(newGList.name)) {
                return false;
            }
        }
        gLists.add(0, newGList);
        return true;
    }

    // returns a string array of the names of lists
//    public String[] getGLists() {
//        String[] gListNames = new String[gLists.size()];
//        int index = 0;
//        for (GList l : gLists) {
//            gListNames[index] = l.name;
//            index++;
//        }
//        return gListNames;
//    }



    public GList findGList(String gListName) {
        for (GList l : gLists) {
            if (l.name.equalsIgnoreCase(gListName)) {
                return l;
            }
        }
        return null;
    }

    public Food addReturnFromInventory(Food newFood) {
        return inventory.addReturnFood(newFood);
    }

    public ArrayList getInventoryFoods() {
        return inventory.getFoods();
    }

    public Boolean addToInventory(Food newFood) {
        return inventory.addFood(newFood);
    }

    public Food findFood(String foodName) {
        return inventory.findFood(foodName);
    }

    public void storeData() {
        //stores food information
        ArrayList <Food> foods = inventory.getFoods();
        storeInt("Food_size", foods.size());
        for (int i = 0; i < foods.size(); i++) {
            Food f = foods.get(i);
            String name = f.getName();
            storeString("food_" + i, name);
            storeString("food_" + name + "_supply", f.getSupply());
            storeString("food_" + name + "_notes", f.getNotes());

            //the glists that the food is found in
            ArrayList <GList> fGLists = f.getGLists();
            storeInt("food_" + name + "_GList_size", fGLists.size());
            for (int j = 0; j < fGLists.size(); j++) {
                storeString("food_" + name + "_gList_" + j, fGLists.get(j).getName());
            }
        }

        //stores glist information. doesn't store which foods are in the glist
        //since that information is in the food keystore
        storeInt("GList_size", gLists.size());
        for (int i = 0; i < gLists.size(); i++) {
            storeString("gList_" + i, gLists.get(i).getName());
        }

        editor.commit();
    }

    //stores data that may have been updated from the food page.
    //only supply or notes could have been changed from the calling activity
    public void storeFoodData(Food f) {
        String name = f.getName();
        storeString("food_" + name + "_supply", f.getSupply());
        storeString("food_" + name + "_notes", f.getNotes());
        editor.commit();
    }

    public void getData() {
        if (!isUpdated) {
            //retrieves gList information. to be continued
            int gListNum = getInt("GList_size");
            for (int i = 0; i < gListNum; i++) {
                gLists.add(new GList(getString("gList_" + i)));
            }

            //retrieves food information
            int foodNum = getInt("Food_size");
            for (int i = 0; i < foodNum; i++) {
                String name = getString("food_" + i);
                Food f = new Food(name);
                f.setSupply(getString("food_" + name + "_supply"));
                f.setNotes(getString("food_" + name + "_notes"));

                //the gLists the food is found in
                int fGListNum = getInt("food_" + name + "_GList_size");
                for (int j = 0; j < fGListNum; j++) {
                    String fGListName = getString("food_" + name + "_gList_" + j);
                    GList gList = findGList(fGListName);
                    f.addGList(gList);
                    gList.addFood(f);
                }
                inventory.addFood(f);
            }
            isUpdated = true;
        }
    }


    private void storeString(String key, String value) {
        editor.putString(key, value);
    }

    private void storeInt(String key, int value) {
        editor.putInt(key, value);
    }

    private int getInt(String key) {
        return dataStore.getInt(key, 0);
    }

    private String getString(String key) {
        return dataStore.getString(key, "");
    }
}
