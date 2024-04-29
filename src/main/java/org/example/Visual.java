package org.example;

import HAL.Gui.GridWindow;

import static HAL.Util.*;
import static org.example.Cells.*;

public class Visual {

    final GridWindow win;

    public Visual(int xDim, int yDim) {

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

            // Check if the agent is null (empty grid cell)
            if (drawMe == null) {
                win.SetPix(i, RGB256(255, 255, 255)); // Set empty cell color to white
            } else {
                // Set colors based on cell type
                if (drawMe.cellType == T) {        // Target cells
                    win.SetPix(i, RGB256(119, 198, 110)); // Set target cell color to green

                } else if (drawMe.cellType == I) { // Infected cells
                    win.SetPix(i, RGB256(124, 65, 120));  // Set infected cell color to purple

                } else if (drawMe.cellType == D) { // Dead cells
                    win.SetPix(i, RGB(0, 0, 0)); // Set dead cell color to black

                }
            }

            // Visualize virus concentration using a heat map
            win.SetPix(G.ItoX(i) + G.xDim, G.ItoY(i), HeatMapRBG(G.infection.virusCon.Get(i)));
        }

        return null;
    }

    public void close() {

        win.Close();
    }
}
