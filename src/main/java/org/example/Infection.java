package org.example;

import HAL.GridsAndAgents.PDEGrid2D;
import org.example.treatment.Treatment;
import org.json.simple.JSONObject;

import static org.example.Cells.I;

public class Infection {

    public double virusRemovalRate;
    public double cellDeathRate;
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

    public Infection(JSONObject jsonObject){
        this.virusProduction = (double) jsonObject.get("virusProduction");
        this.virusDiffCoeff = (double) jsonObject.get("virusDiffCoeff");
        this.virusRemovalRate = (double) jsonObject.get("virusRemovalRate");
        this.cellDeathRate = (double) jsonObject.get("cellDeathRate");
        this.infectionRate = (double) jsonObject.get("infectionRate");
    }

    public Void diffusion(Experiment G, double timeStep) {

        virusCon.DiffusionADI(virusDiffCoeff * timeStep);

        virusCon.Update();

        return null;
    }

    public Void decayAndProduction(Experiment G, double timeStep) {

        for (Cells cell : G) {

            double virusSource = (cell.cellType == I) ? virusProduction : 0.0;

            for (Treatment treatment : G.treatments) {

                virusSource *= 1 - treatment.drug.efficacy.get("virusProductionReduction").compute(treatment.concentration.Get());
            }

            double virusConcentrationChange =
                    (virusCon.Get(cell.Isq()) - virusSource / virusRemovalRate) * (Math.exp(- virusRemovalRate * timeStep) - 1);

            virusCon.Add(cell.Isq(), virusConcentrationChange);
        }

        virusCon.Update();

        return null;
    }

    public void step(Experiment G) {

        diffusion(G, G.technical.timeStep);
        decayAndProduction(G, G.technical.timeStep);
    }
}
