package org.example;

import HAL.GridsAndAgents.AgentGrid2D;
import HAL.GridsAndAgents.PDEGrid2D;

import static HAL.Util.*;
import static org.example.Cells.*;
import static org.example.Technical.*;

import HAL.Rand;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator;
import org.example.treatment.Drug;
import org.example.treatment.Treatment;

/**
 * The NewExperiment class represents a simulation experiment with individual cells in a 2D square grid.
 * It extends the AgentGrid2D class with agents of type Cells, which represent the individual cells in the simulation.
 * The simulation involves interactions between cells, drug diffusion, virus concentration dynamics, and immune response.
 * The experiment can be run for a specified number of ticks, and the state of the system is visualized at each tick.
 */
public class Experiment extends AgentGrid2D<Cells> {

    Infection infection;
    Treatment[] treatments;
    Cells cells;
    Technical technical;

    /**
     * The random number generator used in the simulation.
     */
    public HAL.Rand rn;


    /**
     * The concentration of the drug in the simulation.
     */
    public double drugCon = 0;


    /**
     * The ratio of healthy cells in the initial configuration of the simulation.
     */
    public double ratioHealthy = 0.9995;

    /**
     * The ratio of infected cells in the initial configuration of the simulation.
     */
    public double ratioInfected = 0.0005;


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
                      Infection infection,
                      Treatment[] treatments,
                      Technical technical,
                      Rand rn){

        super(technical.dim[X], technical.dim[Y], Cells.class);

        this.rn = rn;


        this.treatments = treatments;
        this.infection = infection;
        this.cells = cells;
        this.technical = technical;




        // this.ode = new VirusDiffEquation(virusRemovalRate, drugVirusRemovalEff, immuneVirusRemovalEff);
        this.integrator = new DormandPrince54Integrator(10, 10, 10, 10);

        infection.virusCon = new PDEGrid2D(xDim, yDim);

        updateFields(infection.virusCon);
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
                c.Init(Cells.T);
            }
            else if(randomValue > ratioHealthy && randomValue < ratioHealthy + ratioInfected) {
                Cells c = NewAgentSQ(i);
                c.Init(Cells.I);
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

        for (double tick = 0; tick < technical.simulationTime; tick += technical.timeStep) {

            // Progress the simulation by one time step
            simulationStep( (int) tick);
            DrawModel(win);
        }
    }

    /**
     * Performs a time step for all components of the experiment, including immune response,
     * drug dynamics, virus dynamics, and cell-related processes.
     *
     * @param tick The current time step.
     */
    void simulationStep(int tick) {
        //timeStepCells(tick);
        timeStepVirus(tick);
        timeStepTreatments(tick);
        timeStepCells(tick);
    }

    private void timeStepTreatments(int tick) {
        for(Treatment treatment: treatments){
            treatment.concentration.Update();
        }
    }

    /**
     * Performs a time step for the virus-related processes, including virus decay, removal, and production.
     *
     * @param tick The current time step.
     */
    void timeStepVirus(int tick) {
        //performDiffusion(infection.virusCon, infection.virusDiffCoeff);
        // Decay of the virus
        for (Cells cell : this) {
            double virusSource = 0;
            if (cell.cellType == I) { // Infected cell
                virusSource = infection.virusProduction;
            }

            for (Treatment treatment : treatments) {

                virusSource *= 1 - treatment.drug.virusProductionReductionEff.getEfficacy(treatment.concentration.Get());
            }

            double virusConcentrationChange = (infection.virusCon.Get(cell.Isq()) - virusSource/infection.virusRemovalRate) * (Math.exp(-infection.virusRemovalRate * technical.timeStep) - 1);

            virusConcentrationChange = - infection.virusRemovalRate * infection.virusCon.Get(cell.Isq());

            infection.virusCon.Add(cell.Isq(), virusConcentrationChange);
        }

        updateFields(infection.virusCon);

        for (Cells cell : this) {
            double virusSource = 0;
            if (cell.cellType == I) { // Infected cell
                virusSource = infection.virusProduction;

                for (Treatment treatment : treatments) {


                    virusSource *= 1 - treatment.drug.virusProductionReductionEff.getEfficacy(treatment.concentration.Get() * Math.pow(10,3) / 499.535);
                }

                double virusConcentrationChange = virusSource + infection.virusCon.Get(cell.Isq());
                infection.virusCon.Set(cell.Isq(), virusConcentrationChange);
            }
        }
        infection.virusCon.DiffusionADI(infection.virusDiffCoeff);


        updateFields(infection.virusCon);
    }

    /**
     * Performs a time step for cell-related processes, including infection and cell death.
     *
     * @param tick The current time step.
     */
    void timeStepCells(int tick) {
        for (Cells cell : this) {
            //cell.stochasticStateChange();
            if (cell.cellType == Cells.T) {
                cell.stochasticStateChange();
            }
        }
        for (Cells cell : this) {
            if (cell.cellType == Cells.I) {
                cell.stochasticStateChange();
            }
        }
    }

    double[] countCells(){

        double healthyCells = 0, infectedCells = 0, deadCells = 0,
                capillaryCells = 0;
        double[] cellCount = new double[4];
        for (Cells cell: this){
            if (cell.cellType == T){
                healthyCells += 1;
            }
            else if (cell.cellType == I ){
                infectedCells += 1;
            }
            else if (cell.cellType == D){
                deadCells += 1;
            }
        }

        cellCount[T] = healthyCells;
        cellCount[I] = infectedCells;
        cellCount[D] = deadCells;

        return cellCount;
    }


    /**
     * Calculates the drug source in the stomach for a given tick.
     *
     * @param tick The current tick of the simulation.
     * @return The drug source in the stomach.
     */
    /*double DrugSourceStomach(Drug drug, int tick) {
        if ((tick > numberOfTicksDelay) && (((tick - numberOfTicksDelay) % (12 * 60)) == 1)) {
            return drug.drugSourceStomach;
        } else {
            return 0.0;
        }
    }*/

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
                if (drawMe.cellType == T) {        // Healthy cells
                    vis.SetPix(i, RGB256(119, 198, 110)); // Set healthy cell color to green
                } else if (drawMe.cellType == I) { // Infected cells
                    vis.SetPix(i, RGB256(124, 65, 120));  // Set infected cell color to purple
                } else if (drawMe.cellType == D) { // Dead cells
                    vis.SetPix(i, RGB(0, 0, 0)); // Set dead cell color to black
                }
            }

            // Visualize virus concentration using a heat map
            vis.SetPix(ItoX(i) + xDim, ItoY(i), HeatMapRBG(infection.virusCon.Get(i)));
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

    private void performDiffusion(Object object, double coefficient) {
        try {
            // Find and invoke the DiffusionADI method using reflection
            object.getClass().getMethod("DiffusionADI", double.class).invoke(object, coefficient);
        } catch (Exception e) {
            // Handle any exceptions if the method is not found or cannot be invoked
            e.printStackTrace();
        }
    }

    /*
    private void increaseImmunResponseLevel(int tick){
        for (Cells cell : this){
            if (cell.cellType == I){ // infected cells produce interferon
                double addedImmuneResponseLevel = immuneResponseSource(tick, cell);
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
    */

    /*private void doVirusProduction(){
        for (Cells cell : this) {
            for (Treatment treatment: treatments){
                Drug drug = treatment.drug;

            }
        }
    }*/


    /*private void timeStepDrugInVitro(Drug drug) {
        this.drugCon = drug.inVitroDrugCon;
    }

    private void timeStepDrugInVivo(Drug drug, int tick) {
        // Decay of the drug
        drugDecay(drug);

        // Decay of the drug in the stomach and appearance in lung epithelial cells
        decayAndAppearanceInLungCells(drug);

        // Drug appearance in the stomach
        drugAppearanceInStomach(drug, tick);
    }

    private void drugAppearanceInStomach(Drug drug, int tick){
        drug.drugConStomach += DrugSourceStomach(drug, tick);
    }

    private void decayAndAppearanceInLungCells(Drug drug){
        double transferQuantity = drug.drugDecayStomach * drug.drugConStomach;
        this.drugCon += transferQuantity;
        drug.drugConStomach -= transferQuantity;
    }

    private void drugDecay(Drug drug){
        this.drugCon -= drug.drugDecay * this.drugCon;
    }*/
}
