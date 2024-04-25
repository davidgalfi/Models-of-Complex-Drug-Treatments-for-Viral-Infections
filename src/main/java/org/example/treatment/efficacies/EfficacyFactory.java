package org.example.treatment.efficacies;

import org.json.simple.JSONObject;

public class EfficacyFactory {
    public static Efficacy createEfficacy(JSONObject jsonObject, String key) {

        if (jsonObject.containsKey(key)) {

            return new Hill((JSONObject) jsonObject.get(key));
        } else {

            return new NoEffect();
        }
    }
}
