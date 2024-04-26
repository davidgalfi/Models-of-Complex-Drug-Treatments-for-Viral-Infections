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
    public void runExperiment(Visuals visuals) {
        double[] cellCounts = countCells();

        for (double tick = 0; tick < technical.simulationTime; tick += technical.timeStep) {

            // Progress the simulation by one time step
            simulationStep();
            visuals.drawExperimentState(this);
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
}
