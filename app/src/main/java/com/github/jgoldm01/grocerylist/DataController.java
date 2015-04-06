package com.github.jgoldm01.grocerylist;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.jgoldm01.grocerylist.Constants.Colors;
import com.github.jgoldm01.grocerylist.Utilities.Sorter;
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
    //true if user is in delete mode
    Boolean deleteMode;
    int color;
    String sortMechanism;

    protected DataController() {
        inventory = new Inventory();
        gLists = new ArrayList<GList>();
        isUpdated = false;
        deleteMode = false;
        color = Colors.YELLOW;
        sortMechanism = "alphabetical";
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
        getData();
    }

    public ArrayList getGLists() {
        return gLists;
    }

    public Boolean addGList(GList newGList) {
        if (sortMechanism.equalsIgnoreCase("alphabetical")) {
            return addGListAlphabetical(newGList);
        } else {
            return addGListEditedCreated(newGList);
        }
    }

    //checks that there is no GList of the same name
    //adds new GList to front of ArrayList (not alphabetically sorted)
    private Boolean addGListAlphabetical(GList newGList) {
        for (GList g : gLists) {
            if (g.name.equalsIgnoreCase(newGList.name)) {
                return false;
            }
            // if the string name of the GList in gLists is greater (b>a)
            //than the new gList, add newGList to the list
            if (g.name.compareToIgnoreCase(newGList.name) > 0) {
                gLists.add(gLists.indexOf(g), newGList);
                return true;
            }
        }
        gLists.add(newGList);
        return true;
    }

    private Boolean addGListEditedCreated(GList newGList) {
        for (GList g : gLists) {
            if (g.name.equalsIgnoreCase(newGList.name)) {
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
        return inventory.addReturnFood(newFood, sortMechanism);
    }

    public ArrayList getInventoryFoods() {
        return inventory.getFoods();
    }

    public Boolean addToInventory(Food newFood) {
        return inventory.addFood(newFood, sortMechanism);
    }

    public Food findFood(String foodName) {
        return inventory.findFood(foodName);
    }

    public Boolean isDeleteMode() {
        return deleteMode;
    }

    public void setDeleteMode(Boolean mode){
        deleteMode = mode;
    }

    public void setColor(String c) {
        switch (c) {
            case "red":     color = Colors.RED;
                            break;
            case "orange":  color = Colors.ORANGE;
                            break;
            case "yellow":  color = Colors.YELLOW;
                            break;
            case "green":   color = Colors.GREEN;
                            break;
            case "blue":    color = Colors.BLUE;
                            break;
            case "purple":  color = Colors.PURPLE;
                            break;
            case "pink":    color = Colors.PINK;
                            break;
            case "darkBlue":    color = Colors.DARKBLUE;
                                break;
            case "white":   color = Colors.WHITE;
                            break;
        }
    }

    public int getColor() {
        return color;
    }

    //big long function that I need to write that will literally resort everything
    public void setSortMechanism(String textViewString) {
        if (textViewString.equalsIgnoreCase("Alphabetical order")) {
            sortMechanism = "alphabetical";
        } else if (textViewString.equalsIgnoreCase("Most Recently Edited")) {
            sortMechanism = "edited";
        } else if (textViewString.equalsIgnoreCase("Time Created")) {
            sortMechanism = "created";
        }
    }

    public String getSortMechanism() {
        return sortMechanism;
    }

    //deletes all references of gList
    public void deleteGList(int position) {
        String gListName = gLists.get(position).getName();
        //removes all references in foods
        inventory.deleteGListFromFoods(gListName);
        gLists.remove(position);
    }

    public void deleteFood(int position) {
        String foodName = inventory.getFood(position).getName();
        for (GList g : gLists) {
            g.deleteFood(foodName);
        }
        inventory.deleteFood(position);
    }

    public void storeData() {
        storeInt("color", color);
        storeString("sortMechanism", sortMechanism);

        //stores glist information. doesn't store which foods are in the glist
        //since that information is in the food keystore
        storeInt("GList_size", gLists.size());
        for (int i = 0; i < gLists.size(); i++) {
            GList g = gLists.get(i);
            String name = g.getName();
            storeString("gList_" + i, g.getName());
            storeLong("gList_" + name + "_createTime", g.getCreateTime());
            storeLong("gList_" + name + "_editTime", g.getEditTime());
        }

        //stores food information
        ArrayList <Food> foods = inventory.getFoods();
        storeInt("Food_size", foods.size());
        for (int i = 0; i < foods.size(); i++) {
            Food f = foods.get(i);
            String name = f.getName();
            storeString("food_" + i, name);
            storeString("food_" + name + "_supply", f.getSupply());
            storeString("food_" + name + "_notes", f.getNotes());
            storeLong("food_" + name + "_createTime", f.getCreateTime());
            storeLong("food_" + name + "_editTime", f.getEditTime());

            //the glists that the food is found in
            ArrayList<GList> fGLists = f.getGLists();
            storeInt("food_" + name + "_GList_size", fGLists.size());
            for (int j = 0; j < fGLists.size(); j++) {
                storeString("food_" + name + "_gList_" + j, fGLists.get(j).getName());
            }
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
            if (getInt("color") != 0) {
                color = getInt("color");
            }
            if (!getString("sortMechanism").equals("")) {
                sortMechanism = getString("sortMechanism");
            }

            //retrieves gList information. to be continued
            int gListNum = getInt("GList_size");
            for (int i = 0; i < gListNum; i++) {
                String name = getString("gList_" + i);
                GList g = new GList(name);
                g.setCreateTime(getLong("gList_" + name + "_createTime"));
                g.setEditTime(getLong("gList_" + name + "_editTime"));
                gLists.add(g);
            }

            //retrieves food information
            int foodNum = getInt("Food_size");
            for (int i = 0; i < foodNum; i++) {
                String name = getString("food_" + i);
                Food f = new Food(name);
                f.setSupply(getString("food_" + name + "_supply"));
                f.setNotes(getString("food_" + name + "_notes"));
                f.setCreateTime(getLong("food_" + name + "_createTime"));
                f.setEditTime(getLong("food_" + name + "_editTime"));

                //the gLists the food is found in
                int fGListNum = getInt("food_" + name + "_GList_size");
                for (int j = 0; j < fGListNum; j++) {
                    String fGListName = getString("food_" + name + "_gList_" + j);
                    GList gList = findGList(fGListName);
                    f.addGList(gList);
                    gList.addFood(f, sortMechanism);
                }
                inventory.addFood(f, sortMechanism);
            }
            isUpdated = true;
        }
    }


    private void storeInt(String key, int value) {
        editor.putInt(key, value);
    }

    private int getInt(String key) {
        return dataStore.getInt(key, 0);
    }

    private void storeLong(String key, long value) {
        editor.putLong(key, value);
    }

    private long getLong(String key) {
        return dataStore.getLong(key, 0);
    }

    private void storeString(String key, String value) {
        editor.putString(key, value);
    }

    private String getString(String key) {
        return dataStore.getString(key, "");
    }

    public void sort() {
        sortInventory();
        sortGLists();
        for (GList g : gLists) {
            g.sort(sortMechanism);
        }
    }

    public void sortGLists() {
        Sorter.sort(gLists, sortMechanism, "GList");
    }

    public void sortInventory() {
        inventory.sort(sortMechanism);
    }
}
