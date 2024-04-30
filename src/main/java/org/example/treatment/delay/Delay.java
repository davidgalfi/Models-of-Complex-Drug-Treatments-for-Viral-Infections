package org.example.treatment.delay;

public class Delay {

    public final int type;
    public static final int TimeDelay = 0;
    public static final int DamageDelay = 1;

    public final double bound;

    public Delay(int type, double bound) {

        this.type = type;
        this.bound = bound;
    }

    public boolean isOver(double time, double damageRatio) {

        return (type == TimeDelay ? time : damageRatio) >= bound;
    };
}
