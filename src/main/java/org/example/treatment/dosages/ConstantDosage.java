package org.example.treatment.dosages;

import java.util.Arrays;

public class ConstantDosage implements Dosage {
    final double concentration;

    public ConstantDosage(double concentration) {
        this.concentration = concentration;
    }

    @Override
    public double[] getSample(double simulationTime, double timeStep) {

        double[] sample = new double[(int)(simulationTime / timeStep) + 2];
        Arrays.fill(sample, concentration);
        return sample;
    }
}
