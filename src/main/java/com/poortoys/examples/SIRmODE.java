package com.poortoys.examples;

import HAL.Gui.PlotLine;
import HAL.Gui.PlotWindow;
import org.apache.commons.math.ode.*;
import org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.apache.commons.math.ode.sampling.StepInterpolator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static HAL.Util.*;

public class SIRmODE implements FirstOrderDifferentialEquations {

    private final int S = 0;
    private final int I = 1;
    private final int R = 2;
    double beta;
    double alpha;

    // Constructor
    public SIRmODE(double beta, double alpha){
        this.beta = beta;
        this.alpha = alpha;
    }

    // Set the dimension of the system
    @Override
    public int getDimension() {
        return 3;
    }

    // Define differential equation's system
    /*Parameters:
    * - double t: time
    * - double[] y: state vector
    * - double[] yDot: stores the dependent variable's derivative*/
    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
        yDot[S] = -beta*y[S]*y[I];
        yDot[I] = beta*y[S]*y[I] - alpha*y[I];
        yDot[R] = alpha*y[I];
    }

    public static void main(String[] args) throws IntegratorException, DerivativeException {

        final int S = 0;
        final int I = 1;
        final int R = 2;

        // Final destination
        final double END = 3;

        // Set parameters
        double beta = 0.2;
        double alpha = 0.8;

        // Initial value {S, I, R}
        //Double[] y0 = {99.0, 1.0, 0.0};
        // This is for the solver as it can't work with objects
        double[] y0 = {99.0, 1.0, 0.0};

        // It'll store the values
        ArrayList<Double[]> results = new ArrayList<>();
        // It'll store the time
        ArrayList<Double> time = new ArrayList<>();
        // This is for the solver as it can't work with objects
        // It stores the last values of S,I,R variables
        double[] y = new double[3];
        // Append time 0.0 and initial values
        time.add(0.0);
        results.add(new Double[] {y0[S], y0[I], y0[R]});

        // Handling first order differential equations
        FirstOrderDifferentialEquations equations = new SIRmODE(beta, alpha);
        // ODE solver
        RungeKuttaIntegrator solver = new ClassicalRungeKuttaIntegrator(0.01);

        // Time index
        Double t = 0.0;

        // Solving equations
        while(t<END){
            solver.integrate(equations, 0, y0, t + 0.01, y);
            results.add(new Double[] {y[S], y[I], y[R]});
            time.add(t+0.01);
            t += 0.01;
        }

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
        win.ToPNG("SIR.png");
    }
}
