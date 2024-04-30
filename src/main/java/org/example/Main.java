/**
 * The `NirmatrelvirExperiments` class represents a simulation of cellular state space and virus concentration experiments.
 * It provides options to run singular or sweep experiments, with the ability to output collective results.
 */
package org.example;

import org.example.utils.JSONReader;
import org.json.simple.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.function.Function;

public class Main {

    public static class ExperimentAndOutput {
        Experiment experiment; ArrayList<Logger> loggers; ArrayList<Visualizer> visualizers;

        public ExperimentAndOutput(
            Experiment experiment, ArrayList<Logger> loggers, ArrayList<Visualizer> visualizers) {
            this.experiment = experiment; this.loggers = loggers; this.visualizers = visualizers; }
    }

    public static void run(ExperimentAndOutput experimentAndOutput){

        // create callbacks
        ArrayList<Function<Experiment, Void>> callbacks = new ArrayList<>();

        experimentAndOutput.loggers.forEach(logger -> callbacks.add(logger::logState));
        experimentAndOutput.visualizers.forEach(visualizer -> callbacks.add(visualizer::drawExperimentState));

        // loggers: header
        experimentAndOutput.loggers.forEach(logger -> logger.logHeader(experimentAndOutput.experiment));

        // run the simulation
        experimentAndOutput.experiment.run(callbacks);

        // close all outputs
        experimentAndOutput.loggers.forEach(Logger::close);
        experimentAndOutput.visualizers.forEach(Visualizer::close);
    }

    public static ArrayList<Logger> createLoggers(JSONObject jsonObject, String experimentName) {

        ArrayList<Logger> loggers = new ArrayList<>();
        for (Object logger : jsonObject.keySet()) {

            loggers.add(LoggerFactory.createLogger((JSONObject) jsonObject.get(logger), experimentName));
        }

        return loggers;
    }

    public static ArrayList<Visualizer> createVisualizers(JSONObject jsonObject, int xDim, int yDim, String experimentName) {

        ArrayList<Visualizer> visualizers = new ArrayList<>();
        if ((boolean) jsonObject.getOrDefault("enabled", false)) {

            visualizers.add(VisualizerFactory.createVisualizer(jsonObject, xDim, yDim, experimentName));
        }

        return visualizers;
    }

    public static ExperimentAndOutput createOutputs(JSONObject jsonObject, Experiment experiment, String experimentName) {

        return new ExperimentAndOutput(
                experiment,
                createLoggers(
                        (JSONObject) jsonObject.getOrDefault("loggers", new JSONObject()),
                        experimentName
                ),
                createVisualizers(
                        (JSONObject) jsonObject.getOrDefault("visuals", new JSONObject()),
                        experiment.xDim, experiment.yDim,
                        experimentName
                )
        );
    }

    public static ArrayList<ExperimentAndOutput> createExperimentAndOutputs(JSONObject jsonObject) throws URISyntaxException {

        JSONObject experimentsJSONObject = (JSONObject) jsonObject.get("Experiments");

        ArrayList<ExperimentAndOutput> experimentAndOutputs = new ArrayList<>();

        for (Object experimentRawName : experimentsJSONObject.keySet()) {

            String experimentName = (String) experimentRawName;

            JSONObject experimentJSONObject = (JSONObject) experimentsJSONObject.get(experimentName);

            Experiment experiment = ExperimentFactory.createExperiment(
                    experimentJSONObject,
                    (JSONObject) experimentJSONObject.get("Technical"),
                    (JSONObject) experimentJSONObject.get("Infection"),
                    (JSONObject) experimentJSONObject.get("Treatments")
            );

            experimentAndOutputs.add(createOutputs(
                    (JSONObject) experimentJSONObject.getOrDefault("output", new JSONObject()),
                    experiment,
                    experimentName
            ));
        }

        return experimentAndOutputs;
    }

    public static void main(String[] args) throws URISyntaxException {
        String dataFile = "json/datas.json";

        createExperimentAndOutputs(
                JSONReader.getJSONObjectFromJSON(dataFile)
        ).forEach(Main::run);
    }
}
