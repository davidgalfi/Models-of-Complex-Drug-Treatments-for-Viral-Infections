package org.example;

/**
 * The NirmatrelvirDrug class represents the drug used in the simulation to model the effects of antiviral medication.
 * It includes properties for both in vitro and in vivo scenarios, with specific characteristics such as drug concentration,
 * decay rates, and drug-virus interaction parameters.
 */
public class NirmatrelvirDrug extends Drug{

    /**
     * Flag indicating whether Ritonavir is boosted. Determines adjustments in drug properties for in vivo scenarios.
     */
    boolean isRitonavirBoosted;

    boolean isNirmatrelvir;

    /**
     * In vitro constructor for NirmatrelvirDrug.
     * @param inVitroDrugCon The initial drug concentration for in vitro experiments.
     * @param isNirmatrelvir Boolean indicating whether Nirmatrelvir is used.
     */
    public NirmatrelvirDrug(double inVitroDrugCon, boolean isNirmatrelvir){
        if (isNirmatrelvir) {
            this.inVitroDrugCon = inVitroDrugCon;
        } else {
            this.inVitroDrugCon = 0.0;
        }
    }

    /**
     * In vivo constructor for NirmatrelvirDrug.
     * @param isRitonavirBoosted Boolean indicating whether Ritonavir is boosted.
     */
    public NirmatrelvirDrug(boolean isRitonavirBoosted){
        this.isRitonavirBoosted = isRitonavirBoosted;

        // Adjust properties if Ritonavir is boosted
        if (isRitonavirBoosted) {
            drugDecay /= 3.0;
            drugDecayStomach /= 4.0;
            drugSourceStomach *= 3.8;
        }
    }





}

