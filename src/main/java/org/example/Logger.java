package org.example;

import org.example.outputMethods.Log;
import org.example.outputMethods.LogToFile;

import java.util.Map;

public class Logger {

    Log log;

    public Logger(Double logInterval) {

        log = new Log(logInterval);
    }

    public Logger(String path, String filename, Double logInterval) {

        log = new LogToFile(path, filename, logInterval);
    }

    String constructHeader(Experiment G) {
        String header = "time, " +
                "target cells, infected cells, dead cells, " +
                "damage ratio, " +
                "total virus concentration";

        for (Treatment treatment : G.treatments) {
            header += ", " + treatment.drug.name + " concentration";
        }
        header += "\n";

        return header;
    }

    String constructStateInfo(Experiment G) {

        Map<String, Number> statistics = Statistics.get(G);

        String stateInfo = "" + G.timer.get() + ", " +
                statistics.get("T") + ", " + statistics.get("I") + ", " + statistics.get("D") + ", " +
                statistics.get("damageRatio") + ", " +
                statistics.get("totalVirusConcentration");

        for (Treatment treatment : G.treatments) {
            stateInfo += ", " + treatment.concentration.Get();
        }
        stateInfo += "\n";

        return stateInfo;
    }

    public void logHeader(Experiment G) {

        log.logString(constructHeader(G) + constructStateInfo(G));
    }

    public Void logState(Experiment G) {

        log.logState(G.timer, () -> constructStateInfo(G));

        return null;
    }

    public void close() {

        log.close();
    }
}
