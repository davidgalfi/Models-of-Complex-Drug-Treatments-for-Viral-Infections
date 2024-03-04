package org.example;

public class StandardHill implements Efficacy {

    double EC50;

    public StandardHill(double ec50) {
        EC50 = ec50;
    }

    @Override
    public double getEfficacy(double concentration) {
        return 1. / (1. + EC50 / concentration);
    }
}
