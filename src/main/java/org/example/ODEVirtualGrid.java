package org.example;

public class ODEVirtualGrid {
    double[] data;
    int time;
    public ODEVirtualGrid(){}

    public double Get(){
        return data[time];
    }
    public void Update(){
        time++;
    }
}
