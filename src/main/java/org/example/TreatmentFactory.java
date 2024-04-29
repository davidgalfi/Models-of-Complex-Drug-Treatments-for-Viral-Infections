package org.example;

import org.example.treatment.dosages.DosageFactory;
import org.example.treatment.drugs.DrugFactory;
import org.json.simple.JSONObject;

public class TreatmentFactory {

    public static Treatment createTreatment(JSONObject treatmentJsonObject, double simulationTime, double timeStep) {

        return new Treatment(

            DrugFactory.createDrug((JSONObject) treatmentJsonObject.get("Drug")),
            DosageFactory.createDosage((JSONObject) treatmentJsonObject.get("Dosage")),
            simulationTime,
            timeStep
        );
    }
}
