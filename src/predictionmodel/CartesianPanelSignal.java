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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
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
    public static final int X_AXIS_FIRST_X_COORD = 50;
    public int X_AXIS_SECOND_X_COORD = 400;
    public int X_AXIS_Y_COORD = 200; //It was 400

    // y-axis coord constants
    public static final int Y_AXIS_DATA_Y_COORD = 15;
    public static final int Y_AXIS_FIRST_Y_COORD = 30;
    public int Y_AXIS_SECOND_Y_COORD = 200; //It was 400
    public static final int Y_AXIS_X_COORD = 50;
    public static final int Y_AXIS_HEIGH_COORD = 25;

    //Arrows of axis are represented with "hipotenuse" of triangle
    //Definition of length of cathetas of the triangle
    public static final int FIRST_LENGHT = 10;
    public static final int SECOND_LENGHT = 5;

    //Size of start coordinate lenght
    public static final int ORIGIN_COORDINATE_LENGHT = 6;
    public static final int point_lenght = 3;

    //Distance of coordinate strings from axis
    public static final int AXIS_STRING_DISTANCE = 30;

    private ArrayList<Point2D.Double> listPoints;
    private String dataY;
    private String fileName;
    private double counter;
    private final int xCoordNumbers = 10;
    private final int yCoordNumbers = 5; //It was 10
    private int yMinimum;
    private int yMaximum;
    private int xLength;
    private int column;
    private int fontSize;
    private Color titleColor;
    private long timeStamp;
    private File file;

    /**
     * Method that allows the initialization of the specific data for each
     * signal.
     *
     * @param data Title of the signal.
     * @param minimum Minimum value the signal can take.
     * @param maximum Maximum value the signal can take.
     * @param color Specified color for displaying the signal.
     * @param signalFile Name of the file that contain the heart rate and oxygen
     * consumption measured values.
     */
    public void setAxes(String data, int minimum, int maximum, Color color, String signalFile) {
        fontSize = 15;
        xLength = (X_AXIS_SECOND_X_COORD - X_AXIS_FIRST_X_COORD)
                / xCoordNumbers;
        counter = 0;
        listPoints = new ArrayList<>();
        Point2D.Double origin = new Point2D.Double(counter, 0);
        listPoints.add(origin);
        counter++;
        dataY = data;
        yMinimum = minimum;
        yMaximum = maximum;
        titleColor = color;
        fileName = signalFile;
        file = new File(fileName);
        timeStamp = file.lastModified();
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
        counter = 0;
        Point2D.Double origin = new Point2D.Double(counter, 0);
        listPoints.add(origin);
        counter++;
        int counterRow = 0;
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
            //update each second
            while ((line = br.readLine()) != null && counterRow<=CartesianFrameSignal.counterStep) {
             String[] values = line.split(cvsSplitBy);
             Double yValue;
             yValue = Double.valueOf(values[column]);
             Point2D.Double point = new Point2D.Double(counter, yValue);
             listPoints.add(point);
             counter++;
             counterRow++;
             }
            //update each step
            /*while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                Double yValue;
                Double xValue;
                xValue = Double.valueOf(values[0]);
                yValue = Double.valueOf(values[column]);
                if (xValue <= 120 * (CartesianFrameSignal.counterStep)) { //120 because the workload increases each 2 minuts (60*2)
                    Point2D.Double point = new Point2D.Double(counter, yValue);
                    listPoints.add(point);
                    counter++;
                }
            }*/

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
        for (int i = 0; i < listPoints.size(); i++) {
            System.out.println("x: " + listPoints.get(i).x + " y: " + listPoints.get(i).y);
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

        // x-axis
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

        Font font1 = new Font("Times", Font.BOLD, fontSize);
        g2.setFont(font1);

        // x-axis arrow
        g2.drawLine(X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength) - FIRST_LENGHT,
                X_AXIS_Y_COORD - SECOND_LENGHT,
                X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength), X_AXIS_Y_COORD);
        g2.drawLine(X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength) - FIRST_LENGHT,
                X_AXIS_Y_COORD + SECOND_LENGHT,
                X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength), X_AXIS_Y_COORD);

        // draw text "X" and draw text "Y"
        g2.drawString("X", X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength),
                X_AXIS_Y_COORD + AXIS_STRING_DISTANCE);
        if (dataY.contains("FIO2")) {
            g2.drawString(dataY + " (%)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_DATA_Y_COORD);
        } else if (dataY.contains("FEO2")) {
            g2.drawString(dataY + " (%)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_DATA_Y_COORD);
        } else if (dataY.contains("FECO2")) {
            g2.drawString(dataY + " (%)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_DATA_Y_COORD);
        } else if (dataY.contains("FETCO2")) {
            g2.drawString(dataY + " (%)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_DATA_Y_COORD);
        } else if (dataY.contains("FETO2")) {
            g2.drawString(dataY + " (%)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_DATA_Y_COORD);
        } else if (dataY.contains("VE")) {
            g2.drawString(dataY + " (l/min)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_DATA_Y_COORD);
        } else if (dataY.contains("TI")) {
            g2.drawString(dataY + " (sec)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_DATA_Y_COORD);
        } else if (dataY.contains("TE")) {
            g2.drawString(dataY + " (sec)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_DATA_Y_COORD);
        } else if (dataY.contains("HR")) {
            g2.drawString(dataY + " (bpm)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_DATA_Y_COORD);
        } else {
            g2.drawString(dataY + " (l/min)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_DATA_Y_COORD);
        }
        g2.drawString("(0, 0)", X_AXIS_FIRST_X_COORD - AXIS_STRING_DISTANCE,
                Y_AXIS_SECOND_Y_COORD + AXIS_STRING_DISTANCE);

        Font font2 = new Font("Times", Font.PLAIN, fontSize);
        g2.setFont(font2);

        // draw x-axis numbers
        for (int i = 1; i < listPoints.size(); i++) {
            g2.drawLine(X_AXIS_FIRST_X_COORD + (i * xLength),
                    X_AXIS_Y_COORD - SECOND_LENGHT,
                    X_AXIS_FIRST_X_COORD + (i * xLength),
                    X_AXIS_Y_COORD + SECOND_LENGHT);
            g2.drawLine(X_AXIS_FIRST_X_COORD + ((i) * xLength), X_AXIS_Y_COORD,
                    X_AXIS_FIRST_X_COORD + ((i - 1) * xLength), X_AXIS_Y_COORD);
        }
        g2.drawLine(X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength), X_AXIS_Y_COORD,
                X_AXIS_FIRST_X_COORD + ((listPoints.size() - 1) * xLength), X_AXIS_Y_COORD);
        for (int i = 1; i < listPoints.size(); i++) {
            g2.drawString(Integer.toString(i),
                    X_AXIS_FIRST_X_COORD + (i * xLength) - 3,
                    X_AXIS_Y_COORD + AXIS_STRING_DISTANCE);
        }
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
                        Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength));
            }
            String result = String.format("%.0f", yCoordNumbers * division);
            g2.drawString(result,
                    Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
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
                        Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength));
            }
            g2.drawString(Integer.toString(yMaximum),
                    Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                    Y_AXIS_SECOND_Y_COORD - (yMaximum * yLength));
        }
        Stroke stroke = new BasicStroke(2f);
        float[] dashingPattern1 = {2f, 2f};
        Stroke stroke1 = new BasicStroke(0.5f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
        for (int i = 0; i < listPoints.size() - 1; i++) {
            g2.setStroke(stroke);
            g2.setColor(titleColor);
            Point2D.Double initial;
            Point2D.Double end;
            Point2D.Double initialVertical;
            Point2D.Double endVertical;

            if (yMaximum > yCoordNumbers) {
                initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(i).y * yCoordNumbers / yMaximum) * yLength));
                end = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i + 1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(i + 1).y * yCoordNumbers / yMaximum) * yLength));
                initialVertical = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD);
                endVertical = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(i).y * yCoordNumbers / yMaximum) * yLength));

                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(i).y * yCoordNumbers / yMaximum) * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
            } else {
                initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(i).y * yLength));
                end = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i + 1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(i + 1).y * yLength));
                initialVertical = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD);
                endVertical = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(i).y * yLength));

                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(i).y * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
            }

            g2.draw(new Line2D.Double(initial, end));
            g2.setColor(Color.BLACK);
            g2.setStroke(stroke1);
            g2.draw(new Line2D.Double(initialVertical, endVertical));
        }
        Point2D.Double initialVertical;
        Point2D.Double endVertical;
        if (yMaximum > yCoordNumbers) {
            initialVertical = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(listPoints.size() - 1).x * xLength),
                    Y_AXIS_SECOND_Y_COORD);
            endVertical = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(listPoints.size() - 1).x * xLength),
                    Y_AXIS_SECOND_Y_COORD - ((listPoints.get(listPoints.size() - 1).y * yCoordNumbers / yMaximum) * yLength));
            /*initialHorizontal = new Point2D.Double(X_AXIS_FIRST_X_COORD,
             Y_AXIS_SECOND_Y_COORD - ((listPoints.get(listPoints.size()-1).y*yCoordNumbers/yMaximum) * yLength));
             endHorizontal = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(listPoints.size()-1).x * xLength),
             Y_AXIS_SECOND_Y_COORD - ((listPoints.get(listPoints.size()-1).y*yCoordNumbers/yMaximum) * yLength));*/
        } else {
            initialVertical = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(listPoints.size() - 1).x * xLength),
                    Y_AXIS_SECOND_Y_COORD);
            endVertical = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(listPoints.size() - 1).x * xLength),
                    Y_AXIS_SECOND_Y_COORD - (listPoints.get(listPoints.size() - 1).y * yLength));
        }
        g2.draw(new Line2D.Double(initialVertical, endVertical));
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
            /*long newTimeStamp = file.lastModified();
             if (timeStamp != newTimeStamp) {
             timeStamp = newTimeStamp;
             readFile();
             revalidate();
             repaint();
             }*/
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
        //return new Dimension((X_AXIS_FIRST_X_COORD * 2) + (listPoints.size() * xLength), Y_AXIS_SECOND_Y_COORD + Y_AXIS_X_COORD);
        return new Dimension((X_AXIS_FIRST_X_COORD * 2) + (listPoints.size() * xLength), Y_AXIS_SECOND_Y_COORD );
    }
}
