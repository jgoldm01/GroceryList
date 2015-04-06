package com.github.jgoldm01.grocerylist.Utilities;

/**
 * Created by jeremy on 4/1/15.
 */
public class StringCheck {

    public static Boolean isNotInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }
}
