package org.example.treatment;

import org.example.ODEVirtualGrid;
import org.json.simple.JSONObject;

public class Treatment {
    public Drug drug;
    public Dosage dosage;

    public ODEVirtualGrid concentration;

    public Treatment(JSONObject treatmentJsonObject){
        setDrug((JSONObject) treatmentJsonObject.get("Drug"));
        setDosage((JSONObject) treatmentJsonObject.get("Dosage"));
    }

    private void setDrug(JSONObject jsonObject){
        this.drug = new Drug(jsonObject);
    }

    private void setDosage(JSONObject jsonObject){
        this.dosage = new Dosage(jsonObject);
    }
}
