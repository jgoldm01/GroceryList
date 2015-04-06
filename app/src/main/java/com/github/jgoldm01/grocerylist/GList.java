package com.github.jgoldm01.grocerylist;

import com.github.jgoldm01.grocerylist.Utilities.Sorter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jeremy on 2/12/15.
 */
public class GList {
    String name;
    long createTime;
    long editTime;
    ArrayList<Food> foods;

    public GList(String name) {
        this.name = name;
        createTime = System.currentTimeMillis();
        editTime = System.currentTimeMillis();
        foods = new ArrayList<Food>();
    }

    public String getName () {
        return name;
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

    public ArrayList getFoods() {
        return foods;
    }

    public Boolean addFood(Food newFood, String sortMechanism) {
        if (sortMechanism.equalsIgnoreCase("alphabetical")) {
            return addFoodAlphabetical(newFood);
        } else {
            return addFoodEditedCreated(newFood);
        }
    }

    private Boolean addFoodAlphabetical(Food newFood) {
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

    private Boolean addFoodEditedCreated(Food newFood) {
        for (Food f : foods) {
            if (f.name.equalsIgnoreCase(newFood.name)) {
                return false;
            }
        }
        foods.add(0, newFood);
        return true;
    }

    public void deleteFood(String foodName) {
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).getName().equalsIgnoreCase(foodName)) {
                foods.remove(i);
            }
        }
    }

    //delete function called from activity page, ensures that this gList is removed from food object as well
    public void deleteFood(int position) {
        Food f = foods.get(position);
        f.deleteGList(name);
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
