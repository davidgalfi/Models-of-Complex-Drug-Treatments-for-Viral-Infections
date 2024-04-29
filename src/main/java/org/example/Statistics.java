package org.example;

import java.util.HashMap;
import java.util.Map;

public class Statistics {

    public static Map<String, Number> getCellStatistics(Experiment G){

        double targetCellCount = 0, infectedCellCount = 0, deadCellCount = 0;

        for (Cells cell: G){

            if (cell.cellType == Cells.T){
                targetCellCount += 1;

            }
            else if (cell.cellType == Cells.I ){
                infectedCellCount += 1;

            }
            else if (cell.cellType == Cells.D){
                deadCellCount += 1;

            }
        }

        Map<String, Number> statistics = new HashMap<>();

        statistics.put("T", targetCellCount);
        statistics.put("I", infectedCellCount);
        statistics.put("D", deadCellCount);
        statistics.put("damageRatio", 1 - targetCellCount / G.length);

        return statistics;
    }

    public static Map<String, Number> getInfectionStatistics(Experiment G) {

        double totalVirusCon = 0.0;

        for (int i = 0; i < G.length; i++){

            totalVirusCon = totalVirusCon + G.infection.virusCon.Get(i);
        }

        Map<String, Number> statistics = new HashMap<>();

        statistics.put("totalVirusConcentration", totalVirusCon);

        return statistics;
    }

    public static Map<String, Number> get(Experiment G) {

        Map<String, Number> statistics = new HashMap<>(getCellStatistics(G));

        statistics.putAll(getInfectionStatistics(G));

        return statistics;
    }
}
