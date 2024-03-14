package org.example.efficacies;

public class NotStandardSigmoid implements Efficacy{
    @Override
    public double getEfficacy(double concentration) {
        return 1 / (1 + 1 / (Math.pow(concentration, 2)));
    }
}
