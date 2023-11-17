package com.poortoys.examples.onlyHal;

import HAL.Gui.PlotLine;
import HAL.Gui.PlotWindow;
import HAL.Tools.ODESolver.ODESolver;

import java.util.ArrayList;

import static HAL.Util.*;

public class SIR {
    public final double infectionRate;
    public final double recoveryRate;

    public final static int SUSCEPTIBLE=0,INFECTIOUS=1,REMOVED=2;

    public SIR(double infectionRate, double recoveryRate) {
        this.infectionRate = infectionRate;
        this.recoveryRate = recoveryRate;
    }

    public void populationDerivatives(double t,double[]pops,double[]deltas){
        deltas[SUSCEPTIBLE]=-infectionRate*pops[SUSCEPTIBLE]*pops[INFECTIOUS];
        deltas[INFECTIOUS]=infectionRate*pops[SUSCEPTIBLE]*pops[INFECTIOUS]-recoveryRate*pops[INFECTIOUS];
        deltas[REMOVED]=recoveryRate*pops[INFECTIOUS];
    }

    public static void main(String[] args) {
        double infectionRate = 0.1;
        double recoveryRate = 0.8;

        double susceptiblePeopleAtStart = 99;
        double infectiousPeopleAtStart = 1;
        double removedPeopleAtStart = 0;

        SIR model=new SIR(infectionRate,recoveryRate);
        double[]startPops=new double[]{susceptiblePeopleAtStart,infectiousPeopleAtStart,removedPeopleAtStart};

        ArrayList<Double> ts=new ArrayList<>();
        ts.add(0.0);

        ArrayList<double[]> states=new ArrayList<>();
        states.add(startPops);

        PlotWindow win=new PlotWindow("SIR model",500,500,1,0,0,1,1);
        PlotLine susceptibleLine=new PlotLine(win,BLUE);
        PlotLine infectiousLine=new PlotLine(win,RED);
        PlotLine removedLine=new PlotLine(win, GREEN);

        ODESolver solver=new ODESolver();

        double dtStart=0.001;
        double errorTol=0.001;

        solver.Runge45(model::populationDerivatives,states,ts,10,dtStart,errorTol,0);
        for (int i = 0; i < states.size(); i++) {
            susceptibleLine.AddSegment(ts.get(i),states.get(i)[0]);
            infectiousLine.AddSegment(ts.get(i),states.get(i)[1]);
            removedLine.AddSegment(ts.get(i), states.get(i)[2]);
            win.TickPause(100);
        }

        win.DrawAxesLabels();
        win.ToPNG("SIRModel.png");
    }
}

