package Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
}
