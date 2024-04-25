package org.example.treatment.dosages;

import org.example.treatment.dosages.ConstantDosage;
import org.example.treatment.dosages.Dosage;
import org.example.treatment.dosages.DosageInVivo;
import org.json.simple.JSONObject;

public class DosageFactory {
    public static Dosage createDosage(JSONObject jsonObject) {
        if (jsonObject.containsKey("concentration")) {
            return new ConstantDosage(jsonObject);
        } else {
            return new DosageInVivo(jsonObject);
        }
    }
}