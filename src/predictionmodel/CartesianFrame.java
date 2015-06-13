/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictionmodel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 *
 * @author aRosales
 */
class CartesianFrame extends JFrame{
    private ArrayList<String> signals;
    
    public CartesianFrame(ArrayList<String> options) {
        signals = options;
        setSize(1000, 800);
        //setLayout(new GridLayout(1,2));
        
        //
            setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
            //Adding the General Title
            
            JLabel labelTitle = new JLabel();
            labelTitle.setText("PREDICTION RESULTS");
            labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(labelTitle);
            
            //Adding each separate container
            BigPanel bigPanel = new BigPanel(options, labelTitle.getHeight());
            bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));
            
            for(int i=0; i<signals.size(); i++){
                String selected = signals.get(i);
                Container container = new Container();
                container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
                
                JLabel title = new JLabel();
                title.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                CartesianPanel panel = new CartesianPanel();
                panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.setBackground(Color.WHITE);
                
                if(selected.equalsIgnoreCase("HR")){
                    title.setText("Heart rate (HR)");
                    panel.setAxes("HR", 58, 164, Color.RED, 2.5);
                }
                else if(selected.equalsIgnoreCase("VO2")){
                    title.setText("Oxygen consumption (VO2)");
                    panel.setAxes("VO2(computed)", 0, 2, Color.BLUE, 2.5);
                }
                
                container.add(title);
                JScrollPane scroll = new JScrollPane(panel);
                container.add(scroll);
                bigPanel.add(container);
            }
            
            JScrollPane bigScrollPanel = new JScrollPane(bigPanel);
            add(bigScrollPanel);
        
    }
    
    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Prediction Data");
        //pack();
        
        setVisible(true);
    }
    
}
