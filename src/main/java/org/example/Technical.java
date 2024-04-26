package org.example;

import org.example.differential_equation.operatorSplitting.MarchukStrangSplitting;
import org.example.differential_equation.operatorSplitting.OperatorSplitting;
import org.example.differential_equation.operatorSplitting.OperatorSplittingFactory;
import org.example.differential_equation.operatorSplitting.SequentialSplitting;
import org.json.simple.JSONObject;

public class Technical {

    public static final int X = 0;
    public static final int Y = 1;
    public int[] dim = new int[2];

    /**
     * The ratio of target cells in the initial configuration of the simulation.
     */
    public double initialCellRatioT;

    /**
     * The ratio of infected cells in the initial configuration of the simulation.
     */
    public double initialCellRatioI;

    public double simulationTime;
    public double timeStep;

    public OperatorSplitting operatorSplitting;

    public Long seed;

    public Technical(JSONObject jsonObject) {

        this.dim[X] = ((Long) jsonObject.get("xDim")).intValue();
        this.dim[Y] = ((Long) jsonObject.get("yDim")).intValue();

        this.initialCellRatioT = (double) jsonObject.get("initialCellRatioT");
        this.initialCellRatioI = (double) jsonObject.get("initialCellRatioI");

        this.simulationTime = (double) jsonObject.get("simulationTime");
        this.timeStep = (double) jsonObject.get("timeStep");

        this.seed = (Long) jsonObject.getOrDefault("seed", System.currentTimeMillis());

        this.operatorSplitting = OperatorSplittingFactory.createSplitting(this.timeStep, jsonObject);
    }
}
