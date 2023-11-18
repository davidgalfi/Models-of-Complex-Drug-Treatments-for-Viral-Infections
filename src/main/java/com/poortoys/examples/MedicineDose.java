package com.poortoys.examples;

import HAL.Gui.PlotLine;
import HAL.Gui.PlotWindow;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;

import java.util.ArrayList;

import static HAL.Util.RED;

public class MedicineDose implements FirstOrderDifferentialEquations {

    private final double gamma;

    public MedicineDose(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = -gamma*y[0];
    }

    public static void main(String[] args) {

        double gamma = 0.1;
        double dose = 0.5;
        double doseTime = 10;

        double[] y = {1.2};

        double t = 0.0;

        ArrayList<Double> results = new ArrayList<Double>();
        ArrayList<Double> time = new ArrayList<Double>();

        results.add(y[0]);
        time.add(t);

        FirstOrderDifferentialEquations equation = new MedicineDose(gamma);
        RungeKuttaIntegrator solver = new ClassicalRungeKuttaIntegrator(1);

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void init(double t, double[] y, double v1) {}
            @Override
            public void handleStep(StepInterpolator interpolator, boolean b) {
                double t = interpolator.getCurrentTime();
                double[] y = interpolator.getInterpolatedState();
                results.add(y[0]);
                time.add(t);
            }
        };

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t, double[] y, double b) {}

            @Override
            public double g(double t, double[] y) {
                return t % 60.0 - 30;
            }

            @Override
            public Action eventOccurred(double t, double[] y, boolean b) {
                System.out.println("Event occurred at t = " + t);
                System.out.println("t: " + t);
                return Action.RESET_STATE;
            }

            @Override
            public void resetState(double t, double[] y) {
                y[0] = y[0] + dose;
            }
        };

        solver.addStepHandler(stepHandler);
        solver.addEventHandler(eventHandler, 1, 1.0e-6, 100);

        solver.integrate(equation, t, y, t + 360, y);

        PlotWindow win=new PlotWindow("Drug resistance",500,500,1,0,0,1,1);
        PlotLine drugresistance=new PlotLine(win,RED);

        for (int i = 0; i < results.size(); i++) {
            drugresistance.AddSegment(time.get(i),results.get(i));
            win.TickPause(50);
        }

        win.DrawAxesLabels();
        win.ToPNG("DrugResistance.png");
    }
}
