package org.example;

public class NoEffect implements Efficacy {


    @Override
    public double getEfficacy(double concentration) {
        return 0;
    }
}
