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
import javafx.stage.Stage;

public class AeroDashController {

    @FXML private TextField airSpeedTextField;
    @FXML private TextField areaOfAttackTextField;
    @FXML private TextField wingAreaTextField;

    @FXML private Label AirSpeedValue;
    @FXML private Label liftValue;
    @FXML private Label dragValue;

    @FXML private Button startButton;
    @FXML private Button resetButton;
    @FXML private Button viewPathButton;

    @FXML private LineChart<String, Number> liftAreaGraph;
    @FXML private LineChart<String, Number> dragVelocityGraph;
    @FXML private LineChart<String, Number> spachShipGraph;

    private static final double AIR_DENSITY = 1.225; // kg/m³
    private static final double CL = 1.2;           // lift coefficient
    private static final double CD = 0.02;          // drag coefficient

    @FXML
    private void initialize() {
        startButton.setOnAction(e -> startSimulation());
        resetButton.setOnAction(e -> resetFields());
        viewPathButton.setOnAction(e -> viewPath());

        // Set black text for the big graph
        setGraphTextBlack(spachShipGraph);
        // Set black text for small graphs
        setGraphTextBlack(liftAreaGraph);
        setGraphTextBlack(dragVelocityGraph);
    }

    private void startSimulation() {
        try {
            double velocity = Double.parseDouble(airSpeedTextField.getText());
            double wingArea = Double.parseDouble(wingAreaTextField.getText());
            double angle = Double.parseDouble(areaOfAttackTextField.getText());

            double lift = 0.5 * AIR_DENSITY * velocity * velocity * wingArea * CL;
            double drag = 0.5 * AIR_DENSITY * velocity * velocity * wingArea * CD;

            AirSpeedValue.setText(String.format("%.2f m/s", velocity));
            liftValue.setText(String.format("%.2f N", lift));
            dragValue.setText(String.format("%.2f N", drag));

            // Bottom small graphs
            updateGraph(liftAreaGraph, "Lift vs Angle", a -> lift, 0, 20, 2);
            updateGraph(dragVelocityGraph, "Drag vs Velocity", v -> 0.5 * AIR_DENSITY * v * v * wingArea * CD, 0, velocity, 5);

            // Central big graph
            spachShipGraph.getData().clear();
            XYChart.Series<String, Number> liftSeriesBig = createSeries("Lift vs Angle", 0, 20, 2, a -> lift);
            XYChart.Series<String, Number> dragSeriesBig = createSeries("Drag vs Velocity", 0, velocity, 5, v -> 0.5 * AIR_DENSITY * v * v * wingArea * CD);
            spachShipGraph.getData().addAll(liftSeriesBig, dragSeriesBig);

        } catch (NumberFormatException ex) {
            AirSpeedValue.setText("Invalid input");
            liftValue.setText("Invalid input");
            dragValue.setText("Invalid input");
        }
    }

    private void resetFields() {
        airSpeedTextField.clear();
        areaOfAttackTextField.clear();
        wingAreaTextField.clear();

        AirSpeedValue.setText("N/A");
        liftValue.setText("N/A");
        dragValue.setText("N/A");

        liftAreaGraph.getData().clear();
        dragVelocityGraph.getData().clear();
        spachShipGraph.getData().clear();
    }

    private void viewPath() {
        try {
            double velocity = Double.parseDouble(airSpeedTextField.getText());
            double wingArea = Double.parseDouble(wingAreaTextField.getText());
            double angle = Double.parseDouble(areaOfAttackTextField.getText());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PathView.fxml"));
            Parent root = loader.load();

            PathViewController pathController = loader.getController();
            pathController.setFlightData(velocity, wingArea, angle);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Rocket Path View");
            stage.show();

        } catch (NumberFormatException ex) {
            System.out.println("Invalid input for velocity, wing area, or angle.");
        } catch (Exception e) {
            System.out.println("Failed to load PathView: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to update a graph with data
    private void updateGraph(LineChart<String, Number> chart, String name, java.util.function.DoubleUnaryOperator func,
                             double start, double end, double step) {
        chart.getData().clear();
        XYChart.Series<String, Number> series = createSeries(name, start, end, step, func);
        chart.getData().add(series);
    }

    // Helper method to create a series
    private XYChart.Series<String, Number> createSeries(String name, double start, double end, double step,
                                                        java.util.function.DoubleUnaryOperator func) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(name);
        for (double x = start; x <= end; x += step) {
            series.getData().add(new XYChart.Data<>(String.valueOf(x), func.applyAsDouble(x)));
        }
        return series;
    }

    // Helper method to set all chart text black
    private void setGraphTextBlack(LineChart<String, Number> chart) {
        chart.setStyle("-fx-text-fill: black;");
        chart.lookup(".chart-legend").setStyle("-fx-text-fill: black;");
        if (chart.getXAxis() != null) {
            chart.getXAxis().lookup(".axis-label").setStyle("-fx-text-fill: black;");
            chart.getXAxis().lookupAll(".tick-label").forEach(t -> t.setStyle("-fx-text-fill: black;"));
        }
        if (chart.getYAxis() != null) {
            chart.getYAxis().lookup(".axis-label").setStyle("-fx-text-fill: black;");
            chart.getYAxis().lookupAll(".tick-label").forEach(t -> t.setStyle("-fx-text-fill: black;"));
        }
    }
}
