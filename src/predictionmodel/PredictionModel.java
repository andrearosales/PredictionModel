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
        final String signal = args[0]; // Predicted signal (HR_next, HR_peak, VO2_next or VO2_peak)
        final String approach = args[1]; // Approach that was used for the prediction (Multiple-test or Single-test)
        final String technique = args[2]; // Technique that was used for the prediction (ANN or SVM)
        final String error = args[3]; // Measurement of error used in the prediction (MAE or RMSE)
        final String signalFile = args[4]; // Name of the file that contain the original physiological signals
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
