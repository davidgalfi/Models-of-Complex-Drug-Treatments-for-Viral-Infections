package org.example.treatment;

import org.json.simple.JSONObject;

class DosageFactory {
    public static Dosage createDosage(JSONObject jsonObject) {
        if (jsonObject.containsKey("concentration")) {
            return new DosageInVitro(jsonObject);
        } else {
            return new DosageInVivo(jsonObject);
        }
    }
}