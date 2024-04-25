package org.example.treatment.efficacies;

public class NoEffect implements Efficacy {

    @Override
    public double getEfficacy(double concentration) {
        return 0;
    }
}
