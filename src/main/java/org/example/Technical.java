package org.example;

import org.example.differentialEquation.operatorSplitting.OperatorSplitting;
import org.example.differentialEquation.operatorSplitting.OperatorSplittingFactory;
import org.json.simple.JSONObject;

public class Technical {

    public static final int X = 0;
    public static final int Y = 1;
    final public int[] dim = new int[2];

    /**
     * The ratio of target cells in the initial configuration of the simulation.
     */
    final public double initialCellRatioT;

    /**
     * The ratio of infected cells in the initial configuration of the simulation.
     */
    final public double initialCellRatioI;

    final public double simulationTime;
    final public double timeStep;

    final public OperatorSplitting operatorSplitting;

    final public Long seed;

    public Technical(JSONObject jsonObject) {

        this.dim[X] = ((Long) jsonObject.get("xDim")).intValue();
        this.dim[Y] = ((Long) jsonObject.get("yDim")).intValue();

        this.initialCellRatioT = (double) jsonObject.get("initialCellRatioT");
        this.initialCellRatioI = (double) jsonObject.get("initialCellRatioI");

        this.simulationTime = (double) jsonObject.get("simulationTime");
        this.timeStep = (double) jsonObject.getOrDefault("timeStep", 1.0);

        this.seed = (Long) jsonObject.getOrDefault("seed", System.currentTimeMillis());

        this.operatorSplitting = OperatorSplittingFactory.createSplitting(this.timeStep, jsonObject);
    }
}
