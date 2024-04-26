package org.example.treatment;

import org.example.treatment.dosages.Dosage;
import org.example.treatment.dosages.DosageFactory;
import org.json.simple.JSONObject;

public class Treatment {
    public Drug drug;
    public Dosage dosage;

    public ODEVirtualGrid concentration;

    public Treatment(JSONObject treatmentJsonObject, JSONObject technicalJsonObject){
        setDrug((JSONObject) treatmentJsonObject.get("Drug"));
        setDosage((JSONObject) treatmentJsonObject.get("Dosage"));
        setConcentration((JSONObject) technicalJsonObject);
    }

    private void setDrug(JSONObject jsonObject){
        this.drug = new Drug(jsonObject);
    }

    private void setDosage(JSONObject jsonObject){
        this.dosage = DosageFactory.createDosage(jsonObject);
    }

    private void setConcentration(JSONObject jsonObject){
        double simulationTime = (double)jsonObject.get("simulationTime");
        double timeStep = (double)jsonObject.get("timeStep");
        this.concentration = new ODEVirtualGrid(dosage.getSample(simulationTime, timeStep));
    }
}
