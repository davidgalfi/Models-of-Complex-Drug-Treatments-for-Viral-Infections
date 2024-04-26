package org.example.treatment.dosages;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.example.differentialEquation.TwoCompartmentalPharmacoKinetics;
import org.example.utils.Utils;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class TwoCompartmentalAdministration implements Dosage {

    final double targetDecay;
    final double transferRate;
    final double dosage;
    final double interval;

    final TwoCompartmentalPharmacoKinetics pharmacoKineticalModel;

    //interval is in the technical?
    public TwoCompartmentalAdministration(JSONObject jsonObject) {
        this.targetDecay = (double) jsonObject.get("targetDecay");
        this.transferRate = (double) jsonObject.get("transferRate");
        this.dosage = (double) jsonObject.get("dosage");
        this.interval = (double) jsonObject.get("interval");
        this.pharmacoKineticalModel = new TwoCompartmentalPharmacoKinetics(transferRate, targetDecay);
    }

    @Override
    public double[] getSample(double simulationTime, double timeStep) {
        double[] sample = new double[(int)(simulationTime / timeStep) + 2];

        ArrayList<Double> results = new ArrayList<>();

        FirstOrderDifferentialEquations equation = pharmacoKineticalModel;
        RungeKuttaIntegrator solver = new ClassicalRungeKuttaIntegrator(timeStep);

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void init(double t, double[] y, double v1) {}
            @Override
            public void handleStep(StepInterpolator interpolator, boolean b) {
                double[] y = interpolator.getInterpolatedState();
                results.add(y[1]);
            }
        };

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t, double[] y, double b) {}

            @Override
            public double g(double t, double[] y) {
                return t % (interval * 2) - interval;
            }

            @Override
            public Action eventOccurred(double t, double[] y, boolean b) {
                return Action.RESET_STATE;
            }

            @Override
            public void resetState(double t, double[] y) {
                y[0] = y[0] + dosage;
            }
        };

        solver.addStepHandler(stepHandler);
        solver.addEventHandler(eventHandler, 1, 1.0e-6, 100);

        double[] y = {dosage, 0};

        results.add(y[1]);

        solver.integrate(equation, 0, y, simulationTime, y);

        Utils.addList(sample, results);

        return sample;
    }
}
