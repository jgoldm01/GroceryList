package com.github.jgoldm01.grocerylist;

import java.util.ArrayList;

/**
 * Created by jeremy on 2/12/15.
 * singleton dataController object.
 * single point of truth for data operations.
 * in truth, it's public functions interact with the list of grocery lists, and the inventory.
 */
public class DataController {
    private static DataController controller = null;
    Inventory inventory;
    ArrayList<GList> gLists;

    protected DataController() {
        inventory = new Inventory();
        gLists = new ArrayList<GList>();
    }

    public static DataController getController() {
        if (controller == null) {
            controller = new DataController();
        }
        return controller;
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


}
