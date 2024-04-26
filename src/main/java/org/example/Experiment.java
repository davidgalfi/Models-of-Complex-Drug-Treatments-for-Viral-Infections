package org.example;

import HAL.GridsAndAgents.AgentGrid2D;
import HAL.GridsAndAgents.PDEGrid2D;

import static HAL.Util.*;
import static org.example.Cells.*;
import static org.example.Technical.*;

import HAL.Rand;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator;
import org.example.treatment.Treatment;

/**
 * The NewExperiment class represents a simulation experiment with individual cells in a 2D square grid.
 * It extends the AgentGrid2D class with agents of type Cells, which represent the individual cells in the simulation.
 * The simulation involves interactions between cells, drug diffusion, virus concentration dynamics, and immune response.
 * The experiment can be run for a specified number of ticks, and the state of the system is visualized at each tick.
 */
public class Experiment extends AgentGrid2D<Cells> {

    final Infection infection;
    final Treatment[] treatments;
    final Cells cells;
    final Technical technical;

    /**
     * The random number generator used in the simulation.
     */
    final public HAL.Rand rn;

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

    public Experiment(Cells cells,
                      Infection infection,
                      Treatment[] treatments,
                      Technical technical,
                      Rand rn) {

        super(technical.dim[X], technical.dim[Y], Cells.class);

        this.rn = rn;

        this.treatments = treatments;
        this.infection = infection;
        this.cells = cells;
        this.technical = technical;

        initCells();
    }

    /**
     * Initializes the simulation by setting up the grid and initializing cells based on specified ratios.
     * This method populates the grid with cells based on random values and predefined ratios for target and infected cells.
     */
    public void initCells(){

        for (int i = 0; i < length; i++){
            double randomValue = rn.Double();

            if (randomValue < technical.initialCellRatioT){
                Cells c = NewAgentSQ(i);
                c.init(Cells.T);
            }
            else if(randomValue < technical.initialCellRatioT + technical.initialCellRatioI) {
                Cells c = NewAgentSQ(i);
                c.init(Cells.I);
            }
        }
    }

    /**
     * Performs a time step for cell-related processes, including infection and cell death.
     */
    void timeStepCells() {
        for (Cells cell : this) {
            cell.stochasticStateChange();
        }
    }

    /**
     * Performs a time step for the virus-related processes, including virus decay, removal, and production.
     */
    void timeStepVirus() {
        infection.step(this);
    }

    private void timeStepTreatments() {
        for(Treatment treatment: treatments){
            treatment.concentration.Update();
        }
    }

    /**
     * Performs a time step for all components of the experiment, including immune response,
     * drug dynamics, virus dynamics, and cell-related processes.
     */
    void simulationStep() {
        timeStepCells();
        timeStepVirus();
        timeStepTreatments();
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
            simulationStep();
            DrawModel(win);
        }
    }



    double[] countCells(){

        double healthyCells = 0, infectedCells = 0, deadCells = 0;
        double[] cellCount = new double[3];
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
}
