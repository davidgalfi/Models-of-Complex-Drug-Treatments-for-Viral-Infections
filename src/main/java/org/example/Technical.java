package org.example;

import org.json.simple.JSONObject;

public class Technical {

    public static final int X = 0;
    public static final int Y = 1;
    public double simulationTime;
    public double timeStep;
    public double interval;
    public int[] dim = new int[2];

    public Technical(JSONObject jsonObject) {
        this.simulationTime = (double) jsonObject.get("simulationTime");
        this.timeStep = (double) jsonObject.get("timeStep");
        this.interval = (double) jsonObject.get("interval");
        this.dim[X] = ((Long) jsonObject.get("xDim")).intValue();
        this.dim[Y] = ((Long) jsonObject.get("yDim")).intValue();
    }
}
