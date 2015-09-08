/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package predictionmodel;

import javax.swing.SwingUtilities;

/**
 *
 * @author aRosales Main class of the project. Runs the panel with the results
 * of the prediction.
 */
public class PredictionModel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String title = args[0];
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CartesianFrame frame = new CartesianFrame(title);
                frame.showUI();
            }
        });
    }

}
