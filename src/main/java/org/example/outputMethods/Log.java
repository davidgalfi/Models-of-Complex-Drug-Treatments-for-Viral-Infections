package org.example.outputMethods;

import org.example.timer.Timer;
import HAL.Tools.FileIO;

import java.io.File;
import java.util.function.Supplier;

public class Log {

    final boolean logEveryState;

    final double logInterval;

    double time;

    final boolean outputToConsole;

    /**
     * The FileIO object for writing data to a file during simulation.
     */
    HAL.Tools.FileIO outputFile;

    final String filenameAndPath;

    public Log(Double logInterval) {

        this.logEveryState = (logInterval == null);
        this.logInterval = logEveryState ? 0 : logInterval;
        this.time = this.logInterval;

        this.outputToConsole = true;

        this.filenameAndPath = "";
    }

    public Log(String path, String filename, Double logInterval) {

        this.logEveryState = (logInterval == null);
        this.logInterval = logEveryState ? 0 : logInterval;
        this.time = this.logInterval;

        this.outputToConsole = false;

        this.filenameAndPath = path + filename;

        new File(path).mkdirs();
        outputFile = new FileIO(filenameAndPath, "w");
    }

    public void logString(String str) {

        if (outputToConsole) {

            System.out.print(str);
        } else {

            outputFile.Write(str);
        }
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

    public void close() {

        if (!outputToConsole) { outputFile.Close(); }
    }
}
