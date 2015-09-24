This folder contains the java classes that are used for building the visualization framework.
In particular, "predictionmodel" is the main project package and it contains the following classes:
- PredictionModel: Main class of the project. It is in charge of running the main frame that contains the visualization panel.
In order to run the project it is necessary to pass four command line parameters:
1. Predicted signal (HR_next, HR_peak, VO2_next or VO2_peak)
2. Approach that was used for the prediction (Multiple-test or Single-test)
3. Technique that was used for the prediction (ANN or SVM)
4. Measurement of error used in the prediction (MAE or RMSE)

- CartesianFrame: This class is in charge of configuring the panel container. It sets the panel title and it instantiates the graphics panel.

- CartesianPanel: This class contains the panel where the graphics will be displayed. It is in charge of updating the graphics each time the results file has been modified with new results.