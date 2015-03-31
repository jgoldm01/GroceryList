package com.github.jgoldm01.grocerylist;

import java.util.ArrayList;

/**
 * Created by jeremy on 2/12/15.
 */
public class GList {
    String name;
    ArrayList<Food> foods;

    public GList(String name) {
        this.name = name;
        foods = new ArrayList<Food>();
    }

    public String getName () {
        return name;
    }

    public ArrayList getFoods() {
        return foods;
    }

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

//    public String[] getFoods() {
//        String[] foodNames = new String[foods.size()];
//        int index = 0;
//        for (Food f : foods) {
//            foodNames[index] = f.name;
//            index++;
//        }
//        return foodNames;
//    }

}
