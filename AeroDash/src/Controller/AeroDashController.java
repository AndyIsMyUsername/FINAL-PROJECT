package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class AeroDashController {

    @FXML private TextField airSpeedTextField;
    @FXML private TextField angleOfAttack;
    @FXML private TextField wingAreaTextField;

    @FXML private Label airSpeedValue;
    @FXML private Label liftValue;
    @FXML private Label dragValue;
    @FXML private Label errorLabel;

    @FXML private Button startButton;
    @FXML private Button resetButton;
    @FXML private Button viewPathButton;

    @FXML private LineChart<String, Number> liftAngleGraph;
    @FXML private LineChart<String, Number> dragVelocityGraph;
    @FXML private LineChart<String, Number> spachShipGraph;

    private static final double AIR_DENSITY = 1.225; // kg/m³
    private static final double CL = 1.2;           // lift coefficient
    private static final double CD = 0.02;          // drag coefficient
    
    private MediaPlayer dashMedia;

    @FXML
    private void initialize() {
        startButton.setOnAction(e -> startSimulation());
        resetButton.setOnAction(e -> resetFields());
        viewPathButton.setOnAction(e -> viewPath());
        playDashMusic();

        //initialize all the graphs
        initializeGraphs();
    }

    
     private void initializeGraphs() {
        // Clear any existing data
        dragVelocityGraph.getData().clear();
        liftAngleGraph.getData().clear();
        spachShipGraph.getData().clear();
        
        // Set animated to false for better performance
        dragVelocityGraph.setAnimated(false);
        liftAngleGraph.setAnimated(false);
        spachShipGraph.setAnimated(false);
    }
     
    private void startSimulation() {
        try {
            double velocity = Double.parseDouble(airSpeedTextField.getText());
            double wingArea = Double.parseDouble(wingAreaTextField.getText());
            double angle = Double.parseDouble(angleOfAttack.getText());
            
             // Validate inputs(dont allow negative values
            if (velocity <= 0 || wingArea <= 0) {
                showError("Velocity and wing area must be positive values");
                return;
            } else {
                showError("");
            }
           
            
            if (angle < -10 || angle > 30) {
                showError("Angle of attack should be between -10° and 30°");
                return;
            } else {
                showError("");
            }

            double lift = calculateLift(velocity, wingArea, angle);
            double drag = calculateDrag(velocity, wingArea);

            airSpeedValue.setText(String.format("%.2f m/s", velocity));
            liftValue.setText(String.format("%.2f N", lift));
            dragValue.setText(String.format("%.2f N", drag));

            // Update graphs
            updateDragVelocityGraph(velocity, wingArea);
            updateLiftAngleGraph(velocity, wingArea);
            updateSpaceshipGraph(velocity, lift, drag);

        } catch (NumberFormatException ex) {
            airSpeedValue.setText("Invalid input");
            liftValue.setText("Invalid input");
            dragValue.setText("Invalid input");
        }
    }
     
    /*
    calculate the lift
    */
    private double calculateLift(double velocity, double wingArea, double angleOfAttack) {
        // Calculate lift coefficient based on angle of attack
        // Using a simplified linear model: CL = CL_max * sin(2 * alpha)
        double alpha = Math.toRadians(angleOfAttack);
        double cl = CL * Math.sin(2 * alpha);
        
        // Lift formula: L = 0.5 * ρ * V² * S * CL
        return 0.5 * AIR_DENSITY * velocity * velocity * wingArea * cl;
    }
    
    /*
    calculate the drag
    */
    private double calculateDrag(double velocity, double wingArea) {
        // Drag formula: D = 0.5 * ρ * V² * S * CD
        return 0.5 * AIR_DENSITY * velocity * velocity * wingArea * CD;
    }

    /*
    show an error
    */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
    }
    
    /*
    update the drag vs velocity graph
    */
    private void updateDragVelocityGraph(double currentVelocity, double wingArea) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Drag Force");

        // Generate data points for different velocities (0 to 200 m/s)
        for (int v = 0; v <= 200; v += 20) {
            double drag = calculateDrag(v, wingArea);
            series.getData().add(new XYChart.Data<>(String.valueOf(v), drag));
        }
        
        // Add current velocity point
        XYChart.Series<String, Number> currentPoint = new XYChart.Series<>();
        currentPoint.setName("Current");
        currentPoint.getData().add(new XYChart.Data<>(
            String.format("%.0f", currentVelocity), 
            calculateDrag(currentVelocity, wingArea)
        ));

        dragVelocityGraph.getData().clear();
        dragVelocityGraph.getData().addAll(series, currentPoint);
    }
    
    /*
    update lift vs angle
    */
    private void updateLiftAngleGraph(double velocity, double wingArea) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Lift Force");

        // Generate data points for different angles of attack (-10° to 30°)
        for (int angle = -10; angle <= 30; angle += 2) {
            double lift = calculateLift(velocity, wingArea, angle);
            series.getData().add(new XYChart.Data<>(String.valueOf(angle), lift));
        }

        liftAngleGraph.getData().clear();
        liftAngleGraph.getData().add(series);
    }
    
    /*
    update the main spaceship graph
    */
    private void updateSpaceshipGraph(double velocity, double lift, double drag) {
        XYChart.Series<String, Number> liftSeries = new XYChart.Series<>();
        liftSeries.setName("Lift");
        
        XYChart.Series<String, Number> dragSeries = new XYChart.Series<>();
        dragSeries.setName("Drag");
        
        XYChart.Series<String, Number> netForceSeries = new XYChart.Series<>();
        netForceSeries.setName("Net Force");

        // Simple time-based simulation (0 to 10 seconds)
        for (int t = 0; t <= 10; t++) {
            liftSeries.getData().add(new XYChart.Data<>(String.valueOf(t), lift));
            dragSeries.getData().add(new XYChart.Data<>(String.valueOf(t), drag));
            netForceSeries.getData().add(new XYChart.Data<>(String.valueOf(t), lift - drag));
        }

        spachShipGraph.getData().clear();
        spachShipGraph.getData().addAll(liftSeries, dragSeries, netForceSeries);
    }

    /*
    restore data when coming back from PathView
    */
    public void restoreData(double velocity, double wingArea, double angle) {
        // Set the text fields with previous values
        airSpeedTextField.setText(String.valueOf(velocity));
        wingAreaTextField.setText(String.valueOf(wingArea));
        angleOfAttack.setText(String.valueOf(angle));
        
        // Recalculate and display results
        double lift = calculateLift(velocity, wingArea, angle);
        double drag = calculateDrag(velocity, wingArea);
        
        airSpeedValue.setText(String.format("%.2f m/s", velocity));
        liftValue.setText(String.format("%.2f N", lift));
        dragValue.setText(String.format("%.2f N", drag));
        
        // Update all graphs
        updateDragVelocityGraph(velocity, wingArea);
        updateLiftAngleGraph(velocity, wingArea);
        updateSpaceshipGraph(velocity, lift, drag);
    }

    /*
    reset the filed values
    */
    private void resetFields() {
       // Clear all input and output fields
        airSpeedTextField.clear();
        wingAreaTextField.clear();
        angleOfAttack.clear();
        liftValue.setText("N/A");
        dragValue.setText("N/A");
        airSpeedValue.setText("N/A");
        errorLabel.setText("");
        
        // Clear graphs
        initializeGraphs();
    }

    //view path panel(change window)
    private void viewPath() {
       try {
           
           //stop music if playing
           if (dashMedia != null) { 
               dashMedia.stop();
           }
           
            // Get current simulation data
            String velocityStr = airSpeedTextField.getText();
            String wingAreaStr = wingAreaTextField.getText();
            String angleStr = angleOfAttack.getText();
            
            // Load the path view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PathView.fxml"));
            Parent root = loader.load();
            
            // Get the controller and pass data
            PathViewController pathController = loader.getController();
            
            // Only pass data if simulation has been run
            if (!velocityStr.isEmpty() && !wingAreaStr.isEmpty() && !angleStr.isEmpty()) {
                try {
                    double velocity = Double.parseDouble(velocityStr);
                    double wingArea = Double.parseDouble(wingAreaStr);
                    double angle = Double.parseDouble(angleStr);
                    pathController.setFlightData(velocity, wingArea, angle);
                } catch (NumberFormatException e) {
                    // If parsing fails, just open the view without data
                }
            }
            
            // Get the current stage
            Stage stage = (Stage) viewPathButton.getScene().getWindow();
            
            // Create new scene and set it
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Rocket Path View");
            stage.show();
            
        } catch (Exception e) {
            System.err.println("Error loading Path View: " + e.getMessage());
            e.printStackTrace();
            airSpeedValue.setText("Error loading path view");
        }
    }
    
    //play sound when race is finished
    private void playDashMusic() { 
        try {
            Media dashSong = new Media(getClass().getResource("/Sound/dashboardsong.mp3").toExternalForm());
            dashMedia = new MediaPlayer(dashSong);
            dashMedia.setVolume(0.1);
            dashMedia.setCycleCount(1);
            dashMedia.play();
        } catch(Exception e) { 
            System.out.println("COULD NOT PLAY MUSIC ");
        }
    }
}
