package com.poortoys.examples;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;

public class CircleODE implements FirstOrderDifferentialEquations {

    public double[] c;
    public double omega;

    public CircleODE(double[] c, double omega){
        this.c = c;
        this.omega = omega;
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
        yDot[0] = omega*(c[1] - y[1]);
        yDot[1] = omega*(c[0] - y[0]);
    }
}
