package org.example;

/**
 * The NirmatrelvirDrug class represents the drug used in the simulation to model the effects of antiviral medication.
 * It includes properties for both in vitro and in vivo scenarios, with specific characteristics such as drug concentration,
 * decay rates, and drug-virus interaction parameters.
 */
public class NirmatrelvirDrug {

    /**
     * IC50 for the drug in nM, nanoMolars. The concentration at which the drug inhibits 50% of the virus. [nM] = 10^-9 [mol/L].
     */
    double EC50 = 62;

    /**
     * Molar mass of the drug in g/mol. Used for converting drug concentration between ng/ml and nanomolars.
     */
    double molarMassDrug = 499.535;

    /**
     * Initial drug concentration for in vitro experiments.
     */
    double inVitroDrugCon = 5;

    /**
     * Flag indicating whether Ritonavir is boosted. Determines adjustments in drug properties for in vivo scenarios.
     */
    boolean isRitonavirBoosted = true;

    /**
     * Decay rate of the drug in in vivo scenarios.
     */
    double drugDecay = 0.013;

    /**
     * Drug source in the stomach for in vivo scenarios. Represents the initial drug concentration in the stomach.
     */
    double drugSourceStomach = 1800;

    /**
     * Decay rate of the drug in the stomach for in vivo scenarios.
     */
    double drugDecayStomach = 0.015;

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

    /**
     * Calculates the drug-virus production efficiency based on the drug concentration.
     * @param drugNow The current drug concentration in nanograms/ml.
     * @return The drug-virus production efficiency.
     */
    double DrugVirusProdEff(double drugNow){
        double drugNowInNanoMolars = NgPerMlToNanomolars(drugNow);
        return 1 / (1 + (EC50 / StochasticDrug(drugNowInNanoMolars)));
    }

    /**
     * Generates a stochastic drug concentration based on a Gaussian distribution.
     * @param drug The mean drug concentration.
     * @return The stochastic drug concentration.
     */
    double StochasticDrug(double drug){
        double stdDevOfGaussian = drug / 100;
        HAL.Rand random = new HAL.Rand();
        double stochasticDrug = random.Gaussian(drug, stdDevOfGaussian);
        return Math.max(stochasticDrug, 0.0); // Ensure non-negative concentration
    }

    /**
     * Converts drug concentration from ng/ml to nanomolars.
     * @param drugNow The drug concentration in ng/ml.
     * @return The drug concentration in nanomolars.
     */
    double NgPerMlToNanomolars(double drugNow){
        return drugNow * Math.pow(10,3) / molarMassDrug;
    }
}

