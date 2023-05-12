package Dyke.util;

import Dyke.GameObject.Components.Physics.TransformLinkedFloat;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ArrayUtil {
    public static <T> ArrayList<T> combineArrays(T[] first, T[] second){
        int len = first.length + second.length;
        ArrayList<T> toReturn = new ArrayList<T>();

        for (int i = 0; i < first.length; i++) {
            toReturn.add(first[i]);
        }
        for (int i = first.length; i < first.length + second.length; i++) {
            toReturn.add(second[i - first.length]);
        }
        return toReturn;
    }

    public static int lowerBoundFloatLinkedList(ArrayList<TransformLinkedFloat> array, float key)
    {
        // Initialize starting index and
        // ending index
        int low = 0, high = array.size();
        int mid;

        // Till high does not crosses low
        while (low < high) {

            // Find the index of the middle element
            mid = low + (high - low) / 2;

            // If key is less than or equal
            // to array[mid], then find in
            // left subarray
            if (key <= array.get(mid).f) {
                high = mid;
            }

            // If key is greater than array[mid],
            // then find in right subarray
            else {

                low = mid + 1;
            }
        }

        // If key is greater than last element which is
        // array[n-1] then lower bound
        // does not exists in the array
        if (low < array.size() && array.get(low).f < key) {
            low++;
        }

        // Returning the lower_bound index
        return low;
    }

}
