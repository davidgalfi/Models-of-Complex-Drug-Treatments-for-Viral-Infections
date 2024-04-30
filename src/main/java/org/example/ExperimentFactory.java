package org.example;

import org.json.simple.JSONObject;

import java.util.ArrayList;

import static org.example.Technical.X;
import static org.example.Technical.Y;

public class ExperimentFactory {

    public static Experiment createExperiment(
            JSONObject jsonObject,
            JSONObject technicalJSONObject,
            JSONObject infectionJSONObject,
            JSONObject treatmentsJSONObject) {

        Technical technical = new Technical(technicalJSONObject);

        Infection infection = InfectionFactory.createInfection(infectionJSONObject, technical.dim[X], technical.dim[Y]);

        ArrayList<Treatment> treatments = new ArrayList<>();

        for (Object treatment : treatmentsJSONObject.keySet()) {
            JSONObject currentTreatment = (JSONObject) treatmentsJSONObject.get(treatment);
            treatments.add(TreatmentFactory.createTreatment(currentTreatment, technical.simulationTime, technical.timeStep));
        }

        return new Experiment(infection, treatments, technical);
    }
}
