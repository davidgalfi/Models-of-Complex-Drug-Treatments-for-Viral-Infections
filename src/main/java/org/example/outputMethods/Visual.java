package org.example.outputMethods;

import HAL.Gui.GridWindow;
import org.example.timer.Timer;

import java.util.function.Supplier;

public class Visual {

    final GridWindow win;

    final static int scaleFactor = 2;

    final int xDim;
    final int yDim;

    public Visual(int xDim, int yDim, Integer numberOfFrames) {

        this.xDim = xDim;
        this.yDim = yDim;

        int frames = (numberOfFrames == null) ? 1 : numberOfFrames;

        win = new GridWindow(xDim * frames, yDim, scaleFactor, true);
    }

    public void draw(int i, int color) { win.SetPix(i, color); }

    public void draw(int frameNo, int x, int y, int color) { win.SetPix(x + (frameNo - 1) * xDim, y, color); }

    public void frameReady(Timer timer) {}

    public void close() {

        win.Close();
    }
}
