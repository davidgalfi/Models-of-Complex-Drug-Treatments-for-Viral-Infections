package org.example.treatment.dosages;

import org.json.simple.JSONObject;

import java.util.Arrays;

public class ConstantDosage implements Dosage {
    double concentration;

    public ConstantDosage(JSONObject jsonObject) {
        this.concentration = (double) jsonObject.get("concentration");
    }

    @Override
    public double[] getSample(double simulationTime, double timeStep) {

        double[] sample = new double[(int)(simulationTime / timeStep) + 2];
        Arrays.fill(sample, concentration);
        return sample;
    }
}
