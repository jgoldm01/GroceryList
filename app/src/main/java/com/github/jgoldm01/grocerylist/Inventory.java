package com.github.jgoldm01.grocerylist;

import java.util.ArrayList;

/**
 * Created by jeremy on 2/12/15.
 */
public class Inventory {
    ArrayList<Food> foods;

    public Inventory() {
        foods = new ArrayList<Food>();
    }

    public ArrayList getFoods() {
        return foods;
    }

    //returns true if food was added to the inventory,
    //returns false if food is already present in the inventory
    public Boolean addFood(Food newFood) {
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

    //adds newFood to inventory OR returns the already extant object
    //needed by GListActivity
    public Food addReturnFood(Food newFood) {
        for (Food f : foods) {
            if (f.name.equalsIgnoreCase(newFood.name)) {
                return f;
            }
            // if the string name of the food in foods is greater (b>a)
            //than the new food, add newfood to the list
            if (f.name.compareToIgnoreCase(newFood.name) > 0) {
                foods.add(foods.indexOf(f), newFood);
                return newFood;
            }
        }
        foods.add(newFood);
        return newFood;
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

    public Food findFood(String foodName) {
        for (Food f : foods) {
            if (f.name.equalsIgnoreCase(foodName)) {
                return f;
            }
        }
        return null;
    }
}
