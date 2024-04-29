package org.example;

import org.example.outputMethods.Visual;
import org.example.outputMethods.VisualToFile;
import org.example.utils.PathAndFile;
import org.json.simple.JSONObject;
public class VisualizerFactory {

    public static Visualizer createVisualizer(JSONObject jsonObject, int xDim, int yDim, String identifier) {

        boolean plotCells = (boolean) jsonObject.getOrDefault("cells", true);
        boolean plotInfectionConcentration = (boolean) jsonObject.getOrDefault("infectionConcentration", true);

        int numberOfFrames = (plotCells && plotInfectionConcentration) ? 2 : 1;

        boolean toFile = (boolean) jsonObject.getOrDefault("toFile", false);

        if (jsonObject.containsKey("filename") || toFile) {

            String path = PathAndFile.generatePathFromIdentifier(identifier);

            String filename = (String) jsonObject.getOrDefault("filename", PathAndFile.generateFileNameFromDateTime());

            return new Visualizer(
                new VisualToFile(path, filename, jsonObject.containsKey("saveInterval") ? (double) jsonObject.get("saveInterval") : null, xDim, yDim, numberOfFrames),
                plotCells, plotInfectionConcentration);
        }

        return new Visualizer(new Visual(xDim, yDim, numberOfFrames), plotCells, plotInfectionConcentration);
    }
}
