package org.example.differential_equation;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.example.treatment.Dosage;

public class DrugDosageInVivo implements FirstOrderDifferentialEquations {

    public double transferRate;
    public double drugDecay;

    public DrugDosageInVivo(double transferRate, double drugDecay) {
        this.transferRate = transferRate;
        this.drugDecay = drugDecay;
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = -transferRate*y[0];
        yDot[1] = +transferRate*y[0] - drugDecay*y[1];
    }
}
