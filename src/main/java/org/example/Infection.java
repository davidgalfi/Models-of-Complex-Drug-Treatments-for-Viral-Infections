package org.example;

import HAL.GridsAndAgents.PDEGrid2D;

public class Infection {

    final public double virusRemovalRate;
    final public double cellDeathRate;
    final public double infectionRate;

    /**
     * The virus diffusion coefficient parameter in the simulation.
     */
    final public double virusDiffCoeff; // D_V [sigma^2 / min]
    /**
     * The maximum virus production parameter in the simulation.
     */
    final public double virusProduction; // f_{i,j}

    /**
     * The 2D partial differential equation (PDE) grid representing virus concentration in the simulation.
     */
    final public PDEGrid2D virusCon;

    public Infection(double virusProduction, double virusDiffCoeff, double virusRemovalRate, double cellDeathRate, double infectionRate, int xDim, int yDim) {
        this.virusProduction = virusProduction;
        this.virusDiffCoeff = virusDiffCoeff;
        this.virusRemovalRate = virusRemovalRate;
        this.cellDeathRate = cellDeathRate;
        this.infectionRate = infectionRate;

        this.virusCon = new PDEGrid2D(xDim, yDim);
    }

    public Void diffusion(Experiment G, double timeStep) {

        virusCon.DiffusionADI(virusDiffCoeff * timeStep);

        virusCon.Update();

        return null;
    }

    public Void decayAndProduction(Experiment G, double timeStep) {

        for (Cells cell : G) {

            double virusSource = 0;

            if (cell.cellType == Cells.I) {

                virusSource = virusProduction;

                for (Treatment treatment : G.treatments) {

                    virusSource *= 1 - treatment.drug.efficacy.get("virusProductionReduction").compute(treatment.concentration.Get());
                }
            }

            double virusConcentrationChange =
                    (virusCon.Get(cell.Isq()) - virusSource / virusRemovalRate) * (Math.exp(- virusRemovalRate * timeStep) - 1);

            virusCon.Add(cell.Isq(), virusConcentrationChange);
        }

        virusCon.Update();

        return null;
    }

    public void step(Experiment G) {

        G.technical.operatorSplitting.operatorSplit(
                (timeStep) -> diffusion(G, timeStep),
                (timeStep) -> decayAndProduction(G, timeStep)
        );
    }
}
