package org.example.outputMethods;

import HAL.Gui.GridWindow;

public class Visual {

    final GridWindow win;

    final static int scaleFactor = 2;

    final int xDim;
    final int yDim;

    public Visual(int xDim, int yDim, int... numberOfFrames) {

        this.xDim = xDim;
        this.yDim = yDim;

        int frames = (numberOfFrames.length > 0) ? numberOfFrames[0] : 1;

        win = new GridWindow(xDim * frames, yDim, scaleFactor, true);
    }

    public void draw(int i, int color) { win.SetPix(i, color); }

    public void draw(int frameNo, int x, int y, int color) { win.SetPix(x + (frameNo - 1) * xDim, y, color); }

    public void close() {

        win.Close();
    }
}
