package org.example.treatment;

import org.example.treatment.efficacies.Efficacy;

import java.util.Map;

public class Drug {

    final public String name;

    final public Map<String, Efficacy> efficacy;

    final static String[] efficacies = {
            "virusRemoval",
            "cytotoxicity",
            "virusProductionReduction",
            "infectionReduction" };

    public Drug(String name, Map<String, Efficacy> efficacy) {

        this.name = name;
        this.efficacy = efficacy;
    }
}
