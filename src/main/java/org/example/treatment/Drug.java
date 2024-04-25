package org.example.treatment;

import org.example.efficacies.Efficacy;
import org.example.efficacies.NoEffect;
import org.example.efficacies.Hill;
import org.json.simple.JSONObject;

public class Drug {

    public String name;
    public Efficacy virusRemovalEff;
    public Efficacy cytotoxicityEff;
    public Efficacy virusProductionReductionEff;
    public Efficacy infectionReductionEff;
    public Drug(JSONObject jsonObject){

        this.name = (String) jsonObject.get("name");

        virusRemovalEff = getEfficacyFromJSON(jsonObject, "virusRemovalEff", "EC50", "n", "maxEfficacy");
        cytotoxicityEff = getEfficacyFromJSON(jsonObject, "cytotoxicityEff", "EC50", "n", "maxEfficacy");
        virusProductionReductionEff = getEfficacyFromJSON(jsonObject, "virusProductionReductionEff", "EC50", "n", "maxEfficacy");
        infectionReductionEff = getEfficacyFromJSON(jsonObject, "infectionReductionEff", "EC50", "n", "maxEfficacy");
    }

    public Efficacy getEfficacyFromJSON(JSONObject jsonObject, String key, String ec50, String n, String maxEfficacy) {
        if (jsonObject.containsKey(key)) {
            JSONObject efficacyData = (JSONObject) jsonObject.get(key);
            return new Hill((double) efficacyData.get(ec50), Math.toIntExact((Long) efficacyData.get(n)), (double) efficacyData.get(maxEfficacy));
        } else {
            return new NoEffect();
        }
    }
}
