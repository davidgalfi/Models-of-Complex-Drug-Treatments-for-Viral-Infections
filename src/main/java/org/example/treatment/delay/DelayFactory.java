package org.example.treatment.delay;

import org.json.simple.JSONObject;

public class DelayFactory {

    public static Delay createDelay(JSONObject jsonObject) {

        if (jsonObject.containsKey("time")) {
            return new Delay(Delay.TimeDelay, (double) jsonObject.get("time"));

        } else if (jsonObject.containsKey("damageRatio")) {
            return new Delay(Delay.DamageDelay, (double) jsonObject.get("damageRatio"));

        } else {
            return new Delay(Delay.TimeDelay, 0.0);

        }
    }
}
