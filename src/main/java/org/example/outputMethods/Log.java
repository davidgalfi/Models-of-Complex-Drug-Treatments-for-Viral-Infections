package org.example.outputMethods;

import java.util.function.Supplier;

import org.example.timer.Timer;

public class Log {

    final boolean logEveryState;

    final double logInterval;

    double time;

    public Log(Double logInterval) {

        this.logEveryState = (logInterval == null);
        this.logInterval = logEveryState ? 0 : logInterval;
        this.time = this.logInterval;
    }

    public void logString(String str) {

        System.out.print(str);
    }

    public void logState(Timer timer, Supplier<String> constructState) {

        if (
                logEveryState ||
                (timer.get() >= time) ||
                (!timer.isNotOver())
        ) {

            time += logInterval;

            logString(constructState.get());
        }
    }

    public void close() {}
}
