package org.example;

import org.json.simple.JSONObject;

public class Virus {

    /**
     * The maximum virus production parameter in the simulation.
     */
    public double virusMax; // f_{i,j}



    /**
     * The virus diffusion coefficient parameter in the simulation.
     */
    public double virusDiffCoeff; // D_V [sigma^2 / min]

    public Virus(){}

    public Virus(JSONObject jsonObject){
        setVirusMax((double) jsonObject.get("virusMax"));
        setVirusDiffCoeff((double) jsonObject.get("virusDiffCoeff"));
    }

    public double getVirusMax() {
        return virusMax;
    }

    public void setVirusMax(double virusMax) {
        this.virusMax = virusMax;
    }

    public double getVirusDiffCoeff() {
        return virusDiffCoeff;
    }

    public void setVirusDiffCoeff(double virusDiffCoeff) {
        this.virusDiffCoeff = virusDiffCoeff;
    }
}
