/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package predictionmodel;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * Panel that contains the plots of the physiological signals.
 *
 * @author aRosales
 */
public class BigPanel extends JPanel{
    /**
     * Specific height of each plot.
     */
    public static int Y_AXIS_AMOUNT = 270; //It was 450

    /**
     * Initial width of each plot.
     */
    public static int X_AXIS_AMOUNT = 100;
    private static int xLength = 23;
    private static int xCoordNumbers = 23;
    private int titleAmount;

    /**
     * Constructor of the class that initializes the values that are needed to
     * specify the panel dimension.
     *
     * @param height Height of the title label.
     */
    public BigPanel(int height) {
        titleAmount = height;
    }

    /**
     * Method that helps the graphic visualization.
     *
     * @return Whole dimension of the panel.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(X_AXIS_AMOUNT + (xCoordNumbers * xLength), (Y_AXIS_AMOUNT * this.getComponents().length) + titleAmount);
    }
}
