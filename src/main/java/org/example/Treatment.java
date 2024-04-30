package org.example;

import org.example.treatment.ODEVirtualGrid;
import org.example.treatment.delay.Delay;
import org.example.treatment.dosages.ConstantDosage;
import org.example.treatment.dosages.Dosage;
import org.example.treatment.drugs.Drug;

public class Treatment {

    final public Drug drug;
    final public Dosage dosage;

    public ODEVirtualGrid concentration;

    Delay delay;

    public Treatment(Drug drug, Dosage dosage, Delay delay, double simulationTime, double timeStep){

        this.drug = drug;
        this.dosage = dosage;
        this.delay = delay;

        this.concentration = new ODEVirtualGrid(new ConstantDosage(0.0).getSample(simulationTime, timeStep));
    }

    public void delayedStart(Experiment G) {

        if ((delay != null) && delay.isOver(G.timer.get(), (double) Statistics.getCellStatistics(G).get("damageRatio"))) {

            concentration = new ODEVirtualGrid(dosage.getSample(G.timer.totalTime - G.timer.get(), G.timer.timeStep));
            delay = null;
        }
    }
}
