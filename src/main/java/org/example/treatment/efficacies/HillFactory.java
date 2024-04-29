package org.example.treatment.efficacies;

import org.json.simple.JSONObject;

public class HillFactory {

    public static Efficacy createHill(JSONObject jsonObject, double molarMass) {

        return new Hill(

            (double) jsonObject.get("EC50"),
            Math.toIntExact((Long) jsonObject.getOrDefault("n", 1)),
            (double) jsonObject.getOrDefault("maxEfficacy", 1.0),
            molarMass
        );
    }
}
