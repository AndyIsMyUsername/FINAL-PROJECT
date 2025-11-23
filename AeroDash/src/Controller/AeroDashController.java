package Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AeroDashController {

    @FXML
    private Label AirSpeedLabel;

    @FXML
    private Label AirSpeedValue;

    @FXML
    private Label DrageLabel;

    @FXML
    private Label LiftLabel;

    @FXML
    private TextField airSpeedTextField;

    @FXML
    private TextField areaOfAttackTextField;

    @FXML
    private Label dragValue;

    @FXML
    private LineChart<?, ?> dragVelocityGraph;

    @FXML
    private LineChart<?, ?> liftAreaGraph;

    @FXML
    private Label liftValue;

    @FXML
    private Button resetButton;

    @FXML
    private Button startButton;

    @FXML
    private Button viewPathButton;

    @FXML
    private TextField wingAreaTextField;

    // Constants for air properties (example values)
    private static final double AIR_DENSITY = 1.225; // kg/m³
    private static final double CL = 1.2; // Lift coefficient (example)
    private static final double CD = 0.02; // Drag coefficient (example)

    @FXML
    private void initialize() {
        startButton.setOnAction(e -> startSimulation());
        resetButton.setOnAction(e -> resetFields());
        viewPathButton.setOnAction(e -> viewPath());
    }

    private void startSimulation() {
        try {
            // Read input values
            double velocity = Double.parseDouble(airSpeedTextField.getText());
            double wingArea = Double.parseDouble(wingAreaTextField.getText());
            double angle = Double.parseDouble(areaOfAttackTextField.getText());

            // Simple aerodynamic formulas (basic placeholders)
            double lift = 0.5 * AIR_DENSITY * velocity * velocity * wingArea * CL;
            double drag = 0.5 * AIR_DENSITY * velocity * velocity * wingArea * CD;

            // Display the calculated values
            liftValue.setText(String.format("%.2f N", lift));
            dragValue.setText(String.format("%.2f N", drag));

        } catch (NumberFormatException ex) {
            liftValue.setText("Invalid input");
            dragValue.setText("Invalid input");
        }
    }

    private void resetFields() {
        // Clear all input and output fields
        airSpeedTextField.clear();
        wingAreaTextField.clear();
        areaOfAttackTextField.clear();

        liftValue.setText("");
        dragValue.setText("");
    }
    
    //view path of the rocketship
    private void viewPath() { 
        
        try { 
    
        String velocityStr = AirSpeedLabel.getText();
        String wingAreaStr = wingAreaTextField.getText();
        String angleStr = areaOfAttackTextField.getText();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PathView.fxml"));
        Parent root = loader.load();
        
        PathViewController pathController = loader.getController();
        
        
        
        if (!velocityStr.isEmpty() && !wingAreaStr.isEmpty() && !angleStr.isEmpty()) { 
            try { 
                double velocity = Double.parseDouble(velocityStr);
                double wingArea = Double.parseDouble(wingAreaStr);
                double angle = Double.parseDouble(angleStr);
                //set the data
                
            } catch (NumberFormatException e) { 
                //if dosent work
            }
        }
        
        //get the current stage
        Stage stage = (Stage) viewPathButton.getScene().getWindow();
        
        //create a new scene that will show the rocket path
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Rocket Path View");
        stage.show();
        
        //if it cant load path view
        } catch (Exception e){ 
            System.out.println("DID NOT WORK");
        }
    }
}
