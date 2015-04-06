package com.github.jgoldm01.grocerylist;

import com.github.jgoldm01.grocerylist.Utilities.Sorter;

import java.util.ArrayList;

/**
 * Created by jeremy on 2/12/15.
 */
public class Inventory {
    ArrayList<Food> foods;

    public Inventory() {
        foods = new ArrayList<Food>();
    }

    public ArrayList <Food> getFoods() {
        return foods;
    }

    public Food getFood(int position) {
        return foods.get(position);
    }

    //returns true if food was added to the inventory,
    //returns false if food is already present in the inventory
    public Boolean addFood(Food newFood, String sortMechanism) {
        if (sortMechanism.equalsIgnoreCase("alphabetical")) {
            return addFoodAlphabetical(newFood);
        } else {
            return addFoodEditedCreated(newFood);
        }
    }

    //returns true if food was added to the inventory,
    //returns false if food is already present in the inventory
    public Boolean addFoodAlphabetical(Food newFood) {
        for (Food f : foods) {
            if (f.name.equalsIgnoreCase(newFood.name)) {
                return false;
            }
            // if the string name of the food in foods is greater (b>a)
            //than the new food, add newfood to the list
            if (f.name.compareToIgnoreCase(newFood.name) > 0) {
                foods.add(foods.indexOf(f), newFood);
                return true;
            }
        }
        foods.add(newFood);
        return true;
    }

    public Boolean addFoodEditedCreated(Food newFood) {
        for (Food f : foods) {
            if (f.name.equalsIgnoreCase(newFood.name)) {
                return false;
            }
        }
        foods.add(0, newFood);
        return true;
    }

    //adds newFood to inventory OR returns the already extant object
    //needed by GListActivity
    public Food addReturnFood(Food newFood, String sortMechanism) {
        if (sortMechanism.equalsIgnoreCase("alphabetical")) {
            if (addFoodAlphabetical(newFood)) {
                return newFood;
            } else {
                return findFood(newFood.getName());
            }
        } else {
            if (addFoodEditedCreated(newFood)) {
                return newFood;
            } else {
                return findFood(newFood.getName());
            }
        }
    }

    public Food findFood(String foodName) {
        for (Food f : foods) {
            if (f.name.equalsIgnoreCase(foodName)) {
                return f;
            }
        }
        return null;
    }

    public void deleteGListFromFoods(String gListName) {
        for (Food f : foods) {
            f.deleteGList(gListName);
        }
    }

    public void deleteFood(int position) {
        foods.remove(position);
    }

    //    public String[] getFoods() {
//        String[] foodNames = new String[foods.size()];
//        int index = 0;
//        for (Food f : foods) {
//            foodNames[index] = f.name;
//            index++;
//        }
//        return foodNames;
//    }

    public void sort(String sortMechanism) {
        Sorter.sort(foods, sortMechanism, "Food");
    }
}
