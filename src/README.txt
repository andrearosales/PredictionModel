This folder contains the java classes that are used for building the visualization framework.
In particular, "predictionmodel" is the main project package and it contains the following classes:
- PredictionModel: Main class of the project. It is in charge of running the main frame that contains the visualization panel.
In order to run the project it is necessary to pass five command line parameters:
1. Predicted signal (HR_next, HR_peak, VO2_next or VO2_peak)
2. Approach that was used for the prediction (Multiple-test or Single-test)
3. Technique that was used for the prediction (ANN or SVM)
4. Measurement of error used in the prediction (MAE or RMSE)
5. Name of the file that contains the measurements on the physiological signals.

- CartesianFrame: This class is in charge of configuring the panel container. It sets the panel title and it instantiates the graphics panel.

- CartesianPanel: This class contains the panel where the graphics will be displayed. It is in charge of updating the graphics each time the results file has been modified with new results.

- CartesianFrameSignal: This class is in charge of configuring the main container for the physiological signals. It sets the principal characteristics of each of them (i.e. The minimum and maximum values a signal can achieve). If the dataset changes it is necessary to modify the signal characteristics accordingly to the new dataset (panel.setAxes(...) method).

- BigPanel: This class is in charge of containing the panels for the physiological signals. It is used to set the preferred size of the main panel each time a change occurs (new data is included).

- CartesianPanelSignal: This class contains the panel where the graphics will be displayed. It is in charge of updating the graphics each certain amount of time in order to include new data into the visualization window.

In order to run the project it is necessary to:

1. Install Eclipse
2. Once in Eclipse, create a new project
- File -> New -> Java Project ->
- Write the project name (Prediction Model) -> Finish
3. Copy the source files
- Select the package that contains the source files and copy it into the src folder of the created project.
	* Visualization/src/predictionmodel
4. Refresh the project
- Right click -> Refresh
5. Copy the signals files and the MAE output file into the root project directory.
- Physiological signals file: The file that contains the measured signals before applying normalization, sampling or windowing.
- MAE file: The output file once the compute_mae.py script has run.
6. Specify the command line parameter
- Right click -> Properties -> Run/Debug Settings -> New -> Select Java Application
- Write in the name field: Java Application
- In the Main option select the main class: predictionmodel.PredictionModel
- In the Arguments option -> Program arguments: Write the five necessary command line parameters all divided by a white space
	* Predicted signal (HR_next, HR_peak, VO2_next or VO2_peak)
	* Approach that was used for the prediction (Multiple-test or Single-test)
	* Technique that was used for the prediction (ANN or SVM)
	* Measurement of error used in the prediction (MAE or RMSE)
	* Name of the file that contains the physiological signals measurements.
6. Apply the changes
7. Run the project
- Right click -> Run As -> 2 Java Application

These steps are used to test the code statically.
In order to run it along with the prediction model it's necessary to create the .jar file:
- Right click -> Export
- Expand the Java node and select Runnable JAR file -> Click Next
- Select the Launch configuration
- Select the Export destination (PredictionModel.jar). It has to be in the same folder where the python script will run.
- Click Finish