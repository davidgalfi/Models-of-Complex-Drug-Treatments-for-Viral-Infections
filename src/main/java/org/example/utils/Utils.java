package org.example.utils;
import java.lang.reflect.Array;
import java.util.ArrayList;
public class Utils {

    public static double[] convertDoubleArrayListToDoubleArray(
            ArrayList<Double> arrayList) {

        double[] doubleArray = new double[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            doubleArray[i] = arrayList.get(i);
        }

        return doubleArray;
    }

    public static <T> T[] convertArrayListToTArray(
            ArrayList<T> arrayList, Class<T> clazz) {
        T[] array = (T[]) Array.newInstance(clazz, arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }

        return array;
    }


    public static void addList(double[] array, ArrayList<Double> arrayList) {
        if (array == null || arrayList == null) {
            throw new IllegalArgumentException("Array or ArrayList cannot be null");
        }

        if (array.length < arrayList.size()) {
            throw new IllegalArgumentException("Array length is not sufficient to accommodate all elements from ArrayList");
        }

        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
    }
}
