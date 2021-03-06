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
 * Class that individually manages the graphic.
 *
 * @author aRosales
 */
class CartesianPanel extends JPanel implements ActionListener {

    private final Timer timer = new Timer(500, this);
    // x-axis coord constants
    public static final int X_AXIS_FIRST_X_COORD = 60; //Needs to be changed if the letter size changes
    public int X_AXIS_SECOND_X_COORD = 500; //Needs to be changed if the letter size changes
    public int X_AXIS_Y_COORD = 500; //Needs to be changed if the letter size changes

    // y-axis coord constants
    public static final int Y_AXIS_DATA_Y_COORD = 20;
    public static final int Y_AXIS_FIRST_Y_COORD = 60; //Needs to be changed if the letter size changes
    public int Y_AXIS_SECOND_Y_COORD = 500; //Needs to be changed if the letter size changes
    public static final int Y_AXIS_X_COORD = 60; //Needs to be changed if the letter size changes

    //Arrows of axis are represented with "hipotenuse" of triangle
    //Definition of length of cathetas of the triangle
    public static final int FIRST_LENGHT = 10;
    public static final int SECOND_LENGHT = 5;

    // size of start coordinate lenght
    public static final int ORIGIN_COORDINATE_LENGHT = 6;
    public static final int point_lenght = 3;

    // distance of coordinate strings from axis
    public static final int AXIS_STRING_DISTANCE_Y = 55; //Needs to be changed if the letter size changes
    public static final int AXIS_STRING_DISTANCE_X = 30;

    // additional information for the scrolling
    public static final int SCROLL_SPACE_HORIZONTAL = 70;
    public static final int SCROLL_SPACE_VERTICAL = 40;

    public static int predictionStep;

    private ArrayList<Point2D.Double> listPoints;
    private ArrayList<Point2D.Double> listErrorPlus;
    private ArrayList<Point2D.Double> listErrorMinus;
    private double counter;
    private double predictionError;
    private final int xCoordNumbers = 15;
    private final int yCoordNumbers = 10;
    private final int font_size_numbers = 22;
    private final int font_size_letters = 24;
    private int yMinimum;
    private int yMaximum;
    private int columnAVG;
    private int xLength;
    private Color titleColor;
    private long timeStamp;
    private File file;
    private String predictedSignal;
    private String units;

    /**
     * Method that allows the initialization of the initial thata for the
     * graphic.
     *
     * @param color Specified color for displaying the signal.
     * @param signal Predicted signal.
     * @param units Measurement unit of the prediction results.
     */
    public void setAxes(Color color, String signal, String units) {
        xLength = (X_AXIS_SECOND_X_COORD - X_AXIS_FIRST_X_COORD)
                / xCoordNumbers;
        counter = 0;
        predictedSignal = signal;
        listPoints = new ArrayList<>();
        listErrorMinus = new ArrayList<>();
        listErrorPlus = new ArrayList<>();
        counter++;
        titleColor = color;
        file = new File("MAE_result.csv");
        timeStamp = file.lastModified();
        predictionStep = listPoints.size();
        this.units = units;
        readFile();
    }

    /**
     * Method that reads the file where the points are stored.
     */
    public void readFile() {

        listPoints.clear();
        listErrorPlus.clear();
        listErrorMinus.clear();
        counter = 1;

        String csvFile = "MAE_result.csv";
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            columnAVG = 2;
            Double min = 0.0;
            Double max = 0.0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                Double yValue;
                Double valuePlus;
                Double valueMinus;
                yValue = Double.valueOf(values[columnAVG]);
                valuePlus = Double.valueOf(values[columnAVG + 1]);
                if (valuePlus > max) {
                    max = valuePlus;
                }
                valueMinus = Double.valueOf(values[columnAVG + 2]);
                predictionError = Double.valueOf(values[1]);
                if (valueMinus < min) {
                    min = valueMinus;
                }
                Point2D.Double point = new Point2D.Double(counter, yValue);
                listPoints.add(point);
                Point2D.Double pointPlus = new Point2D.Double(counter, valuePlus);
                listErrorPlus.add(pointPlus);
                Point2D.Double pointMinus = new Point2D.Double(counter, valueMinus);
                listErrorMinus.add(pointMinus);
                counter++;
            }
            yMinimum = (int) Math.ceil(min);
            yMaximum = (int) Math.ceil(max);
            predictionStep = listPoints.size();

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
        for (Point2D.Double listPoint : listPoints) {
            System.out.println("x: " + listPoint.x + " y: " + listPoint.y);
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
        g2.drawLine(X_AXIS_FIRST_X_COORD + ((listPoints.size() + 1) * xLength) - FIRST_LENGHT,
                X_AXIS_Y_COORD - SECOND_LENGHT,
                X_AXIS_FIRST_X_COORD + ((listPoints.size() + 1) * xLength), X_AXIS_Y_COORD);
        g2.drawLine(X_AXIS_FIRST_X_COORD + ((listPoints.size() + 1) * xLength) - FIRST_LENGHT,
                X_AXIS_Y_COORD + SECOND_LENGHT,
                X_AXIS_FIRST_X_COORD + ((listPoints.size() + 1) * xLength), X_AXIS_Y_COORD);

        // draw text "X" and draw text "Y"
        g2.drawString("P-STEP", X_AXIS_FIRST_X_COORD + ((listPoints.size() + 1) * xLength),
                X_AXIS_Y_COORD + AXIS_STRING_DISTANCE_X);
        /*if (predictedSignal.contains("HR")) {
         g2.drawString(predictedSignal + " (bpm)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         } else {
         g2.drawString(predictedSignal + " (l/min)", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
         Y_AXIS_DATA_Y_COORD);
         }*/
        g2.drawString(predictedSignal + " (" + units + ")", Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                Y_AXIS_DATA_Y_COORD);
        g2.drawString("(0, 0)", X_AXIS_FIRST_X_COORD - AXIS_STRING_DISTANCE_Y,
                Y_AXIS_SECOND_Y_COORD + AXIS_STRING_DISTANCE_X);

        Font font2 = new Font("Times", Font.PLAIN, font_size_numbers);
        g2.setFont(font2);

        // draw x-axis numbers
        for (int i = 1; i <= listPoints.size() + 1; i++) {
            if (i < listPoints.size() + 1) {
                g2.drawLine(X_AXIS_FIRST_X_COORD + (i * xLength),
                        X_AXIS_Y_COORD - SECOND_LENGHT,
                        X_AXIS_FIRST_X_COORD + (i * xLength),
                        X_AXIS_Y_COORD + SECOND_LENGHT);
            }
            g2.drawLine(X_AXIS_FIRST_X_COORD + ((i) * xLength), X_AXIS_Y_COORD,
                    X_AXIS_FIRST_X_COORD + ((i - 1) * xLength), X_AXIS_Y_COORD);
        }
        g2.drawLine(X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength), X_AXIS_Y_COORD,
                X_AXIS_FIRST_X_COORD + ((listPoints.size() - 1) * xLength), X_AXIS_Y_COORD);
        for (int i = 1; i < listPoints.size() + 1; i++) {
            g2.drawString(Integer.toString(i),
                    X_AXIS_FIRST_X_COORD + (i * xLength) - 3,
                    X_AXIS_Y_COORD + AXIS_STRING_DISTANCE_X);
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
                g2.drawString(Integer.toString(yMinimum + (int) Math.ceil(predictionError) + i),
                        Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength));
            }
            g2.drawString(Integer.toString(yMaximum),
                    Y_AXIS_X_COORD - AXIS_STRING_DISTANCE_Y,
                    Y_AXIS_SECOND_Y_COORD - (yMaximum * yLength));
        }

        Stroke stroke = new BasicStroke(4f);
        float[] dashingPattern1 = {3f};
        Stroke stroke1 = new BasicStroke(4f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dashingPattern1, 0.0f);
        float[] dashingPattern2 = {15f};
        Stroke stroke2 = new BasicStroke(4f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dashingPattern2, 0.0f);
        if (listPoints.size() == 1) {
            g2.setStroke(stroke);
            g2.setColor(titleColor);
            if (yMaximum > yCoordNumbers) {
                g2.setColor(Color.BLUE);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(0).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorPlus.get(0).y * yCoordNumbers / yMaximum) * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
                g2.setColor(titleColor);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(0).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(0).y * yCoordNumbers / yMaximum) * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
                g2.setColor(Color.GRAY);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(0).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorMinus.get(0).y * yCoordNumbers / yMaximum) * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
            } else {
                g2.setColor(Color.BLUE);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(0).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - (listErrorPlus.get(0).y * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
                g2.setColor(titleColor);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(0).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(0).y * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
                g2.setColor(Color.GRAY);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(0).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - (listErrorMinus.get(0).y * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
            }
        }
        for (int i = 0; i < listPoints.size() - 1; i++) {
            g2.setStroke(stroke);
            g2.setColor(titleColor);
            Point2D.Double initial;
            Point2D.Double end;
            Point2D.Double initialPlus;
            Point2D.Double endPlus;
            Point2D.Double initialMinus;
            Point2D.Double endMinus;

            if (yMaximum > yCoordNumbers) {
                initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(i).y * yCoordNumbers / yMaximum) * yLength));
                initialPlus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorPlus.get(i).y * yCoordNumbers / yMaximum) * yLength));
                initialMinus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorMinus.get(i).y * yCoordNumbers / yMaximum) * yLength));
                end = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i + 1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(i + 1).y * yCoordNumbers / yMaximum) * yLength));
                endPlus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(i + 1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorPlus.get(i + 1).y * yCoordNumbers / yMaximum) * yLength));
                endMinus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(i + 1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorMinus.get(i + 1).y * yCoordNumbers / yMaximum) * yLength));

                g2.setColor(Color.BLUE);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(i).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorPlus.get(i).y * yCoordNumbers / yMaximum) * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
                g2.setColor(titleColor);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(i).y * yCoordNumbers / yMaximum) * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
                g2.setColor(Color.GRAY);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(i).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorMinus.get(i).y * yCoordNumbers / yMaximum) * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
            } else {
                initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(i).y * yLength));
                initialPlus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listErrorPlus.get(i).y * yLength));
                initialMinus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listErrorMinus.get(i).y * yLength));
                end = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i + 1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(i + 1).y * yLength));
                endPlus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(i + 1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listErrorPlus.get(i + 1).y * yLength));
                endMinus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(i + 1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listErrorMinus.get(i + 1).y * yLength));

                g2.setColor(Color.BLUE);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(i).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - (listErrorPlus.get(i).y * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
                g2.setColor(titleColor);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(i).y * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
                g2.setColor(Color.GRAY);
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(i).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - (listErrorMinus.get(i).y * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
            }
            g2.setColor(titleColor);
            g2.draw(new Line2D.Double(initial, end));
            g2.setColor(Color.BLUE);
            g2.setStroke(stroke2);
            g2.draw(new Line2D.Double(initialPlus, endPlus));
            g2.setColor(Color.GRAY);
            g2.setStroke(stroke1);
            g2.draw(new Line2D.Double(initialMinus, endMinus));

        }
        g2.setStroke(stroke);
        g2.setColor(titleColor);
        if (yMaximum > yCoordNumbers) {
            g2.setColor(Color.BLUE);
            g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(listErrorPlus.size() - 1).x * xLength) - (point_lenght / 2),
                    Y_AXIS_SECOND_Y_COORD - ((listErrorPlus.get(listErrorPlus.size() - 1).y * yCoordNumbers / yMaximum) * yLength) - (point_lenght / 2),
                    point_lenght, point_lenght));
            g2.setColor(titleColor);
            g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(listPoints.size() - 1).x * xLength) - (point_lenght / 2),
                    Y_AXIS_SECOND_Y_COORD - ((listPoints.get(listPoints.size() - 1).y * yCoordNumbers / yMaximum) * yLength) - (point_lenght / 2),
                    point_lenght, point_lenght));
            g2.setColor(Color.GRAY);
            g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(listErrorMinus.size() - 1).x * xLength) - (point_lenght / 2),
                    Y_AXIS_SECOND_Y_COORD - ((listErrorMinus.get(listErrorMinus.size() - 1).y * yCoordNumbers / yMaximum) * yLength) - (point_lenght / 2),
                    point_lenght, point_lenght));
        } else {
            g2.setColor(Color.BLUE);
            g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(listErrorPlus.size() - 1).x * xLength) - (point_lenght / 2),
                    Y_AXIS_SECOND_Y_COORD - (listErrorPlus.get(listErrorPlus.size() - 1).y * yLength) - (point_lenght / 2),
                    point_lenght, point_lenght));
            g2.setColor(titleColor);
            g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(listPoints.size() - 1).x * xLength) - (point_lenght / 2),
                    Y_AXIS_SECOND_Y_COORD - (listPoints.get(listPoints.size() - 1).y * yLength) - (point_lenght / 2),
                    point_lenght, point_lenght));
            g2.setColor(Color.GRAY);
            g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(listErrorMinus.size() - 1).x * xLength) - (point_lenght / 2),
                    Y_AXIS_SECOND_Y_COORD - (listErrorMinus.get(listErrorMinus.size() - 1).y * yLength) - (point_lenght / 2),
                    point_lenght, point_lenght));
        }
        timer.start();
        revalidate();
    }

    /**
     * Method that is triggered each s seconds in order to analize if the
     * results file has changed.
     *
     * @param ae Action event that triggered the method.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == timer) {
            long newTimeStamp = file.lastModified();
            if (timeStamp != newTimeStamp) {
                timeStamp = newTimeStamp;
                readFile();
                revalidate();
                repaint();
            }
        }
    }

    /**
     * Method that helps the graphic visualization.
     *
     * @return Whole dimension of the panel.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension((X_AXIS_FIRST_X_COORD * 2) + (listPoints.size() * xLength) + SCROLL_SPACE_HORIZONTAL, Y_AXIS_SECOND_Y_COORD + SCROLL_SPACE_VERTICAL);
    }
}
