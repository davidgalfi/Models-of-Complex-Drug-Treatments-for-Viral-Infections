package org.example.outputMethods;

import org.example.timer.Timer;

public class VisualToFile extends Visual {

    final String filenameAndPath;

    final boolean saveEveryPlot;

    final double saveInterval;

    double time;

    public VisualToFile(String path, String filename, Double saveInterval, int xDim, int yDim, Integer numberOfFrames) {
        super(xDim, yDim, numberOfFrames);

        this.filenameAndPath = path + filename;

        this.saveEveryPlot = (saveInterval == null);
        this.saveInterval = saveEveryPlot ? 0 : saveInterval;
        this.time = this.saveInterval;
    }

    @Override
    public void frameReady(Timer timer) {
        if (
            saveEveryPlot ||
            (timer.get() >= time) ||
            (!timer.isNotOver())
        ) {

            time += saveInterval;

            win.ToPNG(filenameAndPath + "_" + Double.toString(timer.get()) + ".png");
        }
    }
}
