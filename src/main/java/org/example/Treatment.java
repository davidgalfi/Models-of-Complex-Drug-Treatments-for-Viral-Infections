package org.example;

import org.example.treatment.ODEVirtualGrid;
import org.example.treatment.dosages.Dosage;
import org.example.treatment.drugs.Drug;

public class Treatment {

    final public Drug drug;
    final public Dosage dosage;

    final public ODEVirtualGrid concentration;

    public Treatment(Drug drug, Dosage dosage, double simulationTime, double timeStep){

        this.drug = drug;
        this.dosage = dosage;
        this.concentration = new ODEVirtualGrid(dosage.getSample(simulationTime, timeStep));
    }
}
