package org.example;

import HAL.GridsAndAgents.AgentGrid2D;
import HAL.GridsAndAgents.PDEGrid2D;

import java.io.File;
import java.util.Objects;

import static HAL.Util.*;

import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

/**
 * The NewExperiment class represents a simulation experiment with individual cells in a 2D square grid.
 * It extends the AgentGrid2D class with agents of type Cells, which represent the individual cells in the simulation.
 * The simulation involves interactions between cells, drug diffusion, virus concentration dynamics, and immune response.
 * The experiment can be run for a specified number of ticks, and the state of the system is visualized at each tick.
 */
public class NewExperiment extends AgentGrid2D<Cells> {

    /**
     * The x-dimension of the grid in the simulation.
     */
    public int x = 200;

    /**
     * The y-dimension of the grid in the simulation.
     */
    public int y = 200;

    /**
     * The visualization scale used in the simulation.
     */
    // TODO: This is currently do nothing, need to figure out what the author wanted to do with it.
    public int visScale = 2;

    /**
     * The number of ticks to delay drug administration in the simulation.
     */
    public int numberOfTicksDelay;

    /**
     * The number of ticks the drug is administered in the simulation.
     */
    public int numberOfTicksDrug;

    /**
     * The total number of ticks for the simulation.
     */
    public int numberOfTicks;

    /**
     * The 2D partial differential equation (PDE) grid representing virus concentration in the simulation.
     */
    public PDEGrid2D virusCon;

    /**
     * The 2D PDE grid representing the immune response level in the simulation.
     * This is similar to interferon concentrations but more generic.
     */
    public PDEGrid2D immuneResponseLevel;

    /**
     * The concentration of the drug in the simulation.
     */
    public double drugCon = 0;

    /**
     * The concentration of the drug in the stomach in the simulation.
     */
    public double drugConStomach = 0;

    /**
     * The random number generator used in the simulation.
     */
    public HAL.Rand rn;

    /**
     * An array storing cellular virus concentrations for each grid location in the simulation.
     */
    // TODO: It seems to be a useless thing, need to delete
    public double[] cellularVirusCon = new double[length];

    /**
     * An array storing cellular immune response levels for each grid location in the simulation.
     */
    // TODO: It seems to be a useless thing, need to delete
    public double[] cellularImmuneResponseLevel = new double[length];

    /**
     * The fixed damage rate in the simulation.
     */
    // TODO: Seems it does not do anything, need find out what is this
    public double fixedDamageRate;

    /**
     * The ratio of healthy cells in the initial configuration of the simulation.
     */
    public double ratioHealthy = 0.9995;

    /**
     * The ratio of infected cells in the initial configuration of the simulation.
     */
    public double ratioInfected = 0.0005;

    /**
     * The ratio of capillary cells in the initial configuration of the simulation.
     */
    // TODO: Implement this line somewhere
    public double ratioCapillaries = 0;

    /**
     * The virus removal rate parameter in the simulation.
     */
    public double virusRemovalRate = 1.67 * Math.pow(10, -3); // mu_V

    /**
     * The maximum virus production parameter in the simulation.
     */
    public double virusMax = 3.72 * Math.pow(10, -3); // f_{i,j}

    /**
     * The infection rate parameter in the simulation.
     */
    public double infectionRate = 1.01 * Math.pow(10, -7); // beta in the ODE

    /**
     * The cell death probability parameter in the simulation.
     */
    public double deathProb = 7.02 * Math.pow(10, -4); // P_D

    /**
     * The virus diffusion coefficient parameter in the simulation.
     */
    public double virusDiffCoeff; // D_V [sigma^2 / min]

    /**
     * The drug object used in the simulation.
     */
    NirmatrelvirDrug drug;

    /**
     * The type of experiment, either "inVivo" or "inVitro".
     */
    String inVivoOrInVitro;

    /**
     * The decay rate of immune response in the simulation.
     */
    // TODO: Seems do nothing
    public double immuneResponseDecay = 0.0005;

    /**
     * The diffusion coefficient of immune response in the simulation.
     */
    // TODO: Seems do nothing
    public double immuneResponseDiffCoeff = 0.1;

    /**
     * The maximum PDE step size in the simulation.
     */
    public double MAX_PDE_STEP = 1;

    /**
     * The threshold value used in the simulation.
     */
    public double threshold = 0.000001;

    /**
     * A boolean indicating whether Nirmatrelvir is used in the simulation.
     */
    // TODO: Seems do nothing
    public boolean isNirmatrelvir = true;

    /**
     * A boolean indicating whether Ritonavir boosting is used in the simulation.
     * Note: This switch only makes sense if isNirmatrelvir is true.
     */
    // TODO: Seems do nothing
    public boolean isRitonavirBoosted = true;

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

    public FirstOrderIntegrator integrator;

    // public FirstOrderDifferentialEquations ode;

    /**
     * Constants representing different cell types or states in the simulation.
     * Each constant corresponds to a unique integer value.
     *
     * H (0): Healthy cell
     */
    public static final int H = 0;

    /**
     * Constants representing different cell types or states in the simulation.
     * Each constant corresponds to a unique integer value.
     *
     * I (1): Infected cell
     */
    public static final int I = 1;

    /**
     * Constants representing different cell types or states in the simulation.
     * Each constant corresponds to a unique integer value.
     *
     * D (2): Dead cell
     */
    public static final int D = 2;

    /**
     * Constants representing different cell types or states in the simulation.
     * Each constant corresponds to a unique integer value.
     *
     * C (3): Capillary (blood vessel)
     */
    public static final int C = 3;


    /**
     * Initializes a new instance of the NewExperiment class with the specified parameters.
     *
     * @param xDim                 The x-dimension of the grid.
     * @param yDim                 The y-dimension of the grid.
     * @param visScale             The visualization scale.
     * @param rn                   A random number generator.
     * @param isNirmatrelvir       A boolean indicating whether Nirmatrelvir is used.
     * @param isRitonavirBoosted   A boolean indicating whether Ritonavir boosting is used.
     * @param numberOfTicksDelay   The number of ticks to delay drug administration.
     * @param virusDiffCoeff       The virus diffusion coefficient.
     * @param inVivoOrInVitro      The type of experiment (inVivo or inVitro).
     * @param fixedDamageRate      The fixed damage rate.
     */
    public NewExperiment(int xDim, int yDim, int visScale, HAL.Rand rn, boolean isNirmatrelvir, boolean isRitonavirBoosted, int numberOfTicksDelay, double virusDiffCoeff, String inVivoOrInVitro, double fixedDamageRate){

        super(xDim, yDim, Cells.class);
        this.x = xDim;
        this.y = yDim;
        this.visScale = visScale;
        this.numberOfTicksDelay = numberOfTicksDelay;
        this.virusDiffCoeff = virusDiffCoeff;
        this.rn = rn;

        this.inVivoOrInVitro = inVivoOrInVitro;
        this.isNirmatrelvir = isNirmatrelvir;
        this.isRitonavirBoosted = isRitonavirBoosted;

        this.fixedDamageRate = fixedDamageRate;

        if (inVivoOrInVitro.equals("inVivo")) {

            this.drug = new NirmatrelvirDrug(isRitonavirBoosted);
            this.numberOfTicksDrug = 5 * 24 * 60; // we administer paxlovid for 5 days, i.e. 5*24*60 minutes

        } else {

            this.drug = new NirmatrelvirDrug(5.0, isNirmatrelvir);
            this.numberOfTicksDrug = 4 * 24 * 60; // we incubate for 4 days

        }

        // this.ode = new VirusDiffEquation(virusRemovalRate, drugVirusRemovalEff, immuneVirusRemovalEff);
        this.integrator = new DormandPrince54Integrator(1e-8, 100, 1e-10, 1e-10);

        virusCon = new HAL.GridsAndAgents.PDEGrid2D(xDim, yDim);
        immuneResponseLevel = new HAL.GridsAndAgents.PDEGrid2D(xDim, yDim);
        virusCon.Update();
        immuneResponseLevel.Update();

        outputDir = this.OutputDirectory();
        outFile = new HAL.Tools.FileIO(outputDir.concat("/").concat("Out").concat(".csv"), "w");
        paramFile = new HAL.Tools.FileIO(outputDir.concat("/").concat("Param").concat(".csv"), "w");
        concentrationsFile = new HAL.Tools.FileIO(outputDir.concat("/").concat("concentrations").concat(".csv"), "w");

    }

    /**
     * Initializes the simulation by setting up the grid and initializing cells based on specified ratios.
     * This method writes the header for output files and populates the grid with cells based on random values
     * and predefined ratios for healthy, infected, and capillary cells.
     */
    public void Init(){

        WriteHeader();

        for (int i = 0; i < length; i++){
            double randomValue = rn.Double();

            if (randomValue < ratioHealthy){
                Cells c = NewAgentSQ(i);
                c.cellInit(true,false, false, false);
            }
            else if(randomValue > ratioHealthy && randomValue < ratioHealthy + ratioInfected ) {
                Cells c = NewAgentSQ(i);
                c.cellInit(false,true, false, false);
            }
            else {
                Cells c = NewAgentSQ(i);
                c.cellInit(false,false, false, true);
            }
        }
    }

    /**
     * Runs the simulation for a specified number of ticks, updating the model and visualizing it at each step.
     * The method also monitors and reports specific events during the simulation, such as reaching a fixed damage rate.
     *
     * @param win The GUI window for visualization.
     * @return The count of healthy cells at the end of the simulation.
     */
    public double RunExperiment(HAL.Gui.GridWindow win) {
        double[] cellCounts = CountCells();

        for (int tick = 0; tick < this.numberOfTicks; tick++) {
            // Check for a specific event and adjust simulation parameters if necessary
            // TODO: Permanently not working, need to find a way to use it.
            if ((numberOfTicksDelay == NirmatrelvirExperiments.BIG_VALUE) && (fixedDamageRate <= 100) &&
                    (((cellCounts[1] + cellCounts[2]) / 40000) * 100 >= fixedDamageRate)) {
                this.numberOfTicksDelay = tick - 1;
                this.numberOfTicks = this.numberOfTicksDelay + this.numberOfTicksDrug;
                System.out.println("Diffusion coeff.: " + this.virusDiffCoeff + ". Damage info: " +
                        fixedDamageRate + " percent damage was found at tick " + numberOfTicksDelay + ".");
            }

            // Progress the simulation by one time step
            TimeStep(tick);
            DrawModel(win);

            // Capture and save snapshots at regular intervals
            // TODO: Permanently does not work, need to find a way to use it.
            if (tick > 0 && ((tick % (24 * 60)) == 0)) {
                win.ToPNG(outputDir + "day" + Integer.toString(tick / (24 * 60)) + ".jpg");
            }

            // Record and write data for analysis
            double totalVirusCon = TotalVirusCon();
            double totalImmuneResponseLevel = TotalImmuneResponseLevel();
            cellCounts = CountCells();
            concentrationsFile.Write(totalVirusCon + "," + totalImmuneResponseLevel + "," +
                    drugCon + "," + drugConStomach + "\n");
            outFile.Write(tick + "," + cellCounts[0] + "," + cellCounts[1] +
                    "," + cellCounts[2] + "," + totalVirusCon + "," + drugCon + "," + drugConStomach + "\n");
        }

        // Close output files and return the count of healthy cells
        CloseFiles();
        return cellCounts[0];
    }

    double[] CountCells(){

        double healthyCells = 0, infectedCells = 0, deadCells = 0,
                capillaryCells = 0;
        double[] cellCount = new double[4];
        for (Cells cell: this){
            if (cell.cellType == H){
                healthyCells += 1;
            }
            else if (cell.cellType == I ){
                infectedCells += 1;
            }
            else if (cell.cellType == D){
                deadCells += 1;
            }
            else if (cell.cellType == C){
                capillaryCells += 1;
            }
        }

        cellCount[H] = healthyCells;
        cellCount[I] = infectedCells;
        cellCount[D] = deadCells;
        cellCount[C] = capillaryCells;

        return cellCount;
    }

    /**
     * Counts the number of cells in each state (healthy, infected, dead, capillary) in the model.
     *
     * @return An array containing the count of cells for each state.
     */
    // TODO: Currently not working (the immune system does not react)
    void TimeStepImmune(int tick){

        // decay of the immuneResponseLevel
        for (Cells cell : this){
            double immuneResponseNow = immuneResponseLevel.Get(cell.Isq());
            immuneResponseLevel.Add(cell.Isq(), -immuneResponseDecay * immuneResponseNow);
        }
        immuneResponseLevel.Update();

        // immune response level increases
        for (Cells cell : this){
            if (cell.cellType == I){ // infected cells produce interferon
                double addedImmuneResponseLevel = ImmuneResponseSource(tick, cell);
                double currentImmuneResponseLevel = immuneResponseLevel.Get(cell.Isq());
                double newImmuneResponseLevel = addedImmuneResponseLevel + currentImmuneResponseLevel;
                immuneResponseLevel.Set(cell.Isq(), newImmuneResponseLevel);
            }
        }

        immuneResponseLevel.DiffusionADI(immuneResponseDiffCoeff);
        immuneResponseLevel.Update();

    }

    /**
     * Performs a time step for the virus-related processes, including virus decay, removal, and production.
     *
     * @param tick The current time step.
     */
    void TimeStepVirus(int tick) {
        // Decay of the virus
        for (Cells cell : this) {
            // TODO: Drug currently does not react
            double drugVirusRemovalEff = 0.0 * drugCon; // Nirmatrelvir has 0 effect on virus removal
            double immuneVirusRemovalEff = 1 / (1 + 1 / (Math.pow(immuneResponseLevel.Get(cell.Isq()), 2)));

            // TODO: Resolve with Apache solver
            /*virusCon.Add(cell.Isq(), -virusRemovalRate * virusCon.Get(cell.Isq()));
            virusCon.Add(cell.Isq(), -drugVirusRemovalEff * virusCon.Get(cell.Isq()));
            virusCon.Add(cell.Isq(), -immuneVirusRemovalEff * virusCon.Get(cell.Isq()));*/
            double currentCell = virusCon.Get(cell.Isq());
            virusCon.Add(cell.Isq(), currentCell*(-virusRemovalRate - drugVirusRemovalEff - immuneVirusRemovalEff));

            /*FirstOrderDifferentialEquations ode = new VirusDiffEquation(virusRemovalRate, drugVirusRemovalEff, immuneVirusRemovalEff);

            double[] y = {currentCell};
            integrator.integrate(ode, 0, y, 1, y);
            virusCon.Add(cell.Isq(), y[0]);*/
        }
        virusCon.Update();

        // Virus production by infected cells
        for (Cells cell : this) {
            if (cell.cellType == I) { // Infected cell
                double addedVirusCon = VirusSource();
                double currentVirusCon = virusCon.Get(cell.Isq());
                double newVirusCon = addedVirusCon + currentVirusCon;
                virusCon.Set(cell.Isq(), newVirusCon);
            }
        }

        virusCon.DiffusionADI(virusDiffCoeff);
        virusCon.Update();
    }


    /**
     * Performs a time step for drug-related processes, including drug decay and movement between stomach and lung epithelial cells.
     *
     * @param tick The current time step.
     */
    void TimeStepDrug(int tick) {
        if (this.inVivoOrInVitro.equals("inVitro")) {
            this.drugCon = this.drug.inVitroDrugCon;
        } else if (this.inVivoOrInVitro.equals("inVivo")) {
            // Decay of the drug
            this.drugCon -= this.drug.drugDecay * this.drugCon;

            // Decay of the drug in the stomach and appearance in lung epithelial cells
            double transferQuantity = this.drug.drugDecayStomach * this.drugConStomach;
            this.drugCon += transferQuantity;
            this.drugConStomach -= transferQuantity;

            // Drug appearance in the stomach
            this.drugConStomach += DrugSourceStomach(tick);
        } else {
            System.out.println("inVitro and inVivo are the only two choices currently.");
        }
    }


    /**
     * Performs a time step for cell-related processes, including infection and cell death.
     *
     * @param tick The current time step.
     */
    void TimeStepCells(int tick) {
        for (Cells cell : this) {
            cell.cellInfection();
        }
        for (Cells cell : this) {
            cell.cellDeath();
        }
    }


    /**
     * Performs a time step for all components of the experiment, including immune response,
     * drug dynamics, virus dynamics, and cell-related processes.
     *
     * @param tick The current time step.
     */
    void TimeStep(int tick) {
        TimeStepImmune(tick);
        TimeStepDrug(tick);
        TimeStepVirus(tick);
        TimeStepCells(tick);
    }


    /**
     * Calculates the total virus concentration across all cells.
     *
     * @return The total virus concentration.
     */
    double TotalVirusCon() {
        double totalVirusCon = 0;
        /*for (int i = 0; i < length; i++) {
            // TODO: Figure out what this line is doing
            cellularVirusCon[i] = virusCon.Get(i);
        }*/
        for (double virusConInCell : virusCon.GetField()) {
            totalVirusCon += virusConInCell;
        }
        return totalVirusCon;
    }


    /**
     * Calculates the total immune response level across all cells.
     *
     * @return The total immune response level.
     */
    double TotalImmuneResponseLevel() {
        double totalImmuneResponseLevel = 0;
        /*for (int i = 0; i < length; i++) {
            cellularImmuneResponseLevel[i] = immuneResponseLevel.Get(i);
        }*/
        for (double immuneResponseInCell : immuneResponseLevel.GetField()) {
            totalImmuneResponseLevel += immuneResponseInCell;
        }
        return totalImmuneResponseLevel;
    }


    /**
     * Calculates the virus source based on the maximum virus concentration and drug effectiveness.
     *
     * @return The virus source.
     */
    double VirusSource() {
        return virusMax * (1 - this.drug.DrugVirusProdEff(this.drugCon));
    }


    /**
     * Calculates the immune response source for a given tick and cell.
     *
     * @param tick The current tick of the simulation.
     * @param cell The cell for which the immune response is calculated.
     * @return The immune response source.
     */
    double ImmuneResponseSource(int tick, Cells cell) {
        // TODO: Currently, the immune system does not react.
        return 0.0 * Math.pow(10,-3);
    }


    /**
     * Calculates the drug source in the stomach for a given tick.
     *
     * @param tick The current tick of the simulation.
     * @return The drug source in the stomach.
     */
    double DrugSourceStomach(int tick) {
        if ((tick > numberOfTicksDelay) && (isNirmatrelvir == true) && (((tick - numberOfTicksDelay) % (12 * 60)) == 1)) {
            return this.drug.drugSourceStomach;
        } else {
            return 0.0;
        }
    }


    /**
     * Writes the header information to the parameter file and output file.
     */
    void WriteHeader() {
        paramFile.Write("Parameters: \n init. ratio of healthy cells, virus removal rate, diff. coeff. \n");
        paramFile.Write(this.ratioHealthy + "," + this.virusRemovalRate + "," + this.virusDiffCoeff + "\n");
        outFile.Write("tick, healthy cells, infected cells, dead cells, "
                + "total virus conc., total drug conc., total drug conc. transfer \n");
    }


    /**
     * Closes the output files.
     */
    void CloseFiles() {
        outFile.Close();
        paramFile.Close();
        concentrationsFile.Close();
    }


    /**
     * Generates the output directory path based on experiment parameters and creates the directory.
     *
     * This method creates a directory path for the output of a simulation experiment, taking into account various
     * parameters such as drug type, drug concentration, in vivo or in vitro conditions, virus diffusion coefficient,
     * and fixed damage rate. The directory structure is organized by experiment type and timestamp, with subdirectories
     * created based on specific conditions such as drug type, in vivo conditions, and damage rate or delay time.
     *
     * @return The generated output directory path as a string.
     */
    String OutputDirectory() {
        // Get the current date and time
        java.util.Date now = new java.util.Date();

        // Format the date and time
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String date_time = dateFormat.format(now);

        // Define the base project path for experiment outputs
        String projPath = PWD() + "/output/NirmatrelvirExperiments";

        // Append subdirectories based on drug type and boosted status
        if (!this.isNirmatrelvir) {
            projPath += "/noDrug";
        } else if (this.isRitonavirBoosted) {
            projPath += "/ritoBoostedNirmatrelvir";
        } else {
            projPath += "/nirmatrelvirOnly";
        }

        // Determine the drug information based on experiment conditions
        double drugInfo = (!this.isNirmatrelvir) ? 0.0 :
                (this.inVivoOrInVitro.equals("inVitro")) ? this.drug.NgPerMlToNanomolars(this.drug.inVitroDrugCon) :
                        this.drug.drugSourceStomach;

        // Generate the output directory path
        String outputDir = projPath + "/" + date_time + this.inVivoOrInVitro + drugInfo + "__diff" + this.virusDiffCoeff;

        // Append subdirectories based on fixed damage rate or delay time
        if (this.fixedDamageRate < 100.0) {
            outputDir += "__damagerate" + this.fixedDamageRate + "/";
        } else {
            outputDir += "__delaytime" + this.numberOfTicksDelay + "/";
        }

        // Create the output directory and its parent directories if they do not exist
        new File(outputDir).mkdirs();

        // Return the generated output directory path
        return outputDir;
    }


    /**
     * Draws the current state of the model on the visualization grid window.
     *
     * This method iterates through each agent in the model, retrieves its information, and sets the corresponding
     * visualization pixel on the grid window. Healthy cells are represented by a green color, infected cells by a
     * purple color, dead cells by black, and capillary cells by white. Additionally, the concentration of the virus is
     * visualized using a heat map on the same grid window.
     *
     * @param vis The grid window used for visualization.
     */
    void DrawModel(HAL.Gui.GridWindow vis) {
        for (int i = 0; i < length; i++) {
            Cells drawMe = GetAgent(i);

            // Check if the agent is null (empty grid cell)
            if (drawMe == null) {
                vis.SetPix(i, RGB256(255, 255, 255)); // Set empty cell color to white
            } else {
                // Set colors based on cell type
                if (drawMe.cellType == H) {        // Healthy cells
                    vis.SetPix(i, RGB256(119, 198, 110)); // Set healthy cell color to green
                } else if (drawMe.cellType == I) { // Infected cells
                    vis.SetPix(i, RGB256(124, 65, 120));  // Set infected cell color to purple
                } else if (drawMe.cellType == D) { // Dead cells
                    vis.SetPix(i, RGB(0, 0, 0)); // Set dead cell color to black
                } else if (drawMe.cellType == C) { // Capillary cells
                    vis.SetPix(i, RGB(255, 255, 255)); // Set capillary cell color to white
                }
            }

            // Visualize virus concentration using a heat map
            vis.SetPix(ItoX(i) + xDim, ItoY(i), HeatMapRBG(virusCon.Get(i)));
        }
    }
}
