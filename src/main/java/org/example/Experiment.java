package org.example;

import HAL.GridsAndAgents.AgentGrid2D;

import static org.example.Cells.*;
import static org.example.Technical.*;

import HAL.Rand;
import org.example.timer.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The Experiment class represents a simulation experiment with individual cells in a 2D square grid.
 * It extends the AgentGrid2D class with agents of type Cells, which represent the individual cells in the simulation.
 * The simulation involves interactions between cells, drug administering, and virus concentration dynamics.
 * The experiment can be run for a specified length of time, and the state of the system is visualized at each timeStep.
 */
public class Experiment extends AgentGrid2D<Cells> {

    final Infection infection;
    final Treatment[] treatments;
    final Cells cells;
    final Technical technical;

    final Timer timer;

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
        this.timer = new Timer(technical.simulationTime, technical.timeStep);

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
            treatment.concentration.Update(this);
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
     * @param callbacks possible callbacks taking the experiment as argument.
     */
    public void run(ArrayList<Function<Experiment, Void>> callbacks) {

        while (timer.isNotOver()) {

            // Progress the simulation by one time step
            simulationStep();

            timer.step();

            // Apply callbacks if any
            for (Function<Experiment, Void> callback : callbacks) {

                callback.apply(this);
            }
        }
    }

    public Map<String, Number> cellStatistics(){

        double targetCellCount = 0, infectedCellCount = 0, deadCellCount = 0;

        for (Cells cell: this){

            if (cell.cellType == T){
                targetCellCount += 1;

            }
            else if (cell.cellType == I ){
                infectedCellCount += 1;

            }
            else if (cell.cellType == D){
                deadCellCount += 1;

            }
        }

        Map<String, Number> statistics = new HashMap<>();

        statistics.put("T", targetCellCount);
        statistics.put("I", infectedCellCount);
        statistics.put("D", deadCellCount);
        statistics.put("damageRatio", 1 - targetCellCount / this.length);

        return statistics;
    }

    public Map<String, Number> infectionStatistics() {

        double totalVirusCon = 0.0;

        for (int i = 0; i < length; i++){

            totalVirusCon = totalVirusCon + infection.virusCon.Get(i);
        }

        Map<String, Number> statistics = new HashMap<>();

        statistics.put("totalVirusConcentration", totalVirusCon);

        return statistics;
    }

    public Map<String, Number> statistics() {

        Map<String, Number> statistics = new HashMap<>(cellStatistics());

        statistics.putAll(infectionStatistics());

        return statistics;
    }
}
