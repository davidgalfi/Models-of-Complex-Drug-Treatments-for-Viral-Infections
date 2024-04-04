package org.example.treatment;

import org.json.simple.JSONObject;

class DosageFactory {
    public static Dosage createDosage(JSONObject jsonObject) {

        String type = (String) jsonObject.get("experimentType");

        if ("InVivo".equals(type)) {
            return new DosageInVivo(jsonObject);
        } else if ("InVitro".equals(type)) {
            return new DosageInVitro(jsonObject);
        } else {
            throw new IllegalArgumentException("Invalid dosage type: " + type);
        }
    }
}