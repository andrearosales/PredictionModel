/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package predictionmodel;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.SwingUtilities;
import static predictionmodel.CartesianPanel.predictionStep;

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
        /*final String signal = args[0]; // Predicted signal (HR_next, HR_peak, VO2_next or VO2_peak)
        final String approach = args[1]; // Approach that was used for the prediction (Multiple-test or Single-test)
        final String technique = args[2]; // Technique that was used for the prediction (ANN or SVM)
        final String error = args[3]; // Measurement of error used in the prediction (MAE or RMSE)
        final String signalFile = args[4]; // Name of the file that contain the original physiological signals
        */
        
        String signal = null; // Predicted signal (HR_next, HR_peak, VO2_next or VO2_peak)
        String approach = null; // Approach that was used for the prediction (Multiple-test or Single-test)
        String technique = null; // Technique that was used for the prediction (ANN or SVM)
        String signalFile = null; // Name of the file that contain the original physiological signals
        
        String configurationFile = "Parameter_Configuration.txt";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(configurationFile));
            br.readLine(); //Read the title line
            br.readLine(); //Read the prediction model title
            technique = br.readLine(); //Read the used prediction model
            br.readLine(); //Read the knowledge base title
            approach = br.readLine(); //Read the used knowledge base
            br.readLine(); //Read the predicted value title
            signal = br.readLine(); //Read the used predicted value
            br.readLine(); //Read the signal file title
            signalFile = br.readLine(); //Read the used signal file
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        final String signalF = signal; // Predicted signal (HR_next, HR_peak, VO2_next or VO2_peak)
        final String approachF = approach; // Approach that was used for the prediction (Multiple-test or Single-test)
        final String techniqueF = technique; // Technique that was used for the prediction (ANN or SVM)
        final String signalFileF = signalFile; // Name of the file that contain the original physiological signals
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CartesianFrame frame = new CartesianFrame(signalF, approachF, techniqueF);
                CartesianFrameSignal frameS = new CartesianFrameSignal(signalFileF, signalF);
                frame.showUI();
                frameS.showUI();
            }
        });
    }

}
