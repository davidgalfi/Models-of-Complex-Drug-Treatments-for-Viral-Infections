package org.example;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public class VirusDiffEquation implements FirstOrderDifferentialEquations {

    private final double virusRemovalRate;
    private final double drugVirusRemovalEff;
    private final double immuneVirusRemovalEff;

    public VirusDiffEquation(double virusRemovalRate, double drugVirusRemovalEff, double immuneVirusRemovalEff) {
        this.virusRemovalRate = virusRemovalRate;
        this.drugVirusRemovalEff = drugVirusRemovalEff;
        this.immuneVirusRemovalEff = immuneVirusRemovalEff;
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        double currentCell = y[0];
        yDot[0] = currentCell * (-virusRemovalRate - drugVirusRemovalEff - immuneVirusRemovalEff);
    }
}
