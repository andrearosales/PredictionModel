/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictionmodel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author aRosales
 */
public class SignalOptions extends JFrame{
    private JCheckBox HR;
    private JCheckBox VO2;
    private JLabel title;
    private JButton button;
    private ArrayList<String> options;

    public SignalOptions() {
        HR = new JCheckBox("Heart rate (HR)");
        VO2 = new JCheckBox("Oxygen consumption (VO2)");
        
        title = new JLabel("Select the prediction results to visualize");
        title.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel containerChecks = new JPanel();
        containerChecks.setLayout(new GridLayout(2,1));
        containerChecks.add(HR);
        containerChecks.add(VO2);
        
        button = new JButton("Continue");
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                options = new ArrayList<String>();
                if(HR.isSelected()){
                    options.add("HR");
                }
                if(VO2.isSelected()){
                    options.add("VO2");
                }
                for(int i=0; i<options.size();i++)
                    System.out.println(options.get(i));
                setVisible(false);
                CartesianFrame frame = new CartesianFrame(options);
                frame.showUI();
            }
        });
        
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        add(title);
        add(containerChecks);
        add(button);
        
    }
    
    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Prediction Results");
        pack();
        setVisible(true);
    }
    
        
}
