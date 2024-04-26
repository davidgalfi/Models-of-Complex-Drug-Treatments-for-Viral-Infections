package org.example.treatment;

public class ODEVirtualGrid {
    double[] data;
    int localTime;
    public ODEVirtualGrid(double[] data){
        this.data = data;
        this.localTime = 0;
    }

    public double Get(Object... args){
        return data[localTime];
    }
    public void Update(Object... args){
        localTime++;
    }
}
