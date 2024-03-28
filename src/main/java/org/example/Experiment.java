package org.example;

import HAL.GridsAndAgents.AgentGrid2D;
import HAL.GridsAndAgents.PDEGrid2D;

import static HAL.Util.*;
import static org.example.Cells.*;

import HAL.Rand;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator;
import org.example.treatment.Drug;

/**
 * The NewExperiment class represents a simulation experiment with individual cells in a 2D square grid.
 * It extends the AgentGrid2D class with agents of type Cells, which represent the individual cells in the simulation.
 * The simulation involves interactions between cells, drug diffusion, virus concentration dynamics, and immune response.
 * The experiment can be run for a specified number of ticks, and the state of the system is visualized at each tick.
 */
public class Experiment extends AgentGrid2D<Cells> {

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
     * The random number generator used in the simulation.
     */
    public HAL.Rand rn;

    /**
     * The fixed damage rate in the simulation.
     */
    // TODO: Seems it does not do anything, need find out what is this
    public double fixedDamageRate;

    /**
     * The infection rate parameter in the simulation.
     */
    public double infectionRate = 1.01 * Math.pow(10, -7); // beta in the ODE

    /**
     * The concentration of the drug in the simulation.
     */
    public double drugCon = 0;

    /**
     * The cell death probability parameter in the simulation.
     */
    public double deathProb =  7.02 * Math.pow(10, -4); // P_D

    /**
     * The ratio of healthy cells in the initial configuration of the simulation.
     */
    public double ratioHealthy = 0.9995;

    /**
     * The ratio of infected cells in the initial configuration of the simulation.
     */
    public double ratioInfected = 0.0005;


    /**
     * The drug object used in the simulation.
     */
    Drug drug;
    Virus virus;
    Cells cells;
    Immune immune;
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

    public Experiment(Cells cells,
                      Rand rn,
                      Drug drug,
                      Immune immune,
                      int numberOfTicksDelay,
                      Virus virus,
                      double fixedDamageRate){

        super(cells.xDim, cells.yDim, Cells.class);

        this.numberOfTicksDelay = numberOfTicksDelay; //  parameter that represents the number of time steps (or ticks) to delay the administration of the drug in the simulation.
        this.rn = rn;
        this.fixedDamageRate = fixedDamageRate;


        this.drug = drug;
        this.virus = virus;
        this.cells = cells;
        this.immune = immune;

        setupDrugAdministrationProtocol();

        // this.ode = new VirusDiffEquation(virusRemovalRate, drugVirusRemovalEff, immuneVirusRemovalEff);
        this.integrator = new DormandPrince54Integrator(10, 10, 10, 10);

        virusCon = new PDEGrid2D(xDim, yDim);
        immuneResponseLevel = new PDEGrid2D(xDim, yDim);

        updateFields(virusCon, immuneResponseLevel);
    }



    // Main functions



    /**
     * Initializes the simulation by setting up the grid and initializing cells based on specified ratios.
     * This method writes the header for output files and populates the grid with cells based on random values
     * and predefined ratios for healthy, infected, and capillary cells.
     */
    public void init(){

        for (int i = 0; i < length; i++){
            double randomValue = rn.Double();

            if (randomValue < ratioHealthy){
                Cells c = NewAgentSQ(i);
                c.cellInit(true,false, false, false);
            }
            else if(randomValue > ratioHealthy && randomValue < ratioHealthy + ratioInfected) {
                Cells c = NewAgentSQ(i);
                c.cellInit(false,true, false, false);
            }
        }
    }

    /**
     * Runs the simulation for a specified number of ticks, updating the model and visualizing it at each step.
     * The method also monitors and reports specific events during the simulation, such as reaching a fixed damage rate.
     *
     * @param win The GUI window for visualization.
     */
    public void runExperiment(HAL.Gui.GridWindow win) {
        double[] cellCounts = countCells();

        for (int tick = 0; tick < this.numberOfTicks; tick++) {

            // Progress the simulation by one time step
            TimeStep(tick);
            DrawModel(win);
        }
    }

    /**
     * Performs a time step for all components of the experiment, including immune response,
     * drug dynamics, virus dynamics, and cell-related processes.
     *
     * @param tick The current time step.
     */
    void TimeStep(int tick) {
        TimeStepVirus(tick);
        TimeStepImmune(tick);
        TimeStepDrug(tick);
        TimeStepCells(tick);
    }

    /**
     * Performs a time step for the virus-related processes, including virus decay, removal, and production.
     *
     * @param tick The current time step.
     */
    void TimeStepVirus(int tick) {
        // Decay of the virus
        for (Cells cell : this) {

            // Drug reaction
            double drugVirusRemovalEff = drug.drugVirusRemovalEff.getEfficacy(drugCon);
            double immuneVirusRemovalEff = drug.immuneVirusRemovalEff.getEfficacy(immuneResponseLevel.Get(cell.Isq()));

            double currentCell = virusCon.Get(cell.Isq());

            // TODO: Resolve with Apache solver
            virusCon.Add(cell.Isq(), currentCell*(-immune.virusRemovalRate - drugVirusRemovalEff - immuneVirusRemovalEff));

            /*double[] y = {currentCell};
            integrator.integrate(new VirusDiffEquation(immune.virusRemovalRate, drugVirusRemovalEff, immuneVirusRemovalEff),
                    0, y, 1, y);
            virusCon.Add(cell.Isq(), y[0]);*/
        }

        // Virus production by infected cells
        doVirusProduction();

        performDiffusion(virusCon, virus.virusDiffCoeff);

        updateFields(virusCon);
    }

    void TimeStepImmune(int tick){

        // decay of the immuneResponseLevel
        decayImmunResponseLevel();

        // immune response level increases
        increaseImmunResponseLevel(tick);

        performDiffusion(immuneResponseLevel, immune.immuneResponseDiffCoeff);

        updateFields(immuneResponseLevel);
    }

    /**
     * Performs a time step for drug-related processes, including drug decay and movement between stomach and lung epithelial cells.
     *
     * @param tick The current time step.
     */
    void TimeStepDrug(int tick) {
        if (drug.inVivoOrInVitro.equals("inVitro")) {
            timeStepDrugInVitro();
        } else if (drug.inVivoOrInVitro.equals("inVivo")) {
            timeStepDrugInVivo(tick);
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
            cell.cellState();
        }
    }

    double[] countCells(){

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
     * Calculates the virus source based on the maximum virus concentration and drug effectiveness.
     *
     * @return The virus source.
     */
    double VirusSource() {
        return virus.virusMax * (1 - drug.drugVirusRemovalEff.getEfficacy(drug.getConvertedAndGeneratedDrug(drugCon)));
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
        if ((tick > numberOfTicksDelay) && (((tick - numberOfTicksDelay) % (12 * 60)) == 1)) {
            return this.drug.drugSourceStomach;
        } else {
            return 0.0;
        }
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



    // Helper functions

    private void updateFields(Object... fields) {
        for (Object field : fields) {
            try {
                // Find and invoke the Update method using reflection
                field.getClass().getMethod("Update").invoke(field);
            } catch (Exception e) {
                // Handle any exceptions if the method is not found or cannot be invoked
                e.printStackTrace();
            }
        }
    }

    private void setupDrugAdministrationProtocol(){
        if (drug.inVivoOrInVitro.equals("inVivo")) {
            this.drug.setInVivo();
            this.numberOfTicksDrug = 5 * 24 * 60; // we administer paxlovid for 5 days, i.e. 5*24*60 minutes
        } else {
            this.drug.setInVitro(5.0);
            this.numberOfTicksDrug = 4 * 24 * 60; // we incubate for 4 days
        }
    }

    private void performDiffusion(Object object, double coefficient) {
        try {
            // Find and invoke the DiffusionADI method using reflection
            object.getClass().getMethod("DiffusionADI", double.class).invoke(object, coefficient);
        } catch (Exception e) {
            // Handle any exceptions if the method is not found or cannot be invoked
            e.printStackTrace();
        }
    }

    private void increaseImmunResponseLevel(int tick){
        for (Cells cell : this){
            if (cell.cellType == I){ // infected cells produce interferon
                double addedImmuneResponseLevel = ImmuneResponseSource(tick, cell);
                double currentImmuneResponseLevel = immuneResponseLevel.Get(cell.Isq());
                double newImmuneResponseLevel = addedImmuneResponseLevel + currentImmuneResponseLevel;
                immuneResponseLevel.Set(cell.Isq(), newImmuneResponseLevel);
            }
        }
    }

    private void decayImmunResponseLevel(){
        for (Cells cell : this){
            double immuneResponseNow = immuneResponseLevel.Get(cell.Isq());
            immuneResponseLevel.Add(cell.Isq(), -immune.immuneResponseDecay * immuneResponseNow);
        }
        updateFields(immuneResponseLevel);
    }

    private void doVirusProduction(){
        for (Cells cell : this) {
            if (cell.cellType == I) { // Infected cell
                double addedVirusCon = VirusSource();
                double currentVirusCon = virusCon.Get(cell.Isq());
                double newVirusCon = addedVirusCon + currentVirusCon;
                virusCon.Set(cell.Isq(), newVirusCon);
            }
        }
    }


    private void timeStepDrugInVitro() {
        this.drugCon = this.drug.inVitroDrugCon;
    }

    private void timeStepDrugInVivo(int tick) {
        // Decay of the drug
        drugDecay();

        // Decay of the drug in the stomach and appearance in lung epithelial cells
        decayAndAppearanceInLungCells();

        // Drug appearance in the stomach
        drugAppearanceInStomach(tick);
    }

    private void drugAppearanceInStomach(int tick){
        drug.drugConStomach += DrugSourceStomach(tick);
    }

    private void decayAndAppearanceInLungCells(){
        double transferQuantity = this.drug.drugDecayStomach * drug.drugConStomach;
        this.drugCon += transferQuantity;
        drug.drugConStomach -= transferQuantity;
    }

    private void drugDecay(){
        this.drugCon -= this.drug.drugDecay * this.drugCon;
    }
}
