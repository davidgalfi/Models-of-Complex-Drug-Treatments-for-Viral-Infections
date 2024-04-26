package org.example.differentialEquation.operatorSplitting;

import org.json.simple.JSONObject;

public class OperatorSplittingFactory {

    public static OperatorSplitting createSplitting(Double timeStep, JSONObject jsonObject) {

        String splittingMethod = ((String) jsonObject.getOrDefault("operatorSplitting", "sequential"));

        if (splittingMethod.equals("marchuk-strang")) {

            return new MarchukStrangSplitting(timeStep);
        } else {

            return new SequentialSplitting(timeStep);
        }
    }
}
