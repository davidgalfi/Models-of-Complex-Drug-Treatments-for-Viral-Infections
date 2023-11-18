package com.poortoys.examples;

import HAL.Gui.PlotLine;
import HAL.Gui.PlotWindow;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;

import java.util.ArrayList;

import static HAL.Util.*;

public class SIRmODE2 implements FirstOrderDifferentialEquations {

    private static final int S = 0;
    private static final int I = 1;
    private static final int R = 2;

    private double gamma;
    private double aplha;

    public SIRmODE2(double gamma, double aplha){
        this.gamma = gamma;
        this.aplha = aplha;
    }

    @Override
    public int getDimension() {
        return 3;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[S] = -gamma*y[S]*y[I];
        yDot[I] = gamma*y[S]*y[I] - aplha*y[I];
        yDot[R] = aplha*y[I];
    }

    public static void main(String[] args) {

        double gamma = 0.2;
        double alpha = 0.8;

        double[] y = new double[3];
        double[] y0 = new double[] {99.0, 1.0, 0.0};

        double t = 0.0;

        ArrayList<Double[]> results = new ArrayList<>();
        ArrayList<Double> time = new ArrayList<>();

        time.add(t);
        results.add(new Double[] {y0[S], y[I], y0[R]});

        FirstOrderDifferentialEquations equations = new SIRmODE2(gamma, alpha);
        RungeKuttaIntegrator solver = new ClassicalRungeKuttaIntegrator(0.01);

        StepHandler stepHandler = new StepHandler() {

            @Override
            public void init(double t0, double[] y0, double t) {}

            @Override
            public void handleStep(StepInterpolator interpolator, boolean b) {
                double t = interpolator.getCurrentTime();
                double[] y = interpolator.getInterpolatedState();
                double susceptible = y[0];
                double infectious = y[1];
                double recovered = y[2];
                results.add(new Double[] {susceptible, infectious, recovered});
                time.add(t);
            }
        };
        solver.addStepHandler(stepHandler);

        solver.integrate(equations, t, y0, t + 5.0, y);

        PlotWindow win=new PlotWindow("SIR model",500,500,1,0,0,1,1);
        PlotLine susceptibleLine=new PlotLine(win,BLUE);
        PlotLine infectiousLine=new PlotLine(win,RED);
        PlotLine removedLine=new PlotLine(win, GREEN);

        for (int i = 0; i < results.size(); i++) {
            susceptibleLine.AddSegment(time.get(i),results.get(i)[0]);
            infectiousLine.AddSegment(time.get(i),results.get(i)[1]);
            removedLine.AddSegment(time.get(i), results.get(i)[2]);
            win.TickPause(20);
        }

        win.DrawAxesLabels();
        win.ToPNG("SIRmODE2.png");
    }
}
