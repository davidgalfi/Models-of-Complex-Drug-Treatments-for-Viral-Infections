package com.poortoys.examples;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;

public class PopulationODE implements FirstOrderDifferentialEquations {

    double constant;

    public PopulationODE(double konstant){
        this.constant = konstant;
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
        yDot[0] = -constant*y[0];
    }
}
