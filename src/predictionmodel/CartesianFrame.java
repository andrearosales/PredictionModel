/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package predictionmodel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * Class that plots the results of the prediction.
 *
 * @author aRosales
 */
class CartesianFrame extends JFrame {

    private final CartesianPanel results;
    private final int font_size_title = 28;
    private final int font_size_labels = 26;
    
    public static int window_width;
    public static int window_height;
    public static String units;

    /**
     * Constructor of the class that initializes the initial plots with the
     * received selected options.
     *
     * @param signal Predicted signal (HR_next, HR_peak, VO2_next or VO2_peak)
     * @param approach Approach that was used for the prediction (Multiple-test or Single-test)
     * @param technique Technique that was used for the prediction (ANN or SVM)
     */
    public CartesianFrame(String signal, String approach, String technique) {
                
        //setSize(1000, 670);
        setSize(window_width, window_height);
        
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        //Adding the general title
        JLabel labelTitle = new JLabel();
        labelTitle.setFont(new Font ("Times", Font.BOLD, font_size_title));
        labelTitle.setText("PREDICTION RESULTS");
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelTitle);

        Container container = new Container();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        results = new CartesianPanel();
        results.setAlignmentX(Component.LEFT_ALIGNMENT);
        results.setBackground(Color.WHITE);
        results.setAxes(Color.BLACK, signal, units);

        JScrollPane scroll = new JScrollPane(results);
        container.add(scroll);

        add(container);

        JLabel predictedValue = new JLabel();
        predictedValue.setFont(new Font ("Times", Font.PLAIN, font_size_labels));
        predictedValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        predictedValue.setText("Predicted value: " + signal);
        
        JLabel predictionModel = new JLabel();
        predictionModel.setFont(new Font ("Times", Font.PLAIN, font_size_labels));
        predictionModel.setAlignmentX(Component.CENTER_ALIGNMENT);
        predictionModel.setText("Prediction model: " + technique);
        
        JLabel knowledgeBase = new JLabel();
        knowledgeBase.setFont(new Font ("Times", Font.PLAIN, font_size_labels));
        knowledgeBase.setAlignmentX(Component.CENTER_ALIGNMENT);
        knowledgeBase.setText("Knowledge base: " + approach);
        
        JLabel blank = new JLabel();
        blank.setFont(new Font ("Times", Font.PLAIN, font_size_labels));
        blank.setAlignmentX(Component.CENTER_ALIGNMENT);
        blank.setText(" ");
        
        /*JLabel plusMAE = new JLabel();
        plusMAE.setFont(new Font ("Times", Font.BOLD, font_size_labels));
        plusMAE.setAlignmentX(Component.CENTER_ALIGNMENT);
        plusMAE.setText(" ---- " + signal + " + MAE");
        plusMAE.setForeground(Color.BLUE);

        JLabel avg = new JLabel();
        avg.setFont(new Font ("Times", Font.BOLD, font_size_labels));
        avg.setAlignmentX(Component.CENTER_ALIGNMENT);
        avg.setText(" _____ " + signal);
        avg.setForeground(Color.BLACK);

        JLabel minusMAE = new JLabel();
        minusMAE.setFont(new Font ("Times", Font.BOLD, font_size_labels));
        minusMAE.setAlignmentX(Component.CENTER_ALIGNMENT);
        minusMAE.setText(" .... " + signal + " - MAE");
        minusMAE.setForeground(Color.GRAY);*/

        JLabel label = new JLabel();
        label.setFont(new Font ("Times", Font.PLAIN, font_size_labels));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setText(signal + " (___);  " + signal + " + MAE (----);  " + signal + " - MAE (....)");
        
        //add(predictedValue);
        add(predictionModel);
        //add(knowledgeBase);
        add(blank);
        //add(plusMAE);
        //add(avg);
        //add(minusMAE);
        add(label);
    }

    /**
     * Method that allows the visualization of the frame.
     */
    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setTitle("Prediction Results");
        setVisible(true);
    }

}
