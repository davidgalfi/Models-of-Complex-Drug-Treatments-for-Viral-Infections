package org.example.treatment.drugs.efficacies;

public class NoEffect implements Efficacy {

    public double compute(double concentration) {
        return 0;
    }
}
