package org.example.treatment;

import org.example.treatment.efficacies.Efficacy;
import org.example.treatment.efficacies.EfficacyFactory;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Drug {

    final public String name;

    final public Map<String, Efficacy> efficacy;

    final static String[] efficacies = {
            "virusRemoval",
            "cytotoxicity",
            "virusProductionReduction",
            "infectionReduction" };

    public Drug(JSONObject jsonObject){

        this.name = (String) jsonObject.getOrDefault("name", "drug");

        this.efficacy = new HashMap<>();

        for (String key : efficacies) {

            efficacy.put(key, EfficacyFactory.createEfficacy(jsonObject, key));
        }
    }
}
