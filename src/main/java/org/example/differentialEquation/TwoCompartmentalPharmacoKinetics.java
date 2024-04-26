package org.example.differentialEquation;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public class TwoCompartmentalPharmacoKinetics implements FirstOrderDifferentialEquations {

    final double transferRate;
    final double targetDecay;

    public TwoCompartmentalPharmacoKinetics(double transferRate, double targetDecay) {
        this.transferRate = transferRate;
        this.targetDecay = targetDecay;
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = - transferRate * y[0];
        yDot[1] =   transferRate * y[0] - targetDecay * y[1];
    }
}
