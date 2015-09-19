/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package predictionmodel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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

    private CartesianPanel results;

    /**
     * Constructor of the class that initializes the initial plots with the
     * received selected options.
     *
     * @param signalTitle Title of the predicted signal
     */
    public CartesianFrame(String signalTitle) {
        setSize(1000, 600);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        //Adding the general title
        JLabel labelTitle = new JLabel();
        labelTitle.setText("PREDICTION RESULTS");
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelTitle);

        Container container = new Container();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JLabel title = new JLabel();
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setText(signalTitle);

        results = new CartesianPanel();
        results.setAlignmentX(Component.LEFT_ALIGNMENT);
        results.setBackground(Color.WHITE);
        results.setAxes(Color.BLUE);

        container.add(title);
        JScrollPane scroll = new JScrollPane(results);
        container.add(scroll);

        add(container);

        JLabel plusMAE = new JLabel();
        plusMAE.setAlignmentX(Component.CENTER_ALIGNMENT);
        plusMAE.setText("SIGNAL + Error Measure");
        plusMAE.setForeground(Color.RED);

        JLabel avg = new JLabel();
        avg.setAlignmentX(Component.CENTER_ALIGNMENT);
        avg.setText("SIGNAL");
        avg.setForeground(Color.BLUE);

        JLabel minusMAE = new JLabel();
        minusMAE.setAlignmentX(Component.CENTER_ALIGNMENT);
        minusMAE.setText("SIGNAL - Error Measure");
        minusMAE.setForeground(Color.GREEN);

        add(plusMAE);
        add(avg);
        add(minusMAE);
    }

    /**
     * Method that allows the visualization of the frame.
     */
    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Prediction Results");
        setVisible(true);
    }

}
