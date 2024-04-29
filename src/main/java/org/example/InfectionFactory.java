package org.example;

import org.json.simple.JSONObject;

public class InfectionFactory {

    public static Infection createInfection(JSONObject jsonObject, int xDim, int yDim) {

        return new Infection(
            (double) jsonObject.get("virusProduction"),
            (double) jsonObject.get("virusDiffCoeff"),
            (double) jsonObject.get("virusRemovalRate"),
            (double) jsonObject.get("cellDeathRate"),
            (double) jsonObject.get("infectionRate"),
            xDim,
            yDim
        );
    }
}
