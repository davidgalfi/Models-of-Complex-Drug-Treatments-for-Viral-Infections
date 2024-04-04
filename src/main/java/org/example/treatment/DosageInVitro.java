package org.example.treatment;

import org.json.simple.JSONObject;

import java.util.Arrays;

public class DosageInVitro implements Dosage{
    public double concentration;

    public DosageInVitro(JSONObject jsonObject) {
        this.concentration = (double) jsonObject.get("concentration");
    }

    @Override
    public double[] getSample(double simulationTime, double timeStep) {
        double[] sample = new double[(int)(simulationTime/timeStep)+1];
        Arrays.fill(sample, concentration);
        return sample;
    }
}
