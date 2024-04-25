package org.example.treatment;

import org.example.treatment.efficacies.Efficacy;
import org.example.treatment.efficacies.EfficacyFactory;
import org.json.simple.JSONObject;

public class Drug {

    public String name;
    public Efficacy virusRemovalEff;
    public Efficacy cytotoxicityEff;
    public Efficacy virusProductionReductionEff;
    public Efficacy infectionReductionEff;
    public Drug(JSONObject jsonObject){

        this.name = (String) jsonObject.get("name");

        virusRemovalEff = EfficacyFactory.createEfficacy(jsonObject, "virusRemovalEff");
        cytotoxicityEff = EfficacyFactory.createEfficacy(jsonObject, "cytotoxicityEff");
        virusProductionReductionEff = EfficacyFactory.createEfficacy(jsonObject, "virusProductionReductionEff");
        infectionReductionEff = EfficacyFactory.createEfficacy(jsonObject, "infectionReductionEff");
    }
}
