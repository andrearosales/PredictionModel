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