package org.example.treatment.drugs.efficacies;

import java.util.function.Function;

public class Hill implements Efficacy {

    final double EC50; // in [nM]
    final int n;
    final double maxEfficacy;

    final Function<Double, Double> convertToNanoMolars;

    public Hill(double EC50, int n, double maxEfficacy, double molarMass) {

        this.EC50 = EC50;
        this.n =  n;
        this.maxEfficacy = maxEfficacy;

        this.convertToNanoMolars = (c) -> c * Math.pow(10, 3) / molarMass;
    }

    public double compute(double concentration) {

        double concentrationInNanoMolars = convertToNanoMolars.apply(concentration);

        return
                (concentrationInNanoMolars > 0) ?
                        maxEfficacy / (1. + Math.pow(EC50 / concentrationInNanoMolars, n)) :
                        0.0;
    }
}
