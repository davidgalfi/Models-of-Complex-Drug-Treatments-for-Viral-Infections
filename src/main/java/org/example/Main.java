/**
 * The `NirmatrelvirExperiments` class represents a simulation of cellular state space and virus concentration experiments.
 * It provides options to run singular or sweep experiments, with the ability to output collective results.
 */
package org.example;

import HAL.Gui.GridWindow;
import HAL.Rand;
import HAL.Tools.FileIO;
import org.json.simple.JSONObject;

import java.io.File;
import java.net.URISyntaxException;

import static HAL.Util.PWD;

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
    public static String singularOrSweep = "singular"; // "singular" or "sweep"

    public static Cells cells;
    public static Drug drug;
    public static Immune immune;
    public static Virus virus;

    /**
     * Represents the grid window for visualizing the cellular state space and virus concentration.
     * The window title is set to "Cellular state space, virus concentration."
     * The dimensions are determined by doubling the horizontal dimension (x * 2), the vertical dimension (y),
     * and the visualization scale (visScale), with additional information about the grid's toroidal nature (true).
     */
    public static GridWindow win;

    /**
     * Executes Nirmatrelvir experiments based on the specified experiment type.
     * If the experiment type is "singular," a single experiment is conducted,
     * whereas if the type is "sweep," a parameter sweep is performed.
     *
     * For "singular" experiments:
     * 1. Initializes a new experiment with the given parameters.
     * 2. Sets the number of ticks based on delay and drug duration.
     * 3. Initializes the experiment.
     * 4. Runs the experiment, obtaining the remaining count of healthy cells.
     * 5. Outputs the results to the console, including drug information if applicable.
     *
     * If the experiment type is neither "singular" nor "sweep," an error message is printed.
     *
     * The visualization window is closed after completing the experiments.
     *
     * singularOrSweep The type of experiment to perform ("singular" or "sweep").
     * inVivoOrInVitro The context of the experiment ("inVivo" or "inVitro").
     * win Represents the grid window for visualizing the cellular state space and virus concentration.
     * y               The height of the grid space.
     * x               The width of the grid space.
     * visScale        The visualization scale factor.
     * isNirmatrelvir  A boolean indicating whether Nirmatrelvir is used.
     * isRitonavirBoosted A boolean indicating whether Ritonavir is boosted.
     *
     * Conducts a parameter sweep experiment based on the specified parameters.
     *
     * For each combination of virus diffusion coefficient and damage rate:
     * 1. Initializes a new experiment with the given parameters.
     * 2. Sets the number of ticks based on delay and drug duration.
     * 3. Initializes the experiment.
     * 4. Runs the experiment, obtaining the remaining count of healthy cells.
     * 5. Records the results for each combination.
     * 6. Writes the collective results to a CSV file and prints them to the console.
     *
     * isNirmatrelvir        A boolean indicating whether Nirmatrelvir is used.
     * isRitonavirBoosted    A boolean indicating whether Ritonavir is boosted.
     * x                    The width of the grid space.
     * y                    The height of the grid space.
     * visScale             The visualization scale factor.
     * inVivoOrInVitro      The context of the experiment ("inVivo" or "inVitro").
     * BIG_VALUE            A constant representing a large numerical value.
     * singularOrSweep      The type of experiment to perform ("singular" or "sweep").
     * collectiveOutFile    The output file for storing collective results.
     * collectiveResults    The string containing collective results.
     * win                  The visualization window.
     */
    public static void RunExperiments(){
        if (singularOrSweep.equals("singular")) {
            // Singular experiment
            Experiment experiment = new Experiment(
                    cells,
                    new Rand(1),
                    drug,
                    immune,
                    12 * 60,
                    virus,
                    110.0);
            experiment.numberOfTicks = experiment.numberOfTicksDelay + experiment.numberOfTicksDrug;
            experiment.Init();
            experiment.RunExperiment(win);
        } else if (singularOrSweep.equals("sweep")) {
            // Sweep experiment
            String collectiveOutputDir = collectiveOutputDir();

            // Sweep parameters
            for (double virusDiffCoeffSweep = 0.00625; virusDiffCoeffSweep < 50; virusDiffCoeffSweep *= 2) {

                for (double damageRateSweep = 0.0; damageRateSweep < 100.0; damageRateSweep += 5.0) {
                    Experiment experiment = new Experiment(
                            cells,
                            new Rand(1),
                            drug,
                            immune,
                            BIG_VALUE,
                            virus,
                            damageRateSweep);
                    experiment.numberOfTicks = experiment.numberOfTicksDelay + experiment.numberOfTicksDrug;
                    experiment.Init();
                    experiment.RunExperiment(win);
                }
            }
        } else {
            System.out.println("The only options are singular and sweep.");
        }

        // Close visualization window
        win.Close();
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
        String projPath = PWD() + "/output/NirmatrelvirExperiments";

        // Adjust the path based on drug conditions
        if (!drug.name.equals("Nirmatrelvir")) {
            projPath += "/noDrug";
        } else if (drug.isRitonavirBoosted) {
            projPath += "/ritoBoostedNirmatrelvir";
        } else {
            projPath += "/nirmatrelvirOnly";
        }

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

    public static void storeCellsData(JSONObject newExperiment){
        JSONObject cell_ = (JSONObject) newExperiment.get("Cell");
        cells = new Cells(cell_);


    }

    public static void storeDrugData(JSONObject newExperiment){
        JSONObject drug_ = (JSONObject) newExperiment.get("Drug");
        drug = new Drug(drug_);
    }

    public static void storeImmuneData(JSONObject newExperiment){
        JSONObject immune_ = (JSONObject) newExperiment.get("Immune");
        immune = new Immune(immune_);
    }

    public static void storeVirusData(JSONObject newExperiment){
        JSONObject virus_ = (JSONObject) newExperiment.get("Virus");
        virus = new Virus(virus_);
    }

    public static void storeDatas(String path) throws URISyntaxException {
        JSONObject jsonObject = readDatas(path);
        JSONObject newExperiment = (JSONObject) jsonObject.get("NewExperiment");
        storeCellsData(newExperiment);
        storeDrugData(newExperiment);
        storeImmuneData(newExperiment);
        storeVirusData(newExperiment);
    }

    public static void createWin(){
        win = new GridWindow(
                "Cellular state space, virus concentration.",
                cells.xDim * 2, cells.yDim, cells.visScale, true);
    }
    public static void main(String[] args) throws URISyntaxException {
        // Executes Nirmatrelvir experiments
        storeDatas("json/datas.json");
        createWin();
        RunExperiments();
    }
}
