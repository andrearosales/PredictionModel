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
 *
 * @author aRosales
 */
class CartesianFrame extends JFrame{
    
    private CartesianPanel results;
    
    public CartesianFrame(String signalTitle) {
        setSize(1000, 600);
        //setLayout(new GridLayout(1,2));
        
        //
            setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
            //Adding the General Title
            
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
            results.setAxes("HR", 58, 164, Color.BLUE, 2.5);
            
            container.add(title);
            JScrollPane scroll = new JScrollPane(results);
            container.add(scroll);
            
            add(container);
            
            JLabel plusMAE = new JLabel();
            plusMAE.setAlignmentX(Component.CENTER_ALIGNMENT);
            plusMAE.setText("AVG_SIGNAL + MAE");
            plusMAE.setForeground(Color.RED);
            
            JLabel avg = new JLabel();
            avg.setAlignmentX(Component.CENTER_ALIGNMENT);
            avg.setText("AVG_SIGNAL");
            avg.setForeground(Color.BLUE);
            
            JLabel minusMAE = new JLabel();
            minusMAE.setAlignmentX(Component.CENTER_ALIGNMENT);
            minusMAE.setText("AVG_SIGNAL - MAE");
            minusMAE.setForeground(Color.GREEN);
            
            add(plusMAE);
            add(avg);
            add(minusMAE);
    }

    
    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Prediction Results");
        //pack();
        
        setVisible(true);
    }
    
}
