package Controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    @FXML private Slider volumeSlider;

    @FXML private LineChart<String, Number> liftAngleGraph;
    @FXML private LineChart<String, Number> dragVelocityGraph;

    @FXML private ImageView spaceImage;

    private static final double AIR_DENSITY = 1.225;
    private static final double CL = 1.2;
    private static final double CD = 0.02;

    private MediaPlayer dashMedia;

    @FXML
    private void initialize() {
        startButton.setOnAction(e -> startSimulation());
        resetButton.setOnAction(e -> resetFields());
        viewPathButton.setOnAction(e -> viewPath());

        playDashMusic();
        initializeGraphs();
        setupVolumeSlider();

     
        startFloatingAnimation();
    }


    private void startFloatingAnimation() {
        if (spaceImage != null) {
            // Fly-away and come-back animation
            TranslateTransition flyAnim = new TranslateTransition(Duration.seconds(10), spaceImage);
            flyAnim.setByX(500); // move right
            flyAnim.setAutoReverse(true); // come back
            flyAnim.setCycleCount(TranslateTransition.INDEFINITE);
            flyAnim.play();


            TranslateTransition floatAnim = new TranslateTransition(Duration.seconds(3), spaceImage);
            floatAnim.setByY(-25); // float up
            floatAnim.setAutoReverse(true);
            floatAnim.setCycleCount(TranslateTransition.INDEFINITE);
            floatAnim.play();
        } else {
            System.out.println("SPACE IMAGE IS NULL");
        }
    }

 
    private void setupVolumeSlider() {
        if (volumeSlider != null) {
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (dashMedia != null) {
                    dashMedia.setVolume(newVal.doubleValue());
                }
            });
        }
    }


    private void initializeGraphs() {
        dragVelocityGraph.getData().clear();
        liftAngleGraph.getData().clear();

        dragVelocityGraph.setAnimated(false);
        liftAngleGraph.setAnimated(false);
    }

    private void updateDragVelocityGraph(double currentVelocity, double wingArea) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Drag Force");

        for (int v = 0; v <= 200; v += 20) {
            double drag = calculateDrag(v, wingArea);
            series.getData().add(new XYChart.Data<>(String.valueOf(v), drag));
        }

        XYChart.Series<String, Number> point = new XYChart.Series<>();
        point.setName("Current");
        point.getData().add(new XYChart.Data<>(
                String.valueOf((int) currentVelocity),
                calculateDrag(currentVelocity, wingArea)
        ));

        dragVelocityGraph.getData().clear();
        dragVelocityGraph.getData().addAll(series, point);
    }

    private void updateLiftAngleGraph(double velocity, double wingArea) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Lift Force");

        for (int angle = -10; angle <= 30; angle += 2) {
            double lift = calculateLift(velocity, wingArea, angle);
            series.getData().add(new XYChart.Data<>(String.valueOf(angle), lift));
        }

        liftAngleGraph.getData().clear();
        liftAngleGraph.getData().add(series);
    }


    private void startSimulation() {
        try {
            double velocity = Double.parseDouble(airSpeedTextField.getText());
            double wingArea = Double.parseDouble(wingAreaTextField.getText());
            double angle = Double.parseDouble(angleOfAttack.getText());

            if (velocity <= 0 || wingArea <= 0) {
                showError("Velocity and wing area must be positive values");
                return;
            } else showError("");

            if (angle < -10 || angle > 30) {
                showError("Angle of attack must be between -10° and 30°");
                return;
            } else showError("");

            double lift = calculateLift(velocity, wingArea, angle);
            double drag = calculateDrag(velocity, wingArea);

            airSpeedValue.setText(String.format("%.2f m/s", velocity));
            liftValue.setText(String.format("%.2f N", lift));
            dragValue.setText(String.format("%.2f N", drag));

            updateDragVelocityGraph(velocity, wingArea);
            updateLiftAngleGraph(velocity, wingArea);

        } catch (NumberFormatException ex) {
            airSpeedValue.setText("Invalid input");
            liftValue.setText("Invalid input");
            dragValue.setText("Invalid input");
        }
    }

    private double calculateLift(double velocity, double wingArea, double angleOfAttack) {
        double alpha = Math.toRadians(angleOfAttack);
        double cl = CL * Math.sin(2 * alpha);
        return 0.5 * AIR_DENSITY * velocity * velocity * wingArea * cl;
    }

    private double calculateDrag(double velocity, double wingArea) {
        return 0.5 * AIR_DENSITY * velocity * velocity * wingArea * CD;
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    public void restoreData(double velocity, double wingArea, double angle) {
        airSpeedTextField.setText(String.valueOf(velocity));
        wingAreaTextField.setText(String.valueOf(wingArea));
        angleOfAttack.setText(String.valueOf(angle));

        double lift = calculateLift(velocity, wingArea, angle);
        double drag = calculateDrag(velocity, wingArea);

        airSpeedValue.setText(String.format("%.2f m/s", velocity));
        liftValue.setText(String.format("%.2f N", lift));
        dragValue.setText(String.format("%.2f N", drag));

        updateDragVelocityGraph(velocity, wingArea);
        updateLiftAngleGraph(velocity, wingArea);
    }

    private void resetFields() {
        airSpeedTextField.clear();
        wingAreaTextField.clear();
        angleOfAttack.clear();
        liftValue.setText("N/A");
        dragValue.setText("N/A");
        airSpeedValue.setText("N/A");
        errorLabel.setText("");

        initializeGraphs();
    }

    private void viewPath() {
        try {
            if (dashMedia != null) dashMedia.stop();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PathView.fxml"));
            Parent root = loader.load();

            PathViewController pathController = loader.getController();

            String vel = airSpeedTextField.getText();
            String area = wingAreaTextField.getText();
            String ang = angleOfAttack.getText();

            if (!vel.isEmpty() && !area.isEmpty() && !ang.isEmpty()) {
                try {
                    pathController.setFlightData(
                        Double.parseDouble(vel),
                        Double.parseDouble(area),
                        Double.parseDouble(ang)
                    );
                } catch (NumberFormatException ignored) { }
            }

            Stage stage = (Stage) viewPathButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Rocket Path View");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            airSpeedValue.setText("Error loading path view");
        }
    }

    private void playDashMusic() {
        try {
            Media dashSong = new Media(getClass().getResource("/Sound/dashboardsong.mp3").toExternalForm());
            dashMedia = new MediaPlayer(dashSong);

            double startVolume = 0.1;
            dashMedia.setVolume(startVolume);

            if (volumeSlider != null)
                volumeSlider.setValue(startVolume);

            dashMedia.setCycleCount(1);
            dashMedia.play();

        } catch (Exception e) {
            System.out.println("COULD NOT PLAY MUSIC");
        }
    }
}
