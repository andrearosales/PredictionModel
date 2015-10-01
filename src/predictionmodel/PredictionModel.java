/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package predictionmodel;

import javax.swing.SwingUtilities;

/**
 * Main class of the project. Runs the panel with the results of the prediction.
 *
 * @author aRosales
 */
public class PredictionModel {

    /**
     * Main method that starts the plot visualization.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String signal = args[0];
        final String approach = args[1];
        final String technique = args[2];
        final String error = args[3];
        final String signalFile = args[4];
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CartesianFrame frame = new CartesianFrame(signal, approach, technique, error);
                CartesianFrameSignal frameS = new CartesianFrameSignal(signalFile);
                frame.showUI();
                frameS.showUI();
            }
        });
    }

}
