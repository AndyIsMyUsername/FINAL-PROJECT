package Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class PathViewController {

    @FXML private Label velocityLabel;
    @FXML private Label angleLabel;
    @FXML private Label maxHeightLabel;
    @FXML private Label rangeLabel;

    @FXML private Canvas pathCanvas;
    @FXML private Button animateButton;
    @FXML private Button resetAnimationButton;
    @FXML private Button backButton;

    // Flight data
    private double velocity;
    private double angle;   // degrees
    private double wingArea; // optional, can use for scaling

    // Animation timeline
    private Timeline animation;

    // Method called from AeroDashController
    public void setFlightData(double velocity, double wingArea, double angle) {
        this.velocity = velocity;
        this.wingArea = wingArea;
        this.angle = angle;

        velocityLabel.setText(String.format("Velocity: %.2f m/s", velocity));
        angleLabel.setText(String.format("Angle: %.1f°", angle));

        // Compute simple projectile motion (no drag)
        double rad = Math.toRadians(angle);
        double g = 9.81;
        double maxHeight = (velocity * velocity * Math.sin(rad) * Math.sin(rad)) / (2 * g);
        double range = (velocity * velocity * Math.sin(2 * rad)) / g;

        maxHeightLabel.setText(String.format("Max Height: %.2f m", maxHeight));
        rangeLabel.setText(String.format("Range: %.2f m", range));

        drawBaseGrid();
    }

    // Draw grid and ground line
    private void drawBaseGrid() {
        GraphicsContext gc = pathCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, pathCanvas.getWidth(), pathCanvas.getHeight());

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);

        // Ground line
        gc.strokeLine(0, pathCanvas.getHeight() - 20, pathCanvas.getWidth(), pathCanvas.getHeight() - 20);
    }

    // Animate trajectory
    @FXML
    void animateTrajectory(ActionEvent event) {
        drawBaseGrid();

        if (animation != null) animation.stop();

        GraphicsContext gc = pathCanvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setFill(Color.RED);

        double rad = Math.toRadians(angle);
        double g = 9.81;

        double scaleX = pathCanvas.getWidth() / ((velocity * velocity * Math.sin(2 * rad)) / g); // range fits canvas
        double scaleY = pathCanvas.getHeight() / ((velocity * velocity * Math.sin(rad) * Math.sin(rad)) / (2 * g) + 20);

        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);

        final double[] t = {0}; // time tracker
        KeyFrame keyFrame = new KeyFrame(Duration.millis(20), e -> {
            double x = velocity * Math.cos(rad) * t[0];
            double y = velocity * Math.sin(rad) * t[0] - 0.5 * g * t[0] * t[0];

            if (y < 0) {
                animation.stop();
                return;
            }

            double screenX = x * scaleX;
            double screenY = pathCanvas.getHeight() - (y * scaleY) - 20;

            gc.fillOval(screenX, screenY, 4, 4);

            t[0] += 0.05;
        });

        animation.getKeyFrames().add(keyFrame);
        animation.play();
    }

    // Reset animation
    @FXML
    void resetAnimation(ActionEvent event) {
        if (animation != null) animation.stop();
        drawBaseGrid();
    }

    // Go back to main dashboard
    @FXML
    void backToDashboard(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/View/AeroDash.fxml")
            );
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) backButton.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println("Failed to return: " + e.getMessage());
        }
    }
}