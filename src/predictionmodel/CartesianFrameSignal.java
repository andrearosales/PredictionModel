/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package predictionmodel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
class CartesianFrameSignal extends JFrame implements ItemListener, ActionListener {

    public static int counterStep;

    private ArrayList<String> signals;
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
    private final Timer timer = new Timer(1000, this);

    /**
     * Default constructor of the class that will initialize the main panels.
     *
     * @param signalFile Name of the file that contain the heart rate and oxygen
     * consumption measured values.
     */
    CartesianFrameSignal(String signalFile) {
        counterStep = 0;
        this.signalFile = signalFile;
        setSize(1000, 800);
        initializeOptions();
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        //Adding the general title
        JLabel labelTitle = new JLabel();
        labelTitle.setText("MEDICAL PHYSIOLOGICAL SIGNALS");
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelTitle);

        //Adding the greater container
        bigPanel = new BigPanel(labelTitle.getHeight());
        bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));

        JScrollPane bigScrollPanel = new JScrollPane(bigPanel);
        add(bigScrollPanel);
        add(containerChecks);
        timer.start();
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
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Fraction of inspired oxygen (FIO2)");
                    contFIO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes("FIO2", 0, 22, Color.BLACK, signalFile);
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
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Fraction of expired oxygen (FEO2)");
                    contFEO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes("FEO2", 0, 22, Color.LIGHT_GRAY, signalFile);
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
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Fraction of expired carbon dioxide (FECO2)");
                    contFECO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes("FECO2", 0, 8, Color.YELLOW, signalFile);
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
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Fraction of end-tidal carbon dioxide (FETCO2)");
                    contFETCO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes("FETCO2", 0, 10, Color.PINK, signalFile);
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
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Fraction of end-tidal oxygen (FETO2)");
                    contFETO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes("FETO2", 0, 21, Color.ORANGE, signalFile);
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
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Ventilation (VE)");
                    contVE.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes("VE", 0, 234, Color.MAGENTA, signalFile);
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
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Inspiratory time (IT)");
                    contTI.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes("TI", 0, 6, Color.GRAY, signalFile);
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
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Expiratory time (ET)");
                    contTE.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes("TE", 0, 12, Color.CYAN, signalFile);
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
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Heart rate (HR)");
                    contHR.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes("HR", 33, 215, Color.RED, signalFile);
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
                    title.setAlignmentX(Component.CENTER_ALIGNMENT);
                    title.setText("Oxygen consumption (VO2)");
                    contVO2.add(title);

                    CartesianPanelSignal panel = new CartesianPanelSignal();
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setAxes("VO2", 0, 6411, Color.BLUE, signalFile);
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
        FIO2.addItemListener(this);
        FEO2 = new JCheckBox("Fraction of expired oxygen (FEO2)");
        FEO2.addItemListener(this);
        FECO2 = new JCheckBox("Fraction of expired carbon dioxide (FECO2)");
        FECO2.addItemListener(this);
        FETCO2 = new JCheckBox("Fraction of end-tidal carbon dioxide (FETCO2)");
        FETCO2.addItemListener(this);
        FETO2 = new JCheckBox("Fraction of end-tidal oxygen (FETO2)");
        FETO2.addItemListener(this);
        VE = new JCheckBox("Ventilation (VE)");
        VE.addItemListener(this);
        TI = new JCheckBox("Inspiratory time (IT)");
        TI.addItemListener(this);
        TE = new JCheckBox("Expiratory time (ET)");
        TE.addItemListener(this);
        HR = new JCheckBox("Heart rate (HR)");
        HR.addItemListener(this);
        VO2 = new JCheckBox("Oxygen consumption (VO2)");
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
     * Method that is triggered each s seconds in order to emulate the monitored
     * physiological signals.
     *
     * @param ae Action event that triggered the method.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == timer) {
            counterStep ++;
            timer.start();
        }
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
