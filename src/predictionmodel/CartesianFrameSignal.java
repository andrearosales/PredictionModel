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
 * Class that plots the heart rate and the oxygen consumption signals.
 *
 * @author aRosales
 */
public class CartesianFrameSignal extends JFrame {

    private CartesianPanelSignal results;
    private BigPanel bigPanel;
    private Container contHR;
    private Container contVO2;

    /**
     * Default constructor of the class that will initialize the main panels.
     *
     * @param signalFile Name of the file that contain the heart rate and oxygen
     * consumption measured values.
     */
    public CartesianFrameSignal(String signalFile) {
        setSize(1000, 800);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        //Adding the general title
        JLabel labelTitle = new JLabel();
        labelTitle.setText("MEDICAL SIGNAL REPRESENTATION");
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelTitle);

        //Adding the greater container
        bigPanel = new BigPanel(labelTitle.getHeight());
        bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));

        JScrollPane bigScrollPanel = new JScrollPane(bigPanel);
        add(bigScrollPanel);

        contHR = new Container();
        contHR.setLayout(new BoxLayout(contHR, BoxLayout.Y_AXIS));

        JLabel title = new JLabel();
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setText("Heart rate (HR)");
        contHR.add(title);

        CartesianPanelSignal panel = new CartesianPanelSignal();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setAxes("HR", 33, 215, Color.GRAY, signalFile);
        panel.setBackground(Color.white);
        JScrollPane scroll = new JScrollPane(panel);
        contHR.add(scroll);

        bigPanel.add(contHR);

        contVO2 = new Container();
        contVO2.setLayout(new BoxLayout(contVO2, BoxLayout.Y_AXIS));

        JLabel titleVO2 = new JLabel();
        titleVO2.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleVO2.setText("Oxygen consumption (VO2)");
        contVO2.add(titleVO2);

        CartesianPanelSignal panelVO2 = new CartesianPanelSignal();
        panelVO2.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelVO2.setAxes("VO2", 0, 6411, Color.YELLOW, signalFile);
        panelVO2.setBackground(Color.white);
        JScrollPane scrollVO2 = new JScrollPane(panelVO2);
        contVO2.add(scrollVO2);

        bigPanel.add(contVO2);
    }

    /**
     * Method that allows the visualization of the frame.
     */
    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Medical Signal Representation");
        setVisible(true);
    }

}
