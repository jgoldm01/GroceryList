package com.github.jgoldm01.grocerylist.Utilities;

import android.hardware.Camera;
import android.util.Log;

import java.util.ArrayList;
import com.github.jgoldm01.grocerylist.*;

/**
 * Created by jeremy on 4/1/15.
 */
public class Sorter {
    //does a mergesort on the arrayList, comparing according to the sortMechanism parameter
    public static void sort(ArrayList arrayList, String sortMechanism, String objectType) {
        int size = arrayList.size();
        insertionSort(arrayList, sortMechanism, size, objectType);
    }

    private static void insertionSort(ArrayList arrayList, String sortMechanism, int size, String objectType) {
        for (int i = 0; i < size; i++) {
            for (int j = i; j > 0; j--) {
                if(compareElements(arrayList.get(j), arrayList.get(j - 1), sortMechanism, objectType)) {
                    swap(arrayList, j, j-1);
                }
            }
        }
    }

    //returns whether the first element should be closer to the zero index of the arrayList than the second
    private static Boolean compareElements(Object firstUncast, Object secondUncast, String sortMechanism, String objectType) {
//        Log.i("elements", "COMPARIN DEM ELEMNS");

        if (objectType.equalsIgnoreCase("GList")) {
            GList first = (GList) firstUncast;
            GList second = (GList) secondUncast;
            if (sortMechanism.equalsIgnoreCase("alphabetical")) {
                if (second.getName().compareToIgnoreCase(first.getName()) > 0) {
                    return true;
                } return false;
            } else if (sortMechanism.equalsIgnoreCase("edited")) {
                if (first.getEditTime() > second.getEditTime()) {
                    return true;
                } return false;
            } else if (sortMechanism.equalsIgnoreCase("created")) {
                if (first.getCreateTime() > second.getCreateTime()) {
                    return true;
                } return false;
            } else {
                Log.i("sorting comparison", "sortMechanism not specified correctly");
                return true;
            }
        } else if (objectType.equalsIgnoreCase("Food")) {
            Food first = (Food) firstUncast;
            Food second = (Food) secondUncast;
            if (sortMechanism.equalsIgnoreCase("alphabetical")) {
                if (second.getName().compareToIgnoreCase(first.getName()) > 0) {
                    return true;
                } return false;
            } else if (sortMechanism.equalsIgnoreCase("edited")) {
                if (first.getEditTime() > second.getEditTime()) {
                    return true;
                } return false;
            } else if (sortMechanism.equalsIgnoreCase("created")) {
                if (first.getCreateTime() > second.getCreateTime()) {
                    return true;
                } return false;
            } else {
                Log.i("sorting comparison", "sortMechanism not specified correctly");
                return true;
            }
        }
        Log.i("sortingComparison error", "objectType not Food or GList");
        return true;
    }

    private static void swap(ArrayList arrayList, int index1, int index2) {
//        Log.i("SWAPPING", "YES" + index1 + " " + index2);

        Object temp = arrayList.get(index1);
        arrayList.add(index1, arrayList.get(index2));
        arrayList.remove(index1 + 1);
        arrayList.add(index2, temp);
        arrayList.remove(index2 + 1);
    }
}
