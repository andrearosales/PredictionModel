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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

/**
 * Class that plots the selected physiological signals.
 *
 * @author aRosales
 */
class CartesianFrameSignal extends JFrame implements ItemListener {

    private ArrayList<String> signals;
    private ArrayList<String> nameSignals;
    private ArrayList<Integer> minValueSignals;
    private ArrayList<Integer> maxValueSignals;
    private ArrayList<Integer> numDivisionSignals;
    private String[] defaultSignals;
    private BigPanel bigPanel;
    private JPanel containerChecks;
    private JCheckBox FIO2;
    private JCheckBox FEO2;
    private JCheckBox FECO2;
    private JCheckBox FETCO2;
    private JCheckBox FETO2;
    private JCheckBox VE;
    private JCheckBox TI;
    private JCheckBox TE;
    private JCheckBox HR;
    private JCheckBox VO2;
    private Container contFIO2;
    private Container contFEO2;
    private Container contFECO2;
    private Container contFETCO2;
    private Container contFETO2;
    private Container contVE;
    private Container contTI;
    private Container contTE;
    private Container contHR;
    private Container contVO2;
    private String signalFile;
    private String predictedSignal;
    private String[] xAxis;
    private int fontSize;
    private int numberSignals = 10;
    private int numberWindows;
    private int yCoordinate;
    private int heightPlot;
    private int durationStep;

    /**
     * Default constructor of the class that will initialize the main panels.
     *
     * @param signalFile Name of the file that contain physiological signals
     * measured values.
     */
    CartesianFrameSignal(String signalFile, String predictedSignal) {
        fontSize = 15;
        this.signalFile = signalFile;
        this.predictedSignal = predictedSignal;
        setSize(1000, 800);
        
        String configurationFile = "Parameter_Configuration.txt";
        BufferedReader br = null;
        String line;
        String txtSplitBy = " ";
        
        nameSignals = new ArrayList<>();
        minValueSignals = new ArrayList<>();
        maxValueSignals = new ArrayList<>();
        numDivisionSignals = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(configurationFile));
            for(int i=0;i<11;i++){
                br.readLine();
            }
            for(int i=0; i<numberSignals;i++){
                line = br.readLine();
                String[] values = line.split(txtSplitBy);
                nameSignals.add(values[0]);
                minValueSignals.add(Integer.valueOf(values[1]));
                maxValueSignals.add(Integer.valueOf(values[2]));
                numDivisionSignals.add(Integer.valueOf(values[3]));
            }
            br.readLine(); //Read the window configuration title
            line = br.readLine(); //Read the line with all the information
            String[] values = line.split(txtSplitBy);
            numberWindows = Integer.valueOf(values[0]);
            yCoordinate = Integer.valueOf(values[1]);
            heightPlot = Integer.valueOf(values[2]);
            br.readLine(); //Read the default signals title
            line = br.readLine(); //Read the line with all the default signals
            defaultSignals = line.split(txtSplitBy);
            br.readLine(); //Read the default signals title
            line = br.readLine(); //Read the duration of prediction step title
            durationStep = Integer.valueOf(line);
            br.readLine(); //Read the x axis title title
            line = br.readLine(); //Read the x axis information
            xAxis = line.split(txtSplitBy); //As a vector if later on the min/sec configuration is implemented.
            
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
        
        initializeOptions();
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        //Adding the general title
        JLabel labelTitle = new JLabel();
        labelTitle.setFont(new Font ("Times", Font.BOLD, fontSize+5));
        labelTitle.setText("PHYSIOLOGICAL SIGNALS MONITORING");
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelTitle);

        CartesianPanelSignal.X_AXIS_Y_COORD = yCoordinate;
        CartesianPanelSignal.Y_AXIS_SECOND_Y_COORD = yCoordinate;     
        CartesianPanelSignal.xAxis = xAxis[0];
        CartesianPanelSignal.durationPrediction = durationStep;
        
        //Adding the greater container
        bigPanel = new BigPanel(labelTitle.getHeight());
        bigPanel.Y_AXIS_AMOUNT = heightPlot;
        bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));

        JScrollPane bigScrollPanel = new JScrollPane(bigPanel);
        add(bigScrollPanel);
        add(containerChecks);
        
        //Default selection
        for(int i = 0;i<1;i++){
            String signal = defaultSignals[i];
            if(signal.contains("FIO2"))
                FIO2.doClick();
            else if(signal.contains("FEO2"))
                FEO2.doClick();
            else if(signal.contains("FECO2"))
                FECO2.doClick();
            else if(signal.contains("FETCO2"))
                FETCO2.doClick();
            else if(signal.contains("FETO2"))
                FETO2.doClick();
            else if(signal.contains("VE"))
                VE.doClick();
            else if(signal.contains("TI"))
                TI.doClick();
            else if(signal.contains("TE"))
                TE.doClick();
            else if(signal.contains("HR"))
                HR.doClick();
            else 
                VO2.doClick();
        }
    }

    /**
     * Inherited method that notifies each time an item was selected.
     *
     * @param e Item that was selected.
     */
    @Override
    public void itemStateChanged(ItemEvent e) {

        Object source = e.getItemSelectable();
        if (source == FIO2) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                bigPanel.remove(contFIO2);
                contFIO2 = null;
            } else {
                if (contFIO2 == null) {
                    contFIO2 = new Container();
                    contFIO2.setLayout(new BoxLayout(contFIO2, BoxLayout.Y_AXIS));

                    JLabel title = new JLabel();
                    title.setFont(new Font ("Times", Font.BOLD, fontSize));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Fraction of inspired oxygen (FIO2)");
                    contFIO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    //panel.setAxes("FIO2", 0, 22, Color.BLACK, signalFile);
                    panel.setAxes(nameSignals.get(0), minValueSignals.get(0), maxValueSignals.get(0), numDivisionSignals.get(0), Color.BLACK, signalFile);
                    panel.setBackground(Color.white);
                    JScrollPane scroll = new JScrollPane(panel);
                    contFIO2.add(scroll);

                    bigPanel.add(contFIO2);
                }
            }
        } else if (source == FEO2) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                bigPanel.remove(contFEO2);
                contFEO2 = null;
            } else {
                if (contFEO2 == null) {
                    contFEO2 = new Container();
                    contFEO2.setLayout(new BoxLayout(contFEO2, BoxLayout.Y_AXIS));

                    JLabel title = new JLabel();
                    title.setFont(new Font ("Times", Font.BOLD, fontSize));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Fraction of expired oxygen (FEO2)");
                    contFEO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    //panel.setAxes("FEO2", 0, 22, Color.LIGHT_GRAY, signalFile);
                    panel.setAxes(nameSignals.get(1), minValueSignals.get(1), maxValueSignals.get(1), numDivisionSignals.get(1), Color.GRAY, signalFile);
                    panel.setBackground(Color.white);
                    JScrollPane scroll = new JScrollPane(panel);
                    contFEO2.add(scroll);

                    bigPanel.add(contFEO2);
                }
            }
        } else if (source == FECO2) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                bigPanel.remove(contFECO2);
                contFECO2 = null;
            } else {
                if (contFECO2 == null) {
                    contFECO2 = new Container();
                    contFECO2.setLayout(new BoxLayout(contFECO2, BoxLayout.Y_AXIS));

                    JLabel title = new JLabel();
                    title.setFont(new Font ("Times", Font.BOLD, fontSize));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Fraction of expired carbon dioxide (FECO2)");
                    contFECO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    //panel.setAxes("FECO2", 0, 8, Color.YELLOW, signalFile);
                    panel.setAxes(nameSignals.get(2), minValueSignals.get(2), maxValueSignals.get(2), numDivisionSignals.get(2), Color.YELLOW, signalFile);
                    panel.setBackground(Color.white);
                    JScrollPane scroll = new JScrollPane(panel);
                    contFECO2.add(scroll);

                    bigPanel.add(contFECO2);
                }
            }
        } else if (source == FETCO2) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                bigPanel.remove(contFETCO2);
                contFETCO2 = null;
            } else {
                if (contFETCO2 == null) {
                    contFETCO2 = new Container();
                    contFETCO2.setLayout(new BoxLayout(contFETCO2, BoxLayout.Y_AXIS));

                    JLabel title = new JLabel();
                    title.setFont(new Font ("Times", Font.BOLD, fontSize));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Fraction of end-tidal carbon dioxide (FETCO2)");
                    contFETCO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    //panel.setAxes("FETCO2", 0, 10, Color.PINK, signalFile);
                    panel.setAxes(nameSignals.get(3), minValueSignals.get(3), maxValueSignals.get(3), numDivisionSignals.get(3), Color.PINK, signalFile);
                    panel.setBackground(Color.white);
                    JScrollPane scroll = new JScrollPane(panel);
                    contFETCO2.add(scroll);

                    bigPanel.add(contFETCO2);
                }
            }
        } else if (source == FETO2) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                bigPanel.remove(contFETO2);
                contFETO2 = null;
            } else {
                if (contFETO2 == null) {
                    contFETO2 = new Container();
                    contFETO2.setLayout(new BoxLayout(contFETO2, BoxLayout.Y_AXIS));

                    JLabel title = new JLabel();
                    title.setFont(new Font ("Times", Font.BOLD, fontSize));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Fraction of end-tidal oxygen (FETO2)");
                    contFETO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    //panel.setAxes("FETO2", 0, 21, Color.ORANGE, signalFile);
                    panel.setAxes(nameSignals.get(4), minValueSignals.get(4), maxValueSignals.get(4), numDivisionSignals.get(4), Color.ORANGE, signalFile);
                    panel.setBackground(Color.white);
                    JScrollPane scroll = new JScrollPane(panel);
                    contFETO2.add(scroll);

                    bigPanel.add(contFETO2);
                }
            }
        } else if (source == VE) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                bigPanel.remove(contVE);
                contVE = null;
            } else {
                if (contVE == null) {
                    contVE = new Container();
                    contVE.setLayout(new BoxLayout(contVE, BoxLayout.Y_AXIS));

                    JLabel title = new JLabel();
                    title.setFont(new Font ("Times", Font.BOLD, fontSize));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Ventilation (VE)");
                    contVE.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    //panel.setAxes("VE", 0, 234, Color.MAGENTA, signalFile);
                    panel.setAxes(nameSignals.get(5), minValueSignals.get(5), maxValueSignals.get(5), numDivisionSignals.get(5), Color.MAGENTA, signalFile);
                    panel.setBackground(Color.white);
                    JScrollPane scroll = new JScrollPane(panel);
                    contVE.add(scroll);

                    bigPanel.add(contVE);
                }
            }
        } else if (source == TI) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                bigPanel.remove(contTI);
                contTI = null;
            } else {
                if (contTI == null) {
                    contTI = new Container();
                    contTI.setLayout(new BoxLayout(contTI, BoxLayout.Y_AXIS));

                    JLabel title = new JLabel();
                    title.setFont(new Font ("Times", Font.BOLD, fontSize));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Inspiratory time (IT)");
                    contTI.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    //panel.setAxes("TI", 0, 6, Color.GRAY, signalFile);
                    panel.setAxes(nameSignals.get(6), minValueSignals.get(6), maxValueSignals.get(6), numDivisionSignals.get(6), Color.GRAY, signalFile);
                    panel.setBackground(Color.white);
                    JScrollPane scroll = new JScrollPane(panel);
                    contTI.add(scroll);

                    bigPanel.add(contTI);
                }
            }
        } else if (source == TE) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                bigPanel.remove(contTE);
                contTE = null;
            } else {
                if (contTE == null) {
                    contTE = new Container();
                    contTE.setLayout(new BoxLayout(contTE, BoxLayout.Y_AXIS));

                    JLabel title = new JLabel();
                    title.setFont(new Font ("Times", Font.BOLD, fontSize));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Expiratory time (ET)");
                    contTE.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    //panel.setAxes("TE", 0, 12, Color.CYAN, signalFile);
                    panel.setAxes(nameSignals.get(7), minValueSignals.get(7), maxValueSignals.get(7), numDivisionSignals.get(7), Color.CYAN, signalFile);
                    panel.setBackground(Color.white);
                    JScrollPane scroll = new JScrollPane(panel);
                    contTE.add(scroll);

                    bigPanel.add(contTE);
                }
            }
        } else if (source == HR) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                bigPanel.remove(contHR);
                contHR = null;
            } else {
                if (contHR == null) {
                    contHR = new Container();
                    contHR.setLayout(new BoxLayout(contHR, BoxLayout.Y_AXIS));

                    JLabel title = new JLabel();
                    title.setFont(new Font ("Times", Font.BOLD, fontSize));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Heart rate (HR)");
                    contHR.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    //panel.setAxes("HR", 33, 215, Color.RED, signalFile);
                    panel.setAxes(nameSignals.get(8), minValueSignals.get(8), maxValueSignals.get(8), numDivisionSignals.get(8), Color.RED, signalFile);
                    panel.setBackground(Color.white);
                    JScrollPane scroll = new JScrollPane(panel);
                    contHR.add(scroll);

                    bigPanel.add(contHR);
                }
            }
        } else if (source == VO2) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                bigPanel.remove(contVO2);
                contVO2 = null;
            } else {
                if (contVO2 == null) {
                    contVO2 = new Container();
                    contVO2.setLayout(new BoxLayout(contVO2, BoxLayout.Y_AXIS));

                    JLabel title = new JLabel();
                    title.setFont(new Font ("Times", Font.BOLD, fontSize));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Oxygen consumption (VO2)");
                    contVO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    //panel.setAxes("VO2", 0, 6411, Color.BLUE, signalFile);
                    panel.setAxes(nameSignals.get(9), minValueSignals.get(9), maxValueSignals.get(9), numDivisionSignals.get(9), Color.BLUE, signalFile);
                    panel.setBackground(Color.white);
                    JScrollPane scroll = new JScrollPane(panel);
                    contVO2.add(scroll);

                    bigPanel.add(contVO2);
                }
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Method that initializes the panel with all the possible signal options.
     */
    public void initializeOptions() {

        FIO2 = new JCheckBox("Fraction of inspired oxygen (FIO2)");
        FIO2.setFont(new Font ("Times", Font.PLAIN, fontSize));
        FIO2.addItemListener(this);
        FEO2 = new JCheckBox("Fraction of expired oxygen (FEO2)");
        FEO2.setFont(new Font ("Times", Font.PLAIN, fontSize));
        FEO2.addItemListener(this);
        FECO2 = new JCheckBox("Fraction of expired carbon dioxide (FECO2)");
        FECO2.setFont(new Font ("Times", Font.PLAIN, fontSize));
        FECO2.addItemListener(this);
        FETCO2 = new JCheckBox("Fraction of end-tidal carbon dioxide (FETCO2)");
        FETCO2.setFont(new Font ("Times", Font.PLAIN, fontSize));
        FETCO2.addItemListener(this);
        FETO2 = new JCheckBox("Fraction of end-tidal oxygen (FETO2)");
        FETO2.setFont(new Font ("Times", Font.PLAIN, fontSize));
        FETO2.addItemListener(this);
        VE = new JCheckBox("Ventilation (VE)");
        VE.setFont(new Font ("Times", Font.PLAIN, fontSize));
        VE.addItemListener(this);
        TI = new JCheckBox("Inspiratory time (IT)");
        TI.setFont(new Font ("Times", Font.PLAIN, fontSize));
        TI.addItemListener(this);
        TE = new JCheckBox("Expiratory time (ET)");
        TE.setFont(new Font ("Times", Font.PLAIN, fontSize));
        TE.addItemListener(this);
        HR = new JCheckBox("Heart rate (HR)");
        HR.setFont(new Font ("Times", Font.PLAIN, fontSize));
        HR.addItemListener(this);
        VO2 = new JCheckBox("Oxygen consumption (VO2)");
        VO2.setFont(new Font ("Times", Font.PLAIN, fontSize));
        VO2.addItemListener(this);

        containerChecks = new JPanel();
        containerChecks.setLayout(new GridLayout(5, 2));
        containerChecks.add(FIO2);
        containerChecks.add(FEO2);
        containerChecks.add(FECO2);
        containerChecks.add(FETCO2);
        containerChecks.add(FETO2);
        containerChecks.add(VE);
        containerChecks.add(TI);
        containerChecks.add(TE);
        containerChecks.add(HR);
        containerChecks.add(VO2);

    }

    /**
     * Method that allows the visualization of the frame with the different
     * options and the selected plots.
     */
    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Medical Signal Representation");
        setVisible(true);
    }
}
