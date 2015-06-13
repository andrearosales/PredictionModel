/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package predictionmodel;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author aRosales
 */
public class BigPanel extends JPanel {
    
    //private final Timer timer = new Timer(3000, this);
    private ArrayList<String> signals;
    public static int Y_AXIS_AMOUNT = 450;
    public static int X_AXIS_AMOUNT = 100;
    private int counter;
    private int xLength;
    private int titleAmount;
    private int xCoordNumbers;
    
    public BigPanel(ArrayList<String> options, int height) {
        titleAmount = height;
        signals = options;
        xCoordNumbers = 15;
        counter = 1;
        xLength = 23;
        //timer.start();
    }
    
    /*@Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==timer){
            if(!CartesianPanel.finish){
                revalidate();
                counter++;
            }
            else
                revalidate();
        }
    }*/
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(X_AXIS_AMOUNT + (counter * xCoordNumbers * xLength), (Y_AXIS_AMOUNT*signals.size())+titleAmount);
    }
    
}
