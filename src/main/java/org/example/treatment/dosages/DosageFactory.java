package org.example.treatment.dosages;

import org.json.simple.JSONObject;

public class DosageFactory {
    public static Dosage createDosage(JSONObject jsonObject) {
        if (jsonObject.containsKey("concentration")) {

            return new ConstantDosage((double) jsonObject.get("concentration"));
        } else {

            return new TwoCompartmentalAdministration(
                (double) jsonObject.get("targetDecay"),
                (double) jsonObject.get("transferRate"),
                (double) jsonObject.get("dosage"),
                (double) jsonObject.get("interval")
            );
        }
    }
}