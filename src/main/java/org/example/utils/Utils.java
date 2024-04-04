package org.example.utils;
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


}
