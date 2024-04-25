package org.example.treatment;

import org.example.treatment.efficacies.Efficacy;
import org.example.treatment.efficacies.EfficacyFactory;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Drug {

    public String name;

    public Map<String, Efficacy> efficacy;

    static String[] efficacies = {
            "virusRemoval",
            "cytotoxicity",
            "virusProductionReduction",
            "infectionReduction" };

    public Drug(JSONObject jsonObject){

        this.name = (String) jsonObject.get("name");

        this.efficacy = new HashMap<>();

        for (String key : efficacies) {

            efficacy.put(key, EfficacyFactory.createEfficacy(jsonObject, key));
        }
    }
}
