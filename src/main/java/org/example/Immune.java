package org.example;

import org.json.simple.JSONObject;

public class Immune {

    /**
     * The virus removal rate parameter in the simulation.
     */
    public double virusRemovalRate; // mu_V

    /**
     * The decay rate of immune response in the simulation.
     */
    public double immuneResponseDecay;

    /**
     * The diffusion coefficient of immune response in the simulation.
     */
    public double immuneResponseDiffCoeff;

    public Immune(){}

    public Immune(JSONObject jsonObject){

        setVirusRemovalRate((double) jsonObject.get("virusRemovalRate"));
        setImmuneResponseDecay((double) jsonObject.get("immuneResponseDecay"));
        setImmuneResponseDiffCoeff((double) jsonObject.get("immuneResponseDiffCoeff"));
    }

    public double getVirusRemovalRate() {
        return virusRemovalRate;
    }

    public void setVirusRemovalRate(double virusRemovalRate) {
        this.virusRemovalRate = virusRemovalRate;
    }

    public double getImmuneResponseDecay() {
        return immuneResponseDecay;
    }

    public void setImmuneResponseDecay(double immuneResponseDecay) {
        this.immuneResponseDecay = immuneResponseDecay;
    }

    public double getImmuneResponseDiffCoeff() {
        return immuneResponseDiffCoeff;
    }

    public void setImmuneResponseDiffCoeff(double immuneResponseDiffCoeff) {
        this.immuneResponseDiffCoeff = immuneResponseDiffCoeff;
    }
}
