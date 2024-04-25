package org.example.treatment.dosages;

import org.json.simple.JSONObject;

public class DosageFactory {
    public static Dosage createDosage(JSONObject jsonObject) {
        if (jsonObject.containsKey("concentration")) {
            return new ConstantDosage(jsonObject);
        } else {
            return new TwoCompartmentalAdministration(jsonObject);
        }
    }
}