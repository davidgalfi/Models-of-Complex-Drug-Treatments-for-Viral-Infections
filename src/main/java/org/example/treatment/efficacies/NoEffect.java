package org.example.treatment.efficacies;

public class NoEffect implements Efficacy {

    @Override
    public double compute(double concentration) {
        return 0;
    }
}
