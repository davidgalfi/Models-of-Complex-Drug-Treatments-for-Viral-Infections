package org.example;

import org.json.simple.JSONObject;
public class VisualizerFactory {

    public static Visualizer createVisualizer(JSONObject jsonObject, int xDim, int yDim) {

        boolean plotCells = (boolean) jsonObject.getOrDefault("cells", true);
        boolean plotInfectionConcentration = (boolean) jsonObject.getOrDefault("infectionConcentration", true);

        return new Visualizer(xDim, yDim, plotCells, plotInfectionConcentration);
    }
}
