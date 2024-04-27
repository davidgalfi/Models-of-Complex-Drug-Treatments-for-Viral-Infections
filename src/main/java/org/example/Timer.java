package org.example;

public class Timer {

    final public double totalTime;

    final public double timeStep;

    double time;

    public Timer(double simulationTime, double timeStep) {

        this.totalTime = simulationTime;
        this.timeStep = timeStep;

        this.time = 0.0;
    }

    public double get() {

        return time;
    }

    public void step() {
        time += timeStep;
    }

    public boolean isNotOver() {

        return time <= totalTime;
    }
}
