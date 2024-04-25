package org.example.efficacies;

import org.example.efficacies.Efficacy;

public class NoEffect implements Efficacy {

    @Override
    public double getEfficacy(double concentration) {
        return 0;
    }
}
