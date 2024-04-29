package org.example.outputMethods;

import HAL.Gui.GridWindow;
import org.example.timer.Timer;

import java.util.function.Supplier;

public class Visual {

    final boolean saveToFile;

    String filenameAndPath;

    boolean saveEveryPlot;

    double saveInterval;

    double time;

    final GridWindow win;

    final static int scaleFactor = 2;

    final int xDim;
    final int yDim;

    public Visual(int xDim, int yDim, int... numberOfFrames) {

        this.saveToFile = false;

        this.xDim = xDim;
        this.yDim = yDim;

        int frames = (numberOfFrames.length > 0) ? numberOfFrames[0] : 1;

        win = new GridWindow(xDim * frames, yDim, scaleFactor, true);
    }

    public Visual(String path, String filename, Double saveInterval, int xDim, int yDim, int... numberOfFrames) {

        this.saveToFile = true;

        this.filenameAndPath = path + filename;

        this.saveEveryPlot = (saveInterval == null);
        this.saveInterval = saveEveryPlot ? 0 : saveInterval;
        this.time = this.saveInterval;

        this.xDim = xDim;
        this.yDim = yDim;

        int frames = (numberOfFrames.length > 0) ? numberOfFrames[0] : 1;

        win = new GridWindow(xDim * frames, yDim, scaleFactor, true);
    }

    public void draw(int i, int color) { win.SetPix(i, color); }

    public void draw(int frameNo, int x, int y, int color) { win.SetPix(x + (frameNo - 1) * xDim, y, color); }

    public void frameReady(Timer timer) {
        if (saveToFile) {
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

    public void close() {

        win.Close();
    }
}
