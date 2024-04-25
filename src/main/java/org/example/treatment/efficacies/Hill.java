package org.example.treatment.efficacies;

import java.util.function.Function;
import org.json.simple.JSONObject;

public class Hill implements Efficacy {

    double EC50; // in [nM]
    int n;
    double maxEfficacy;

    Function<Double, Double> convertToNanoMolars;

    public Hill(JSONObject jsonObject, double molarMass) {

        this.EC50 = (double) jsonObject.get("EC50");
        this.n = jsonObject.containsKey("n") ? Math.toIntExact((Long) jsonObject.get("n")) : 1;
        this.maxEfficacy = jsonObject.containsKey("maxEfficacy") ? (double) jsonObject.get("maxEfficacy") : 1.0;

        this.convertToNanoMolars = (c) -> c * Math.pow(10, 3) / molarMass;
    }

    @Override
    public double compute(double concentration) {

        double concentrationInNanoMolars = convertToNanoMolars.apply(concentration);
        return maxEfficacy / (1. + Math.pow(EC50 / concentrationInNanoMolars, n));
    }
}
