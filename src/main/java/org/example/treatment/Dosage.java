package org.example.treatment;

import org.json.simple.JSONObject;

public class Dosage {

    public double drugDecay;
    public double drugSourceStomach;
    public double drugDecayStomach;
    public double transferRate;
    public double metabolismRate;
    public double dosage;
    public int interval;

    public Dosage(JSONObject jsonObject) {
        this.drugDecay = (double) jsonObject.get("drugDecay");
        this.drugSourceStomach = (double) jsonObject.get("drugSourceStomach");
        this.drugDecayStomach = (double) jsonObject.get("drugDecayStomach");
        this.transferRate = (double) jsonObject.get("transferRate");
        this.metabolismRate = (double) jsonObject.get("metabolismRate");
        this.dosage = (double) jsonObject.get("dosage");
        this.interval = (int) jsonObject.get("interval");
    }
}
