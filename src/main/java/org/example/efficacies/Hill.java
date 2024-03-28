package org.example.efficacies;

public class Hill implements Efficacy {

    double EC50;
    int n;
    double maxEfficacy = 1;

    public Hill(double ec50, int n) {
        this.EC50 = ec50;
        this.n = n;
    }

    public Hill(double ec50, int n, double maxEfficacy) {
        this.EC50 = ec50;
        this.n = n;
        this.maxEfficacy = maxEfficacy;
    }

    @Override
    public double getEfficacy(double concentration) {
        return maxEfficacy / (1. + Math.pow(EC50 / concentration, n));
    }
}
