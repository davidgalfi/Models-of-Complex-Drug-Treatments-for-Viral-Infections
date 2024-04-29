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

    public static Cells cells;
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
    public static ArrayList<Visuals> visuals;

    /**
     * The FileIO object for writing simulation output to a file.
     */
    public HAL.Tools.FileIO outFile;

    /**
     * The FileIO object for writing simulation parameters to a file.
     */
    public HAL.Tools.FileIO paramFile;

    /**
     * The FileIO object for writing concentration data to a file in the simulation.
     */
    public HAL.Tools.FileIO concentrationsFile;

    /**
     * The output directory path for storing simulation results.
     */
    public String outputDir;

    public static void RunExperiments(){

        ArrayList<Function<Experiment, Void>> callbacks = new ArrayList<>();

        for (Logger logger : loggers) {
            callbacks.add((e) -> logger.logState(e));
        }
        for (Visuals visual : visuals) {
            callbacks.add((e) -> visual.drawExperimentState(e));
        }

        // Singular experiment
        Experiment experiment = new Experiment(
                cells,
                infection,
                treatments,
                technical,
                new Rand(technical.seed));

        for (Logger logger : loggers) {
            logger.logHeader(experiment);
        }

        experiment.run(callbacks);
    }

    /**
     * Generates a directory path for storing collective experiment output based on specified parameters.
     *
     * The generated path includes the current date and time to ensure uniqueness. The directory structure
     * is organized according to the presence of Nirmatrelvir and Ritonavir boosting in the experiment.
     *
     * isNirmatrelvir       A boolean indicating whether Nirmatrelvir is used in the experiment.
     * isRitonavirBoosted   A boolean indicating whether Ritonavir is boosted in the experiment.
     *
     * @return A string representing the directory path for storing collective experiment output.
     *
     * The generated path follows the format: "PWD()/output/NirmatrelvirExperiments/{DrugType}/{DateTime}__collective"
     * - PWD(): The current working directory.
     * - DrugType: Represents the type of drug used. It can be one of the following:
     *   - "noDrug": If isNirmatrelvir is false, indicating the absence of Nirmatrelvir.
     *   - "ritoBoostedNirmatrelvir": If isRitonavirBoosted is true, indicating Ritonavir-boosted Nirmatrelvir.
     *   - "nirmatrelvirOnly": If neither of the above conditions is met, indicating Nirmatrelvir without Ritonavir.
     * - DateTime: Represents the current date and time in the format "yyyy-MM-dd_HH-mm-ss".
     * - "__collective": A suffix indicating that the directory stores collective experiment output.
     *
     * The method also ensures the creation of the specified directory and its parent directories.
     */
    public static String collectiveOutputDir() {
        // Get the current date and time
        java.util.Date now = new java.util.Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String date_time = dateFormat.format(now);

        // Set the base project path
        String projPath = PWD() + "/output/Experiments";

        // Adjust the path based on drug conditions
        projPath += treatments[0].drug.name;

        // Generate the final collective output directory path
        String collectiveOutputDir = projPath + "/" + date_time + "__collective";

        // Create the directory and its parent directories if they do not exist
        new File(collectiveOutputDir).mkdirs();

        // Return the generated directory path
        return collectiveOutputDir;
    }

    public static JSONObject readDatas(String path) throws URISyntaxException {
        return JSONReader.getJSONObjectFromJSON(path);
    }

    /*public static void storeCellsData(JSONObject newExperiment){
        JSONObject cell_ = (JSONObject) newExperiment.get("Cell");
        cells = new Cells(cell_);


    }*/

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

        visuals = new ArrayList<>();
        if ((boolean) visualsJSONObject.getOrDefault("enabled", true)) {

            visuals.add(new Visuals(technical.dim[X], technical.dim[Y]));
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        // Executes Nirmatrelvir experiments
        storeDatas("json/datas.json");
        RunExperiments();
        for (Logger logger : loggers) {
            logger.close();
        }

        for (Visuals visual : visuals) {
            visual.close();
        }
    }
}
