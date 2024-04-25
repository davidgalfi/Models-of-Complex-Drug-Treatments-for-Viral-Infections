package org.example.treatment.efficacies;

import org.json.simple.JSONObject;

public class Hill implements Efficacy {

    double EC50;
    int n;
    double maxEfficacy;

    public Hill(JSONObject jsonObject) {

        this.EC50 = (double) jsonObject.get("EC50");
        this.n = jsonObject.containsKey("n") ? Math.toIntExact((Long) jsonObject.get("n")) : 1;
        this.maxEfficacy = jsonObject.containsKey("maxEfficacy") ? (double) jsonObject.get("maxEfficacy") : 1.0;
    }

    @Override
    public double compute(double concentration) {
        return maxEfficacy / (1. + Math.pow(EC50 / concentration, n));
    }
}
