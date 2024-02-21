package org.example;

public abstract class Drug {

    /**
     * IC50 for the drug in nM, nanoMolars. The concentration at which the drug inhibits 50% of the virus. [nM] = 10^-9 [mol/L].
     */
    double EC50;

    /**
     * Molar mass of the drug in g/mol. Used for converting drug concentration between ng/ml and nanomolars.
     */
    double molarMassDrug;

    /**
     * Initial drug concentration for in vitro experiments.
     */
    double inVitroDrugCon;

    /**
     * Decay rate of the drug in in vivo scenarios.
     */
    double drugDecay;

    /**
     * Drug source in the stomach for in vivo scenarios. Represents the initial drug concentration in the stomach.
     */
    double drugSourceStomach;

    /**
     * Decay rate of the drug in the stomach for in vivo scenarios.
     */
    double drugDecayStomach;

    /**
     * The concentration of the drug in the stomach in the simulation.
     */
    public double drugConStomach;

    /**
     * The type of experiment, either "inVivo" or "inVitro".
     */
    public String inVivoOrInVitro;
}
