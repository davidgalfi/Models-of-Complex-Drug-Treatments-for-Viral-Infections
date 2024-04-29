package org.example.treatment;

import org.example.treatment.dosages.Dosage;
import org.example.treatment.dosages.DosageFactory;
import org.json.simple.JSONObject;

public class Treatment {

    final public Drug drug;
    final public Dosage dosage;

    final public ODEVirtualGrid concentration;

    public Treatment(JSONObject treatmentJsonObject, double simulationTime, double timeStep){

        this.drug = DrugFactory.createDrug((JSONObject) treatmentJsonObject.get("Drug"));

        this.dosage = DosageFactory.createDosage((JSONObject) treatmentJsonObject.get("Dosage"));

        this.concentration = new ODEVirtualGrid(dosage.getSample(simulationTime, timeStep));
    }
}
