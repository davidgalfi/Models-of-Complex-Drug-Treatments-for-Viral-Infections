package org.example;

import java.util.ArrayList;
import java.util.function.Function;

import HAL.GridsAndAgents.AgentGrid2D;
import HAL.Rand;

import static org.example.Technical.*;
import org.example.timer.Timer;


/**
 * The Experiment class represents a simulation experiment with individual cells in a 2D square grid.
 * It extends the AgentGrid2D class with agents of type Cells, which represent the individual cells in the simulation.
 * The simulation involves interactions between cells, drug administering, and virus concentration dynamics.
 * The experiment can be run for a specified length of time, and the state of the system is visualized at each timeStep.
 */
public class Experiment extends AgentGrid2D<Cell> {

    final Infection infection;
    final ArrayList<Treatment> treatments;
    final Technical technical;
    /**
     * The random number generator used in the simulation.
     */
    final public HAL.Rand rn;
    final Timer timer;

    public Experiment(
          Infection infection,
          ArrayList<Treatment> treatments,
          Technical technical
    ) {

        super(technical.dim[X], technical.dim[Y], Cell.class);

        this.infection = infection;
        this.treatments = treatments;

        this.technical = technical;

        this.rn = new Rand(technical.seed);
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
                Cell c = NewAgentSQ(i);
                c.init(Cell.T);
            }
            else if(randomValue < technical.initialCellRatioT + technical.initialCellRatioI) {
                Cell c = NewAgentSQ(i);
                c.init(Cell.I);
            }
        }
    }

    /**
     * Runs the simulation for a specified number of ticks, updating the model and visualizing it at each step.
     * The method also monitors and reports specific events during the simulation, such as reaching a fixed damage rate.
     *
     * @param callbacks possible callbacks taking the experiment as argument.
     */
    public void run(ArrayList<Function<Experiment, Void>> callbacks) {

        while (timer.isNotOver()) {

            // Performs a time step for instantaneous treatment-related processes.
            treatments.forEach(treatment -> treatment.delayedStart(this));

            // Performs a time step for instantaneous cell-related processes, including infection and cell death.
            this.forEach(Cell::stochasticStateChange);

            // Performs a time step for the continuous virus-related processes, including virus decay, removal, and production.
            infection.step(this);

            // Performs a time step for continuous treatment-related processes.
            treatments.forEach(treatment -> treatment.concentration.Update());

            // advance timer
            timer.step();

            // Apply callbacks if any
            callbacks.forEach(callback -> callback.apply(this));
        }
    }
}
