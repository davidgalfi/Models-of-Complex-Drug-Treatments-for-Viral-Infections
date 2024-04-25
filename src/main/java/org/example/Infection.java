package org.example;

import HAL.GridsAndAgents.PDEGrid2D;
import org.json.simple.JSONObject;

public class Infection {

    public double virusRemovalRate;
    public double cellDeathRate;
    public double cellDeathProbability;
    public double infectionRate;

    /**
     * The virus diffusion coefficient parameter in the simulation.
     */
    public double virusDiffCoeff; // D_V [sigma^2 / min]
    /**
     * The maximum virus production parameter in the simulation.
     */
    public double virusProduction; // f_{i,j}

    /**
     * The 2D partial differential equation (PDE) grid representing virus concentration in the simulation.
     */
    public PDEGrid2D virusCon;

    public double infectionProbability;

    public Infection(JSONObject jsonObject, JSONObject technicalJsonObject){
        this.virusProduction = (double) jsonObject.get("virusProduction");
        this.virusDiffCoeff = (double) jsonObject.get("virusDiffCoeff");
        this.virusRemovalRate = (double) jsonObject.get("virusRemovalRate");
        this.cellDeathRate = (double) jsonObject.get("cellDeathRate");
        this.infectionRate = (double) jsonObject.get("infectionRate");

        double timeStep = (double) technicalJsonObject.get("timeStep");

        //this.cellDeathProbability = 1-Math.exp(-cellDeathRate*timeStep);
        this.cellDeathProbability = cellDeathRate*timeStep;
        this.infectionProbability = infectionRate*timeStep;
    }
}
