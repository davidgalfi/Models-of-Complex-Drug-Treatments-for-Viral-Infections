package org.example.differential_equation;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public class DrugDosageInVivo implements FirstOrderDifferentialEquations {

    public double gamma;

    public DrugDosageInVivo(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = -gamma*y[0];
    }
}
