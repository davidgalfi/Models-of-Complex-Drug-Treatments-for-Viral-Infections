package org.example;

import org.json.simple.JSONObject;

public class Drug {

    String name;

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

    boolean isRitonavirBoosted;

    Efficacy drugVirusRemovalEff;

    public Drug(){}
    public Drug(JSONObject jsonObject){

        setName((String) jsonObject.get("name"));
        setEC50((double) jsonObject.get("EC50"));
        setMolarMassDrug((double) jsonObject.get("molarMassDrug"));
        setInVitroDrugCon((double) jsonObject.get("inVitroDrugCon"));
        setDrugDecay((double) jsonObject.get("drugDecay"));
        setDrugSourceStomach((double) jsonObject.get("drugSourceStomach"));
        setDrugDecay((double) jsonObject.get("drugDecayStomach"));
        setDrugConStomach((double) jsonObject.get("drugConStomach"));
        setInVivoOrInVitro((String) jsonObject.get("inVivoOrInVitro"));
        setRitonavirBoosted((boolean) jsonObject.get("isRitonavirBoosted"));
        setdrugVirusRemovalEff(getEC50());
    }

    public void setdrugVirusRemovalEff(double ec50){
        if(ec50 > 0){
            drugVirusRemovalEff = new StandardHill(getEC50());
        } else {
            drugVirusRemovalEff = new NoEffect();
        }
    }

    public void setInVitro(double inVitroDrugCon){
        this.inVitroDrugCon = inVitroDrugCon;
    }

    public void setInVivo(){
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
     * Converts drug concentration from ng/ml to nanomolars.
     * @param drugNow The drug concentration in ng/ml.
     * @return The drug concentration in nanomolars.
     */
    double NgPerMlToNanomolars(double drugNow){
        return drugNow * Math.pow(10,3) / molarMassDrug;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEC50() {
        return EC50;
    }

    public void setEC50(double EC50) {
        this.EC50 = EC50;
    }

    public double getMolarMassDrug() {
        return molarMassDrug;
    }

    public void setMolarMassDrug(double molarMassDrug) {
        this.molarMassDrug = molarMassDrug;
    }

    public double getInVitroDrugCon() {
        return inVitroDrugCon;
    }

    public void setInVitroDrugCon(double inVitroDrugCon) {
        this.inVitroDrugCon = inVitroDrugCon;
    }

    public double getDrugDecay() {
        return drugDecay;
    }

    public void setDrugDecay(double drugDecay) {
        this.drugDecay = drugDecay;
    }

    public double getDrugSourceStomach() {
        return drugSourceStomach;
    }

    public void setDrugSourceStomach(double drugSourceStomach) {
        this.drugSourceStomach = drugSourceStomach;
    }

    public double getDrugDecayStomach() {
        return drugDecayStomach;
    }

    public void setDrugDecayStomach(double drugDecayStomach) {
        this.drugDecayStomach = drugDecayStomach;
    }

    public double getDrugConStomach() {
        return drugConStomach;
    }

    public void setDrugConStomach(double drugConStomach) {
        this.drugConStomach = drugConStomach;
    }

    public String getInVivoOrInVitro() {
        return inVivoOrInVitro;
    }

    public void setInVivoOrInVitro(String inVivoOrInVitro) {
        this.inVivoOrInVitro = inVivoOrInVitro;
    }

    public boolean isRitonavirBoosted() {
        return isRitonavirBoosted;
    }

    public void setRitonavirBoosted(boolean ritonavirBoosted) {
        isRitonavirBoosted = ritonavirBoosted;
    }
}
