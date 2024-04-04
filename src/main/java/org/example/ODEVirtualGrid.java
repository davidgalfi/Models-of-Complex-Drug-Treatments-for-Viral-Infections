package org.example;

public class ODEVirtualGrid {
    double[] data;
    int time;
    public ODEVirtualGrid(double[] data){
        this.data = data;
        this.time = 0;
    }

    public double Get(){
        return data[time];
    }
    public void Update(){
        time++;
    }
}
