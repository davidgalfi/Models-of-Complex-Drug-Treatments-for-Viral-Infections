package com.poortoys.examples;

import HAL.Gui.PlotLine;
import HAL.Gui.PlotWindow;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.FirstOrderIntegrator;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator;

import java.util.ArrayList;
import java.util.List;

import static HAL.Util.BLUE;
import static HAL.Util.RED;

public class AppMain {

    public static void main(String[] args) throws IntegratorException, DerivativeException {
        double constant = 0.1;
        double[] y0 = {1.0};

        FirstOrderDifferentialEquations equations = new PopulationODE(constant);

        RungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.1);

        double t = 0.0;
        double tEnd = 10;
        double[] y = new double[1];

        ArrayList<Double> results = new ArrayList<Double>();
        ArrayList<Double> ts = new ArrayList<Double>();

        results.add(1.0);
        ts.add(0.0);

        while(t<tEnd){
            integrator.integrate(equations, 0, y0, t + 1, y);
            results.add(y[0]);
            ts.add(t+1.0);
            t += 1.0;
        }

        /*for(Double num: results){
            System.out.print(num + ", ");
        }*/

        PlotWindow win=new PlotWindow("Population model",500,500,1,0,0,1,1);
        PlotLine population=new PlotLine(win,RED);

        for (int i = 0; i < results.size(); i++) {
            population.AddSegment(ts.get(i),results.get(i));
            win.TickPause(100);
        }

        win.DrawAxesLabels();
        win.ToPNG("Population.png");
    }
}
