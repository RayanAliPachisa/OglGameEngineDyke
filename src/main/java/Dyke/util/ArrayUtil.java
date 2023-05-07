package Dyke.util;

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

}
