/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package predictionmodel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Class that manages each physiological signal.
 *
 * @author aRosales
 */
class CartesianPanelSignal extends JPanel implements ActionListener {

    private final Timer timer = new Timer(500, this);
    // x-axis coord constants
    public static final int X_AXIS_FIRST_X_COORD = 60; //Needs to be changed if the letter size changes
    public int X_AXIS_SECOND_X_COORD = 500; //Needs to be changed if the letter size changes
    public static int X_AXIS_Y_COORD; //Set in the configuration file

    // y-axis coord constants
    public static final int Y_AXIS_DATA_Y_COORD = 20;
    public static final int Y_AXIS_FIRST_Y_COORD = 60; //Needs to be changed if the letter size changes
    public static int Y_AXIS_SECOND_Y_COORD; //Set in the configuration file
    public static final int Y_AXIS_X_COORD = 60; //Needs to be changed if the letter size changes
    public static final int Y_AXIS_HEIGH_COORD = 25;

    //Arrows of axis are represented with "hipotenuse" of triangle
    //Definition of length of cathetas of the triangle
    public static final int FIRST_LENGHT = 10;
    public static final int SECOND_LENGHT = 5;

    //Size of start coordinate lenght
    public static final int ORIGIN_COORDINATE_LENGHT = 6;
    public static final int point_lenght = 3;

    //Distance of coordinate strings from axis
    public static final int AXIS_STRING_DISTANCE_Y = 55; //Needs to be changed if the letter size changes
    public static final int AXIS_STRING_DISTANCE_X = 30;

    // additional information for the scrolling
    public static final int SCROLL_SPACE_HORIZONTAL = 70;
    public static final int SCROLL_SPACE_VERTICAL = 40;

    public static String xAxis;
    public static int durationPrediction;

    private ArrayList<Point2D.Double> listPoints;
    private ArrayList<Double> listTimes;
    private String dataY;
    private String fileName;
    private String units;
    private double counter;
    private final int xCoordNumbers = 15;
    private final int font_size_numbers = 22;
    private final int font_size_letters = 24;
    private static int yCoordNumbers; //Set in the configuration file
    private int yMinimum;
    private int yMaximum;
    private float yMinimumFloat;
    private float yMaximumFloat;
    private int xLength;
    private int column;
    private Color titleColor;

    /**
     * Method that allows the initialization of the specific data for each
     * signal.
     *
     * @param data Title of the signal.
     * @param minimum Minimum value the signal can take.
     * @param maximum Maximum value the signal can take.
     * @param units Measurement unit of the physiological signal.
     * @param numDivisions Number of divisions along the y axis.
     * @param color Specified color for displaying the signal.
     * @param signalFile Name of the file that contain the heart rate and oxygen
     * consumption measured values.
     */
    public void setAxes(String data, int minimum, int maximum, String units, int numDivisions, Color color, String signalFile) {
        yCoordNumbers = numDivisions;
        xLength = (X_AXIS_SECOND_X_COORD - X_AXIS_FIRST_X_COORD)
                / xCoordNumbers;
        counter = 0;
        listPoints = new ArrayList<>();
        listTimes = new ArrayList<>();
        counter++;
        dataY = data;
        yMinimum = minimum;
        yMaximum = maximum;
        titleColor = color;
        fileName = signalFile;
        this.units = units;
        readFile();
    }

    public void setAxes(String data, float minimum, float maximum, String units, int numDivisions, Color color, String signalFile) {
        yCoordNumbers = numDivisions;
        xLength = (X_AXIS_SECOND_X_COORD - X_AXIS_FIRST_X_COORD)
                / xCoordNumbers;
        counter = 0;
        listPoints = new ArrayList<>();
        listTimes = new ArrayList<>();
        counter++;
        dataY = data;
        yMinimumFloat = minimum;
        yMaximumFloat = maximum;
        titleColor = color;
        fileName = signalFile;
        this.units = units;
        readFile();
    }
    
    /**
     * Method that reads the file where the points are stored.
     */
    public void readFile() {

        String csvFile = fileName;
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";
        listPoints.clear();
        listTimes.clear();
        counter = 1;
        //Point2D.Double origin = new Point2D.Double(counter, 0);
        //listPoints.add(origin);
        //listTimes.add(0.0);
        counter++;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            int count = 0;
            String[] titles = line.split(cvsSplitBy);
            for (String title : titles) {
                if (title.equals(dataY)) {
                    column = count;
                    break;
                } else {
                    count++;
                }
            }
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                Double yValue;
                Double xValue;
                xValue = Double.valueOf(values[0]);
                yValue = Double.valueOf(values[column]);
                if (xValue <= durationPrediction * (CartesianPanel.predictionStep)) { //Because the workload increases each durationPrediction
                    Point2D.Double point = new Point2D.Double(counter, yValue);
                    listPoints.add(point);
                    listTimes.add(xValue);
                    counter++;
                }
            }

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
    }

    /**
     * Method that prints the stored points so far. Used for debugging purposes.
     */
    public void printPoints() {
        System.out.println("listsize: " + listPoints.size());
        for (int i = 0; i < listPoints.size(); i++) {
            System.out.println("x: " + listPoints.get(i).x + " y: " + listPoints.get(i).y + " time: " + listTimes.get(i));
        }
    }

    /**
     * Inherited method in charge of setting the components of the image so they
     * can be visualized.
     *
     * @param g Graphic were the components will be displayed.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // y-axis
        g2.drawLine(Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD,
                Y_AXIS_X_COORD, Y_AXIS_SECOND_Y_COORD);

        // y-axis arrow
        g2.drawLine(Y_AXIS_X_COORD - SECOND_LENGHT,
                Y_AXIS_FIRST_Y_COORD + FIRST_LENGHT,
                Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD);
        g2.drawLine(Y_AXIS_X_COORD + SECOND_LENGHT,
                Y_AXIS_FIRST_Y_COORD + FIRST_LENGHT,
                Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD);

        // draw origin Point
        g2.fillOval(
                X_AXIS_FIRST_X_COORD - (ORIGIN_COORDINATE_LENGHT / 2),
                Y_AXIS_SECOND_Y_COORD - (ORIGIN_COORDINATE_LENGHT / 2),
                ORIGIN_COORDINATE_LENGHT, ORIGIN_COORDINATE_LENGHT);

        Font font1 = new Font("Times", Font.BOLD, font_size_letters);
        g2.setFont(font1);

        // x-axis arrow
        g2.drawLine(X_AXIS_FIRST_X_COORD + ((CartesianPanel.predictionStep + 1) * xLength) - FIRST_LENGHT,
                X_AXIS_Y_COORD - SECOND_LENGHT,
                X_AXIS_FIRST_X_COORD + ((CartesianPanel.predictionStep + 1) * xLength), X_AXIS_Y_COORD);
        g2.drawLine(X_AXIS_FIRST_X_COORD + ((CartesianPanel.predictionStep + 1) * xLength) - FIRST_LENGHT,
                X_AXIS_Y_COORD + SECOND_LENGHT,
                X_AXIS_FIRST_X_COORD + ((CartesianPanel.predictionStep + 1) * xLength), X_AXIS_Y_COORD);

        // draw text "X" and draw text "Y"
        g2.drawString("P-STEP", X_AXIS_FIRST_X_COORD + ((CartesianPanel.predictionStep + 1) * xLength),
                X_AXIS_Y_COORD + AXIS_STRING_DISTANCE_X);
        /*if (dataY.contains("FIO2")) {
         g2.drawString(dataY + " (%)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         } else if (dataY.contains("FEO2")) {
         g2.drawString(dataY + " (%)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         } else if (dataY.contains("FECO2")) {
         g2.drawString(dataY + " (%)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         } else if (dataY.contains("FETCO2")) {
         g2.drawString(dataY + " (%)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         } else if (dataY.contains("FETO2")) {
         g2.drawString(dataY + " (%)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         } else if (dataY.contains("VE")) {
         g2.drawString(dataY + " (l/min)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         } else if (dataY.contains("TI")) {
         g2.drawString("IT (sec)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         } else if (dataY.contains("TE")) {
         g2.drawString("ET (sec)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         } else if (dataY.contains("HR")) {
         g2.drawString(dataY + " (bpm)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         } else {
         g2.drawString(dataY + " (l/min)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         }*/
        g2.drawString(dataY + " (" + units + ")", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                Y_AXIS_DATA_Y_COORD);

        g2.drawString("(0, 0)", X_AXIS_FIRST_X_COORD - AXIS_STRING_DISTANCE_Y,
                Y_AXIS_SECOND_Y_COORD + AXIS_STRING_DISTANCE_X);

        Font font2 = new Font("Times", Font.PLAIN, font_size_numbers);
        g2.setFont(font2);

        // draw x-axis numbers
        for (int i = 1; i <= CartesianPanel.predictionStep; i++) {
            g2.drawLine(X_AXIS_FIRST_X_COORD + (i * xLength),
                    X_AXIS_Y_COORD - SECOND_LENGHT,
                    X_AXIS_FIRST_X_COORD + (i * xLength),
                    X_AXIS_Y_COORD + SECOND_LENGHT);
            g2.drawLine(X_AXIS_FIRST_X_COORD + ((i) * xLength), X_AXIS_Y_COORD,
                    X_AXIS_FIRST_X_COORD + ((i - 1) * xLength), X_AXIS_Y_COORD);
        }
        g2.drawLine(X_AXIS_FIRST_X_COORD + ((CartesianPanel.predictionStep + 1) * xLength), X_AXIS_Y_COORD,
                X_AXIS_FIRST_X_COORD + ((CartesianPanel.predictionStep) * xLength), X_AXIS_Y_COORD);
        for (int i = 1; i <= CartesianPanel.predictionStep; i++) {
            g2.drawString(Integer.toString(i),
                    X_AXIS_FIRST_X_COORD + (i * xLength) - 3,
                    X_AXIS_Y_COORD + AXIS_STRING_DISTANCE_X);
        }
        // draw y-axis numbers
        if (dataY.contains("VO2") && units.equals("l/min")) {
            int yLength;
            float division;
            if (yMaximumFloat > yCoordNumbers) {
                Float max = (float) yMaximumFloat;
                yLength = (Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD)
                        / yCoordNumbers;
                division = max / yCoordNumbers;
                //division = (float) Math.ceil(division);
                for (int i = 1; i < yCoordNumbers; i++) {
                    g2.drawLine(Y_AXIS_X_COORD - SECOND_LENGHT,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength),
                            Y_AXIS_X_COORD + SECOND_LENGHT,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength));
                    String result = String.format("%.2f", i * division);
                    g2.drawString(result,
                            Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength));
                }
                String result = String.format("%.2f", yCoordNumbers * division);
                g2.drawString(result,
                        Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                        Y_AXIS_SECOND_Y_COORD - (yCoordNumbers * yLength));
            } else {
                yLength = (Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD)
                        / (int)yMaximumFloat;
                for (int i = yMinimum + 1; i < yMaximumFloat; i++) {
                    g2.drawLine(Y_AXIS_X_COORD - SECOND_LENGHT,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength),
                            Y_AXIS_X_COORD + SECOND_LENGHT,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength));
                    g2.drawString(Integer.toString(yMinimum + i),
                            Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength));
                }
                g2.drawString(Float.toString(yMaximumFloat),
                        Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                        Y_AXIS_SECOND_Y_COORD - (yMaximumFloat * yLength));
            }

            Stroke stroke = new BasicStroke(2f);
            ArrayList<Double> listPointsDraw = new ArrayList<>();

            for (int i = 0; i < CartesianPanel.predictionStep; i++) {
                Double higher = 0.0;
                for (int j = 0; j < listTimes.size(); j++) {
                    if ((listTimes.get(j) >= (durationPrediction * i)) && (listTimes.get(j) < (durationPrediction * (i + 1)))) {
                        listPointsDraw.add(listPoints.get(j).y);
                    }
                    if (listTimes.get(j) <= (durationPrediction * (i + 1))) {
                        higher = listPoints.get(j).y;
                    }
                }
                float subDivision = xLength / listPointsDraw.size();
                for (int k = 0; k < listPointsDraw.size() - 1; k++) {
                    g2.setStroke(stroke);
                    g2.setColor(titleColor);
                    Point2D.Double initial;
                    Point2D.Double end;
                    if (yMaximumFloat > yCoordNumbers) {
                        initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + (k * subDivision),
                                Y_AXIS_SECOND_Y_COORD - ((listPointsDraw.get(k) * yCoordNumbers / yMaximumFloat) * yLength));
                        end = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + ((k + 1) * subDivision),
                                Y_AXIS_SECOND_Y_COORD - ((listPointsDraw.get(k + 1) * yCoordNumbers / yMaximumFloat) * yLength));
                    } else {
                        initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + (k * subDivision),
                                Y_AXIS_SECOND_Y_COORD - (listPointsDraw.get(k) * yLength));
                        end = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + ((k + 1) * subDivision),
                                Y_AXIS_SECOND_Y_COORD - (listPointsDraw.get(k + 1) * yLength));
                    }
                    g2.draw(new Line2D.Double(initial, end));
                }

                Point2D.Double initial;
                Point2D.Double end;

                if (yMaximumFloat > yCoordNumbers) {
                    initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + ((listPointsDraw.size() - 1) * subDivision),
                            Y_AXIS_SECOND_Y_COORD - ((listPointsDraw.get(listPointsDraw.size() - 1) * yCoordNumbers / yMaximumFloat) * yLength));
                    end = new Point2D.Double(X_AXIS_FIRST_X_COORD + ((i + 1) * xLength),
                            Y_AXIS_SECOND_Y_COORD - ((higher * yCoordNumbers / yMaximumFloat) * yLength));
                } else {
                    initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + ((listPointsDraw.size() - 1) * subDivision),
                            Y_AXIS_SECOND_Y_COORD - (listPointsDraw.get(listPointsDraw.size() - 1) * yLength));
                    end = new Point2D.Double(X_AXIS_FIRST_X_COORD + ((i + 1) * xLength),
                            Y_AXIS_SECOND_Y_COORD - (higher * yLength));
                }

                g2.draw(new Line2D.Double(initial, end));
                listPointsDraw.clear();
            }
        } else {
            int yLength;
            float division;
            if (yMaximum > yCoordNumbers) {
                Float max = (float) yMaximum;
                yLength = (int) Math.ceil((Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD)
                        / yCoordNumbers);
                division = max / yCoordNumbers;
                division = (float) Math.ceil(division);
                for (int i = 1; i < yCoordNumbers; i++) {
                    g2.drawLine(Y_AXIS_X_COORD - SECOND_LENGHT,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength),
                            Y_AXIS_X_COORD + SECOND_LENGHT,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength));
                    String result = String.format("%.0f", i * division);
                    g2.drawString(result,
                            Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength));
                }
                String result = String.format("%.0f", yCoordNumbers * division);
                g2.drawString(result,
                        Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                        Y_AXIS_SECOND_Y_COORD - (yCoordNumbers * yLength));
            } else {
                yLength = (Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD)
                        / yMaximum;
                for (int i = yMinimum + 1; i < yMaximum; i++) {
                    g2.drawLine(Y_AXIS_X_COORD - SECOND_LENGHT,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength),
                            Y_AXIS_X_COORD + SECOND_LENGHT,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength));
                    g2.drawString(Integer.toString(yMinimum + i),
                            Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                            Y_AXIS_SECOND_Y_COORD - (i * yLength));
                }
                g2.drawString(Integer.toString(yMaximum),
                        Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                        Y_AXIS_SECOND_Y_COORD - (yMaximum * yLength));
            }

            Stroke stroke = new BasicStroke(2f);
            ArrayList<Double> listPointsDraw = new ArrayList<>();

            for (int i = 0; i < CartesianPanel.predictionStep; i++) {
                Double higher = 0.0;
                for (int j = 0; j < listTimes.size(); j++) {
                    if ((listTimes.get(j) >= (durationPrediction * i)) && (listTimes.get(j) < (durationPrediction * (i + 1)))) {
                        listPointsDraw.add(listPoints.get(j).y);
                    }
                    if (listTimes.get(j) <= (durationPrediction * (i + 1))) {
                        higher = listPoints.get(j).y;
                    }
                }
                float subDivision = xLength / listPointsDraw.size();
                for (int k = 0; k < listPointsDraw.size() - 1; k++) {
                    g2.setStroke(stroke);
                    g2.setColor(titleColor);
                    Point2D.Double initial;
                    Point2D.Double end;
                    if (yMaximum > yCoordNumbers) {
                        initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + (k * subDivision),
                                Y_AXIS_SECOND_Y_COORD - ((listPointsDraw.get(k) * yCoordNumbers / yMaximum) * yLength));
                        end = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + ((k + 1) * subDivision),
                                Y_AXIS_SECOND_Y_COORD - ((listPointsDraw.get(k + 1) * yCoordNumbers / yMaximum) * yLength));
                    } else {
                        initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + (k * subDivision),
                                Y_AXIS_SECOND_Y_COORD - (listPointsDraw.get(k) * yLength));
                        end = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + ((k + 1) * subDivision),
                                Y_AXIS_SECOND_Y_COORD - (listPointsDraw.get(k + 1) * yLength));
                    }
                    g2.draw(new Line2D.Double(initial, end));
                }

                Point2D.Double initial;
                Point2D.Double end;

                if (yMaximum > yCoordNumbers) {
                    initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + ((listPointsDraw.size() - 1) * subDivision),
                            Y_AXIS_SECOND_Y_COORD - ((listPointsDraw.get(listPointsDraw.size() - 1) * yCoordNumbers / yMaximum) * yLength));
                    end = new Point2D.Double(X_AXIS_FIRST_X_COORD + ((i + 1) * xLength),
                            Y_AXIS_SECOND_Y_COORD - ((higher * yCoordNumbers / yMaximum) * yLength));
                } else {
                    initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength) + ((listPointsDraw.size() - 1) * subDivision),
                            Y_AXIS_SECOND_Y_COORD - (listPointsDraw.get(listPointsDraw.size() - 1) * yLength));
                    end = new Point2D.Double(X_AXIS_FIRST_X_COORD + ((i + 1) * xLength),
                            Y_AXIS_SECOND_Y_COORD - (higher * yLength));
                }

                g2.draw(new Line2D.Double(initial, end));
                listPointsDraw.clear();
            }
        }
        timer.start();
        revalidate();
    }

    /**
     * Method that is triggered each s seconds in order to analize if more data
     * points were added.
     *
     * @param ae Action event that triggered the method.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == timer) {
            readFile();
            revalidate();
            repaint();
        }
    }

    /**
     * Method that helps the graphic visualization.
     *
     * @return Whole dimension of the panel.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension((X_AXIS_FIRST_X_COORD * 2) + (CartesianPanel.predictionStep * xLength) + SCROLL_SPACE_HORIZONTAL, Y_AXIS_SECOND_Y_COORD + SCROLL_SPACE_VERTICAL);
    }
}
