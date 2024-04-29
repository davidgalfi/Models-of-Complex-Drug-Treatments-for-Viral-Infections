package org.example;

import org.json.simple.JSONObject;
public class VisualizerFactory {

    public static Visualizer createVisualizer(JSONObject jsonObject, int xDim, int yDim) {

        return new Visualizer(xDim, yDim, true, true);
    }
}
