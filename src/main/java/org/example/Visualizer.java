package org.example;

import HAL.Gui.GridWindow;

import java.util.Map;

import static HAL.Util.*;

public class Visualizer {

    final GridWindow win;

    final static Map<Integer, Integer> colors =
            Map.of(
                    -1, RGB256(255, 255, 255),   // Empty cell color:    white,
                    Cells.T, RGB256(119, 198, 110),  // Target cell color:   green
                    Cells.I, RGB256(124, 65, 120),   // Infected cell color: purple
                    Cells.D, RGB(0, 0, 0)            // Dead cell color:     black
            );

    public Visualizer(int xDim, int yDim) {

        win = new GridWindow(xDim * 2, yDim, 2, true);
    }

    /**
     * Draws the current state of the model on the visualization grid window.
     *
     * This method iterates through each agent in the model, retrieves its information, and sets the corresponding
     * visualization pixel on the grid window. Target cells are represented by a green color, infected cells by a
     * purple color, dead cells by black, and capillary cells by white. Additionally, the concentration of the virus is
     * visualized using a heatmap on the same grid window.
     *
     * @param G The experiment (at its given state) to be visualized.
     */
    public Void drawExperimentState(Experiment G) {
        for (int i = 0; i < G.length; i++) {
            Cells drawMe = G.GetAgent(i);

            win.SetPix(i, drawMe == null ? colors.get(-1) : colors.get(drawMe.cellType));

            // Visualize virus concentration using a heat map
            win.SetPix(G.ItoX(i) + G.xDim, G.ItoY(i), HeatMapRBG(G.infection.virusCon.Get(i)));
        }

        return null;
    }

    public void close() {

        win.Close();
    }
}
