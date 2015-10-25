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

/**
 * Class that plots the selected physiological signals.
 *
 * @author aRosales
 */
class CartesianFrameSignal extends JFrame implements ItemListener {

    private ArrayList<String> nameSignals;
    private ArrayList<String> unitsSignals;
    private ArrayList<Integer> minValueSignals;
    private ArrayList<Integer> maxValueSignals;
    private ArrayList<Integer> numDivisionSignals;
    private String[] defaultSignals;
    private final BigPanel bigPanel;
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
    private String[] xAxis;
    private final int font_size_title = 28;
    private final int font_size_labels = 26;
    private int numberSignals = 10;
    private int numberWindows;
    private int yCoordinate;
    private int heightPlot;
    private int durationStep;
    private int width;
    private int height;

    /**
     * Default constructor of the class that will initialize the main panels.
     *
     * @param signalFile Name of the file that contain physiological signals
     * measured values.
     */
    CartesianFrameSignal(String signalFile) {
        this.signalFile = signalFile;
        //setSize(1000, 670);

        String configurationFile = "Parameter_Configuration.txt";
        BufferedReader br = null;
        String line;
        String txtSplitBy = " ";

        nameSignals = new ArrayList<>();
        minValueSignals = new ArrayList<>();
        maxValueSignals = new ArrayList<>();
        numDivisionSignals = new ArrayList<>();
        unitsSignals = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(configurationFile));
            for (int i = 0; i < 15; i++) {
                br.readLine();
            }
            line = br.readLine();
            String[] window_val = line.split(txtSplitBy);
            width = Integer.valueOf(window_val[0]);
            height = Integer.valueOf(window_val[1]);
            setSize(width, height); //Read the parameters for the window size
            br.readLine(); //Read the signal title
            for (int i = 0; i < numberSignals; i++) {
                line = br.readLine();
                String[] values = line.split(txtSplitBy);
                nameSignals.add(values[0]);
                minValueSignals.add(Integer.valueOf(values[1]));
                maxValueSignals.add(Integer.valueOf(values[2]));
                unitsSignals.add(values[3]);
                numDivisionSignals.add(Integer.valueOf(values[4]));
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
        labelTitle.setFont(new Font("Times", Font.BOLD, font_size_title));
        labelTitle.setText("PHYSIOLOGICAL SIGNALS MONITORING");
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelTitle);

        CartesianPanelSignal.X_AXIS_Y_COORD = yCoordinate;
        CartesianPanelSignal.Y_AXIS_SECOND_Y_COORD = yCoordinate;
        CartesianPanelSignal.xAxis = xAxis[0];
        CartesianPanelSignal.durationPrediction = durationStep;

        //Adding the greater container
        bigPanel = new BigPanel(labelTitle.getHeight());
        BigPanel.Y_AXIS_AMOUNT = heightPlot;
        bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));

        JScrollPane bigScrollPanel = new JScrollPane(bigPanel);
        add(bigScrollPanel);
        add(containerChecks);

        //Default selection
        for (int i = 0; i < numberWindows; i++) {
            String signal = defaultSignals[i];
            if (signal.contains("FIO2")) {
                FIO2.doClick();
            } else if (signal.contains("FEO2")) {
                FEO2.doClick();
            } else if (signal.contains("FECO2")) {
                FECO2.doClick();
            } else if (signal.contains("FETCO2")) {
                FETCO2.doClick();
            } else if (signal.contains("FETO2")) {
                FETO2.doClick();
            } else if (signal.contains("VE")) {
                VE.doClick();
            } else if (signal.contains("TI")) {
                TI.doClick();
            } else if (signal.contains("TE")) {
                TE.doClick();
            } else if (signal.contains("HR")) {
                HR.doClick();
            } else {
                VO2.doClick();
            }
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
                    title.setFont(new Font("Times", Font.BOLD, font_size_labels));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("FRACTION OF INSPIRED OXYGEN (FIO2)");
                    contFIO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes(nameSignals.get(0), minValueSignals.get(0), maxValueSignals.get(0), unitsSignals.get(0), numDivisionSignals.get(0), Color.BLACK, signalFile);
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
                    title.setFont(new Font("Times", Font.BOLD, font_size_labels));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("FRACTION OF EXPIRED OXYGEN (FEO2)");
                    contFEO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes(nameSignals.get(1), minValueSignals.get(1), maxValueSignals.get(1), unitsSignals.get(1), numDivisionSignals.get(1), Color.GRAY, signalFile);
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
                    title.setFont(new Font("Times", Font.BOLD, font_size_labels));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("FRACTION OF EXPIRED CARBON DIOXIDE (FECO2)");
                    contFECO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes(nameSignals.get(2), minValueSignals.get(2), maxValueSignals.get(2), unitsSignals.get(2), numDivisionSignals.get(2), Color.YELLOW, signalFile);
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
                    title.setFont(new Font("Times", Font.BOLD, font_size_labels));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("FRACTION OF END-TIDAL CARBON DIOXIDE (FETCO2)");
                    contFETCO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes(nameSignals.get(3), minValueSignals.get(3), maxValueSignals.get(3), unitsSignals.get(3), numDivisionSignals.get(3), Color.PINK, signalFile);
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
                    title.setFont(new Font("Times", Font.BOLD, font_size_labels));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("FRACTION OF END-TIDAL OXYGEN (FETO2)");
                    contFETO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes(nameSignals.get(4), minValueSignals.get(4), maxValueSignals.get(4), unitsSignals.get(4), numDivisionSignals.get(4), Color.ORANGE, signalFile);
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
                    title.setFont(new Font("Times", Font.BOLD, font_size_labels));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("VENTILATION (VE)");
                    contVE.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes(nameSignals.get(5), minValueSignals.get(5), maxValueSignals.get(5), unitsSignals.get(5), numDivisionSignals.get(5), Color.MAGENTA, signalFile);
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
                    title.setFont(new Font("Times", Font.BOLD, font_size_labels));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("INSPIRATORY TIME (IT)");
                    contTI.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes(nameSignals.get(6), minValueSignals.get(6), maxValueSignals.get(6), unitsSignals.get(6), numDivisionSignals.get(6), Color.GRAY, signalFile);
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
                    title.setFont(new Font("Times", Font.BOLD, font_size_labels));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("EXPIRATORY TIME (ET)");
                    contTE.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes(nameSignals.get(7), minValueSignals.get(7), maxValueSignals.get(7), unitsSignals.get(7), numDivisionSignals.get(7), Color.CYAN, signalFile);
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
                    title.setFont(new Font("Times", Font.BOLD, font_size_labels));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("HEART RATE (HR)");
                    contHR.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes(nameSignals.get(8), minValueSignals.get(8), maxValueSignals.get(8), unitsSignals.get(8), numDivisionSignals.get(8), Color.RED, signalFile);
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
                    title.setFont(new Font("Times", Font.BOLD, font_size_labels));
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("OXYGEN CONSUMPTION (VO2)");
                    contVO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes(nameSignals.get(9), minValueSignals.get(9), maxValueSignals.get(9), unitsSignals.get(9), numDivisionSignals.get(9), Color.BLUE, signalFile);
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

        FIO2 = new JCheckBox("FRACTION OF INSPIRED OXYGEN (FIO2)");
        FIO2.setFont(new Font("Times", Font.PLAIN, font_size_labels));
        FIO2.addItemListener(this);
        FEO2 = new JCheckBox("FRACTION OF EXPIRED OXYGEN (FEO2)");
        FEO2.setFont(new Font("Times", Font.PLAIN, font_size_labels));
        FEO2.addItemListener(this);
        FECO2 = new JCheckBox("FRACTION OF EXPIRED CARBON DIOXIDE (FECO2)");
        FECO2.setFont(new Font("Times", Font.PLAIN, font_size_labels));
        FECO2.addItemListener(this);
        FETCO2 = new JCheckBox("FRACTION OF END-TIDAL CARBON DIOXIDE (FETCO2)");
        FETCO2.setFont(new Font("Times", Font.PLAIN, font_size_labels));
        FETCO2.addItemListener(this);
        FETO2 = new JCheckBox("FRACTION OF END-TIDAL OXYGEN (FETO2)");
        FETO2.setFont(new Font("Times", Font.PLAIN, font_size_labels));
        FETO2.addItemListener(this);
        VE = new JCheckBox("VENTILATION (VE)");
        VE.setFont(new Font("Times", Font.PLAIN, font_size_labels));
        VE.addItemListener(this);
        TI = new JCheckBox("INSPIRATORY TIME (IT)");
        TI.setFont(new Font("Times", Font.PLAIN, font_size_labels));
        TI.addItemListener(this);
        TE = new JCheckBox("EXPIRATORY TIME (ET)");
        TE.setFont(new Font("Times", Font.PLAIN, font_size_labels));
        TE.addItemListener(this);
        HR = new JCheckBox("HEART RATE (HR)");
        HR.setFont(new Font("Times", Font.PLAIN, font_size_labels));
        HR.addItemListener(this);
        VO2 = new JCheckBox("OXYGEN CONSUMPTION (VO2)");
        VO2.setFont(new Font("Times", Font.PLAIN, font_size_labels));
        VO2.addItemListener(this);

        containerChecks = new JPanel();
        containerChecks.setLayout(new GridLayout(5, 2));
        containerChecks.add(HR);
        containerChecks.add(VO2);
        containerChecks.add(FIO2);
        containerChecks.add(FEO2);
        containerChecks.add(FECO2);
        containerChecks.add(FETCO2);
        containerChecks.add(FETO2);
        containerChecks.add(VE);
        containerChecks.add(TI);
        containerChecks.add(TE);

    }

    /**
     * Method that allows the visualization of the frame with the different
     * options and the selected plots.
     */
    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setTitle("Medical Signal Representation");
        setVisible(true);
    }
}
