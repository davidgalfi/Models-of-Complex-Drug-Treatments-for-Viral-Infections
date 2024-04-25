package org.example.treatment.dosages;

public interface Dosage {
    double[] getSample(double simulationTime, double timeStep); // in [ng/ml]
}
