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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


/**
 *
 * @author aRosales
 */
class CartesianPanel extends JPanel implements ActionListener{
    private final Timer timer = new Timer(1000, this);
    // x-axis coord constants
    public static final int X_AXIS_FIRST_X_COORD = 50;
    public int X_AXIS_SECOND_X_COORD = 400;
    public int X_AXIS_Y_COORD = 400;
    
    // y-axis coord constants
    public static final int Y_AXIS_DATA_Y_COORD = 20;
    public static final int Y_AXIS_FIRST_Y_COORD = 30;
    public int Y_AXIS_SECOND_Y_COORD = 400;
    public static final int Y_AXIS_X_COORD = 50;
    
    //arrows of axis are represented with "hipotenuse" of
    //triangle
    // now we are define length of cathetas of that triangle
    public static final int FIRST_LENGHT = 10;
    public static final int SECOND_LENGHT = 5;
    
    // size of start coordinate lenght
    public static final int ORIGIN_COORDINATE_LENGHT = 6;
    public static final int point_lenght = 3;
    
    // distance of coordinate strings from axis
    public static final int AXIS_STRING_DISTANCE = 30;
    
    //added by me
    private double counter;
    private int counterTop;
    private ArrayList<Point2D.Double> listPoints;
    private ArrayList<Point2D.Double> listErrorPlus;
    private ArrayList<Point2D.Double> listErrorMinus;
    private String dataY;
    private boolean finish;
    private int columnAVG;
    private final int xCoordNumbers = 15;
    private final int yCoordNumbers = 10;
    private int yMinimum;
    private int yMaximum;
    private Color titleColor;
    private int cicle;
    private int xLength;
    private double predictionError;
    
    private long timeStamp;
    private File file;
    
    public void setCoordinates(int width, int height) {
        X_AXIS_SECOND_X_COORD = width;
        X_AXIS_Y_COORD = width;
        Y_AXIS_SECOND_Y_COORD = height;
        // numerate axis
        xLength = (X_AXIS_SECOND_X_COORD - X_AXIS_FIRST_X_COORD)
                / xCoordNumbers;
    }
    
    public void setAxes(String data, int minimum, int maximum, Color color, double error) {
        xLength = (X_AXIS_SECOND_X_COORD - X_AXIS_FIRST_X_COORD)
                / xCoordNumbers;
        counter=0;
        counterTop=0;
        cicle=0;
        finish=false;
        listPoints = new ArrayList<>();
        Point2D.Double origin = new Point2D.Double(counter, 0);
        listPoints.add(origin);
        listErrorMinus = new ArrayList<>();
        Point2D.Double originMinus = new Point2D.Double(counter, 0-error);
        listErrorMinus.add(originMinus);
        listErrorPlus = new ArrayList<>();
        Point2D.Double originPlus = new Point2D.Double(counter, 0+error);
        listErrorPlus.add(originPlus);
        counter++;
        //X_AXIS_SECOND_X_COORD = x_axis_second_x_coord;
        /*X_AXIS_SECOND_X_COORD = this.getWidth()-100;
        //X_AXIS_Y_COORD = x_axis_y_coord;
        X_AXIS_Y_COORD = this.getWidth()-100;
        //Y_AXIS_SECOND_Y_COORD = y_axis_second_y_coord;
        Y_AXIS_SECOND_Y_COORD = this.getHeight()-100;*/
        dataY = data;
        //yMinimum = minimum-(int)Math.ceil(error);
        //yMaximum = maximum+(int)Math.ceil(error);
        titleColor = color;
        //predictionError = error;
        file = new File("MAE_result.csv");
        timeStamp = file.lastModified();
        readFile();
        //printPoints();
    }
        
    public void readFile(){
        
        listPoints.clear();
        listErrorPlus.clear();
        listErrorMinus.clear();
        counter=1;
        //Point2D.Double origin = new Point2D.Double(counter, 0);
        //listPoints.add(origin);
        
        String csvFile = "MAE_result.csv";
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ";";
        
        try {

		br = new BufferedReader(new FileReader(csvFile));
                line = br.readLine();
                //int count = 0;
                String[] titles = line.split(cvsSplitBy);
                /*for(int i = 0 ; i<titles.length;i++){
                    if(titles[i].equals(dataY)){
                        column = count;
                        break;
                    }
                    else{
                        count++;
                    }
                }*/
                
                /*for(int i=1;i<=counterTop;i++){
                    line = br.readLine();
                }*/
                columnAVG = 2;
                int top = 0;
                Double min = 0.0;
                Double max = 0.0;
                while ((line = br.readLine()) != null)
                {
                    String[] values = line.split(cvsSplitBy);
                    Double yValue = null;
                    Double valuePlus = null;
                    Double valueMinus = null;
                    yValue = Double.valueOf(values[columnAVG]);
                    valuePlus = Double.valueOf(values[columnAVG+1]);
                    if(valuePlus>max)
                        max = valuePlus;
                    valueMinus = Double.valueOf(values[columnAVG+2]);
                    predictionError = Double.valueOf(values[1]);
                    
                    if(valueMinus<min)
                        min = valueMinus;
                    Point2D.Double point = new Point2D.Double(counter, yValue);
                    listPoints.add(point);
                    Point2D.Double pointPlus = new Point2D.Double(counter, valuePlus);
                    listErrorPlus.add(pointPlus);
                    Point2D.Double pointMinus = new Point2D.Double(counter, valueMinus);
                    listErrorMinus.add(pointMinus);
                    
                    counter++;
                    counterTop++;
                    top++;
                }
                //if((line = br.readLine()) == null)
                    //finish=true;
                System.out.println("countertop "+counterTop);
                //counter=1;

                cicle++;
                
                yMinimum = (int)Math.ceil(min);
                yMaximum = (int)Math.ceil(max);
                System.out.println("min "+yMinimum);
                System.out.println("max "+yMaximum);
                System.out.println("list "+listPoints.size());

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
        
        /*try
        {
            //listPoints.clear();
            
            POIFSFileSystem file = new POIFSFileSystem(new FileInputStream("Prediction.xls"));
            
            //Create Workbook instance holding reference to .xlsx file
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            
            //Get first/desired sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(1);
            
            Iterator<Row> rowIterator = sheet.iterator();
            Row titles = rowIterator.next();
            Iterator<Cell> titlesIterator = titles.cellIterator();
            int count = 0;
            while (titlesIterator.hasNext())
            {
                Cell cell = titlesIterator.next();
                //Check the cell type and format accordingly
                if(cell.getStringCellValue().equals(dataY)){
                    column = count;
                    break;
                }
                else{
                    count++;
                }
            }
            for(int i=1;i<=counterTop;i++){
                rowIterator.next();
            }
            int top = 0;
            //while (rowIterator.hasNext() && top<xCoordNumbers)
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                for(int i=0;i<column;i++){
                    cellIterator.next();
                }
                Cell cell = cellIterator.next();
                Double yValue;
                yValue = cell.getNumericCellValue();
                Point2D.Double point = new Point2D.Double(counter, yValue);
                listPoints.add(point);
                //
                Point2D.Double pointPlus = new Point2D.Double(counter, yValue + predictionError);
                listErrorPlus.add(pointPlus);
                Point2D.Double pointMinus = new Point2D.Double(counter, yValue - predictionError);
                listErrorMinus.add(pointMinus);
                //
                counter++;
                counterTop++;
                top++;
            }
            if(!rowIterator.hasNext())
                finish=true;
            System.out.println("countertop "+counterTop);
            //counter=1;
            
            cicle++;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/
    }
    
    public void printPoints(){
        for(int i=0;i<listPoints.size();i++){
            System.out.println("x: " + listPoints.get(i).x + " y: " + listPoints.get(i).y);
        }
    }
    
    //
    
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
        
        Font font1 = new Font(g2.getFont().getFontName(), Font.BOLD, 12);
        g2.setFont(font1); 
        
        
        
        // x-axis arrow
        g2.drawLine(X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength) - FIRST_LENGHT,
                X_AXIS_Y_COORD - SECOND_LENGHT,
                X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength), X_AXIS_Y_COORD);
        g2.drawLine(X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength) - FIRST_LENGHT,
                X_AXIS_Y_COORD + SECOND_LENGHT,
                X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength), X_AXIS_Y_COORD);
        
        // draw text "X" and draw text "Y"
        g2.drawString("X", X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength) ,
                X_AXIS_Y_COORD + AXIS_STRING_DISTANCE);
        g2.drawString(dataY, Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                Y_AXIS_DATA_Y_COORD );
        g2.drawString("(0, 0)", X_AXIS_FIRST_X_COORD - AXIS_STRING_DISTANCE,
                Y_AXIS_SECOND_Y_COORD + AXIS_STRING_DISTANCE);
        
        Font font2 = new Font(g2.getFont().getFontName(), Font.PLAIN, 10);
        g2.setFont(font2); 
        
        
        
        // draw x-axis numbers
        for(int i = 1; i < listPoints.size(); i++) {
            g2.drawLine(X_AXIS_FIRST_X_COORD + (i * xLength),
                    X_AXIS_Y_COORD - SECOND_LENGHT,
                    X_AXIS_FIRST_X_COORD + (i * xLength),
                    X_AXIS_Y_COORD + SECOND_LENGHT);
            g2.drawLine(X_AXIS_FIRST_X_COORD + ((i) * xLength), X_AXIS_Y_COORD,
                X_AXIS_FIRST_X_COORD + ((i-1) * xLength), X_AXIS_Y_COORD);
        }
        g2.drawLine(X_AXIS_FIRST_X_COORD + (listPoints.size() * xLength), X_AXIS_Y_COORD,
                X_AXIS_FIRST_X_COORD + ((listPoints.size()-1) * xLength), X_AXIS_Y_COORD);
        for(int i = 1; i < listPoints.size(); i++) {
                g2.drawString(Integer.toString(i),
                        X_AXIS_FIRST_X_COORD + (i * xLength) - 3,
                        X_AXIS_Y_COORD + AXIS_STRING_DISTANCE);
            }
        
                
        int yLength;
        
        //draw y-axis numbers
        //int division;
        float division;
        if(yMaximum > yCoordNumbers){
            Float max = (float)yMaximum;
            yLength = (Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD)
                    / yCoordNumbers;
            //division = (int)Math.ceil(max / yCoordNumbers);
            division = max / yCoordNumbers;
            for(int i = 1; i < yCoordNumbers; i++) {
                g2.drawLine(Y_AXIS_X_COORD - SECOND_LENGHT,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength),
                        Y_AXIS_X_COORD + SECOND_LENGHT,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength));
                String result = String.format("%.2f", i * division);
                g2.drawString(result,
                        Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength));
            }
            g2.drawString(Double.toString(yCoordNumbers * division),
                        Y_AXIS_X_COORD - AXIS_STRING_DISTANCE,
                        Y_AXIS_SECOND_Y_COORD - (yCoordNumbers * yLength));
        }
        else{
            yLength = (Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD)
                / yMaximum;
            for(int i = yMinimum+1; i < yMaximum; i++) {
                g2.drawLine(Y_AXIS_X_COORD - SECOND_LENGHT,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength),
                        Y_AXIS_X_COORD + SECOND_LENGHT,
                        Y_AXIS_SECOND_Y_COORD - (i * yLength));
                g2.drawString(Integer.toString(yMinimum+(int)Math.ceil(predictionError)+i),
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
        
        for (int i=0;i<listPoints.size()-1;i++){
            g2.setStroke(stroke);
            g2.setColor(titleColor);
            Point2D.Double initial;
            Point2D.Double end;
            Point2D.Double initialPlus;
            Point2D.Double endPlus;
            Point2D.Double initialMinus;
            Point2D.Double endMinus;
            
            if(yMaximum > yCoordNumbers){
                initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(i).y*yCoordNumbers/yMaximum) * yLength));
                initialPlus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorPlus.get(i).y*yCoordNumbers/yMaximum) * yLength));
                initialMinus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorMinus.get(i).y*yCoordNumbers/yMaximum) * yLength));
                end = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i+1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(i+1).y*yCoordNumbers/yMaximum)* yLength));
                endPlus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(i+1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorPlus.get(i+1).y*yCoordNumbers/yMaximum)* yLength));
                endMinus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(i+1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - ((listErrorMinus.get(i+1).y*yCoordNumbers/yMaximum)* yLength));
                
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - ((listPoints.get(i).y*yCoordNumbers/yMaximum) * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
            }
            else{
                initial = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(i).y * yLength));
                initialPlus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listErrorPlus.get(i).y * yLength));
                initialMinus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(i).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listErrorMinus.get(i).y * yLength));
                end = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i+1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(i+1).y * yLength));
                endPlus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorPlus.get(i+1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listErrorPlus.get(i+1).y * yLength));
                endMinus = new Point2D.Double(X_AXIS_FIRST_X_COORD + (listErrorMinus.get(i+1).x * xLength),
                        Y_AXIS_SECOND_Y_COORD - (listErrorMinus.get(i+1).y * yLength));
                
                g2.draw(new Ellipse2D.Double(X_AXIS_FIRST_X_COORD + (listPoints.get(i).x * xLength) - (point_lenght / 2),
                        Y_AXIS_SECOND_Y_COORD - (listPoints.get(i).y * yLength) - (point_lenght / 2),
                        point_lenght, point_lenght));
            }
                        
            g2.draw(new Line2D.Double(initial,end));
            
            g2.setColor(Color.RED);
            //g2.setStroke(stroke1);
            
            g2.draw(new Line2D.Double(initialPlus,endPlus));
            g2.setColor(Color.GREEN);
            g2.draw(new Line2D.Double(initialMinus,endMinus));
            
            
        }        
        //if(!finish)
            timer.start();
        revalidate();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==timer){
            long newTimeStamp = file.lastModified();
            if(timeStamp != newTimeStamp ) {
               timeStamp = newTimeStamp;
               readFile();
               revalidate();
               repaint();
            }
            /*if(!finish){
                //removeAll();
                readFile();
                //printPoints();
                revalidate();
                repaint();// this will call at every s seconds
            }*/
            //else
              //  revalidate();
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension((X_AXIS_FIRST_X_COORD*2) + (listPoints.size() * xLength), Y_AXIS_SECOND_Y_COORD+Y_AXIS_X_COORD);
        //return new Dimension((X_AXIS_FIRST_X_COORD*2) + X_AXIS_SECOND_X_COORD, Y_AXIS_SECOND_Y_COORD+Y_AXIS_X_COORD);
    }
}