/**
 * The `NirmatrelvirExperiments` class represents a simulation of cellular state space and virus concentration experiments.
 * It provides options to run singular or sweep experiments, with the ability to output collective results.
 */
package org.example;

import HAL.Rand;
import org.example.utils.JSONReader;
import org.example.utils.Utils;
import org.json.simple.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.function.Function;

import static HAL.Util.PWD;
import static org.example.Technical.*;

/**
 * The NirmatrelvirExperiments class orchestrates and conducts experiments related to the cellular state space and virus concentration simulation.
 * It includes constants, properties, and methods for both singular and parameter sweep experiments, along with visualization and data output functionalities.
 */

public class Main {

    /**
     * A constant representing a large integer value, calculated as (Integer.MAX_VALUE-1) / 2.
     */
    public static final int BIG_VALUE = (Integer.MAX_VALUE - 1) / 2;

    /**
     * Specifies whether the experiment is set to a singular or sweep condition.
     * Possible values are "singular" or "sweep".
     */

    public static Infection infection;
    public static Treatment[] treatments;
    public static Technical technical;

    public static Logger[] loggers;

    /**
     * Represents the grid window for visualizing the cellular state space and virus concentration.
     * The window title is set to "Cellular state space, virus concentration."
     * The dimensions are determined by doubling the horizontal dimension (x * 2), the vertical dimension (y),
     * and the visualization scale (visScale), with additional information about the grid's toroidal nature (true).
     */
    public static ArrayList<Visualizer> visualizers;

    /**
     * The output directory path for storing simulation results.
     */
    public String outputDir;

    public static void RunExperiments(){

        ArrayList<Function<Experiment, Void>> callbacks = new ArrayList<>();

        for (Logger logger : loggers) {
            callbacks.add(logger::logState);
        }
        for (Visualizer visualizer : visualizers) {
            callbacks.add(visualizer::drawExperimentState);
        }

        // Singular experiment
        Experiment experiment = new Experiment(
                infection,
                treatments,
                technical);

        for (Logger logger : loggers) {
            logger.logHeader(experiment);
        }

        experiment.run(callbacks);
    }

    public static JSONObject readDatas(String path) throws URISyntaxException {
        return JSONReader.getJSONObjectFromJSON(path);
    }

    public static void storeTreatmentsData(JSONObject newExperiment, Technical technical_){
        JSONObject treatments_ = (JSONObject) newExperiment.get("Treatments");

        ArrayList<Treatment> treatmentsArrayList = new ArrayList<>();

        int numberOfTreatment_ = 1;
        String treatmentName_ = "Treatment_" + Integer.toString(numberOfTreatment_);

        while (treatments_.containsKey(treatmentName_)){
            JSONObject currentTreatment = (JSONObject) treatments_.get(treatmentName_);
            treatmentsArrayList.add(TreatmentFactory.createTreatment(currentTreatment, technical_.simulationTime, technical_.timeStep));

            numberOfTreatment_++;
            treatmentName_ = "Treatment_" + Integer.toString(numberOfTreatment_);
        }

        treatments = new Treatment[treatmentsArrayList.size()];
        treatments = Utils.convertArrayListToTArray(treatmentsArrayList, Treatment.class);
    }

    public static void storeInfectionData(JSONObject newExperiment, Technical technical_){
        JSONObject infection_ = (JSONObject) newExperiment.get("Infection");
        infection = InfectionFactory.createInfection(infection_, technical_.dim[X], technical_.dim[Y]);
    }

    public static void storeTechnicalData(JSONObject newExperiment){
        JSONObject technical_ = (JSONObject) newExperiment.get("Technical");
        technical = new Technical(technical_);
    }

    //TODO: create array to work with multiple experiments
    public static void storeDatas(String path) throws URISyntaxException {
        JSONObject jsonObject = readDatas(path);
        JSONObject Experiments = (JSONObject) jsonObject.get("Experiments");

        String experimentName = "Experiment_1";
        JSONObject newExperiment = (JSONObject) Experiments.get(experimentName);

        //storeCellsData(newExperiment);
        storeTechnicalData(newExperiment);
        storeTreatmentsData(newExperiment, technical);
        storeInfectionData(newExperiment, technical);

        JSONObject outputJSONObject = (JSONObject) newExperiment.getOrDefault("output", new JSONObject());

        JSONObject loggersJSONObject = (JSONObject) outputJSONObject.getOrDefault("loggers", new JSONObject());

        ArrayList<Logger> loggersArrayList = new ArrayList<>();
        for (Object logger : loggersJSONObject.keySet()) {

            loggersArrayList.add(LoggerFactory.createLogger((JSONObject) loggersJSONObject.get(logger), experimentName));
        }

        loggers = new Logger[loggersArrayList.size()];
        loggers = Utils.convertArrayListToTArray(loggersArrayList, Logger.class);

        JSONObject visualsJSONObject = (JSONObject) outputJSONObject.getOrDefault("visuals", new JSONObject());

        visualizers = new ArrayList<>();
        if ((boolean) visualsJSONObject.getOrDefault("enabled", false)) {

            visualizers.add(VisualizerFactory.createVisualizer(visualsJSONObject, technical.dim[X], technical.dim[Y], experimentName));
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        // Executes Nirmatrelvir experiments
        storeDatas("json/datas.json");
        RunExperiments();
        for (Logger logger : loggers) {
            logger.close();
        }

        for (Visualizer visualizer : visualizers) {
            visualizer.close();
        }
    }
}
