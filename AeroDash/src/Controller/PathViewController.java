package Controller;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
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
    private double angle;
    private double wingArea;
    
    // Animation elements
    private Circle rocket;
    private PathTransition pathTransition;
    private FadeTransition fadeTransition;
    
    //physic CONSTANTS
    private static final double GRAVITY = 9.81;

    @FXML
    private void initialize() {
        // Create rocket circle (initially hidden)
        rocket = new Circle(6);
        rocket.setFill(Color.rgb(255, 165, 0));
        rocket.setStroke(Color.rgb(255, 100, 0));
        rocket.setStrokeWidth(2);
        rocket.setVisible(false);
        
        //draw the grid
        drawBaseGrid();
    }
    
    // Method called from AeroDashController
    public void setFlightData(double velocity, double wingArea, double angle) {
        this.velocity = velocity;
        this.wingArea = wingArea;
        this.angle = angle;

        velocityLabel.setText(String.format("Velocity: %.2f m/s", velocity));
        angleLabel.setText(String.format("Angle: %.1f°", angle));

        // Compute simple projectile motion (no drag)
        double rad = Math.toRadians(angle);
        double maxHeight = (velocity * velocity * Math.sin(rad) * Math.sin(rad)) / (2 * GRAVITY);
        double range = (velocity * velocity * Math.sin(2 * rad)) / GRAVITY;

        maxHeightLabel.setText(String.format("Max Height: %.2f m", maxHeight));
        rangeLabel.setText(String.format("Range: %.2f m", range));

        // Add rocket to parent if not already added
        addRocketToScene();
        
        drawTrajectoryPath();
    }
    
    // Helper method to add rocket to the scene
    private void addRocketToScene() {
        if (rocket.getParent() == null && pathCanvas.getParent() != null) {
            if (pathCanvas.getParent() instanceof Pane) {
                ((Pane) pathCanvas.getParent()).getChildren().add(rocket);
            } else if (pathCanvas.getParent().getParent() instanceof Pane) {
                // Sometimes canvas is nested deeper
                ((Pane) pathCanvas.getParent().getParent()).getChildren().add(rocket);
            }
        }
    }
    
    private void drawTrajectoryPath() {
        drawBaseGrid();
        
        if (velocity == 0) return;
        
        GraphicsContext gc = pathCanvas.getGraphicsContext2D();
        gc.setStroke(Color.rgb(79, 227, 255));
        gc.setLineWidth(2);

        double rad = Math.toRadians(angle);
        double range = (velocity * velocity * Math.sin(2 * rad)) / GRAVITY;
        double maxHeight = (velocity * velocity * Math.sin(rad) * Math.sin(rad)) / (2 * GRAVITY);

        double scaleX = (pathCanvas.getWidth() - 40) / range;
        double scaleY = (pathCanvas.getHeight() - 40) / (maxHeight + 10);
        double scale = Math.min(scaleX, scaleY);

        double offsetX = 20;
        double offsetY = pathCanvas.getHeight() - 20;

        gc.beginPath();
        gc.moveTo(offsetX, offsetY);

        // Draw trajectory curve
        double timeStep = 0.05;
        double totalTime = 2 * velocity * Math.sin(rad) / GRAVITY;
        
        for (double t = 0; t <= totalTime; t += timeStep) {
            double x = velocity * Math.cos(rad) * t;
            double y = velocity * Math.sin(rad) * t - 0.5 * GRAVITY * t * t;

            if (y < 0) break;

            double screenX = offsetX + x * scale;
            double screenY = offsetY - y * scale;

            gc.lineTo(screenX, screenY);
        }

        gc.stroke();

        // Draw launch point
        gc.setFill(Color.rgb(255, 100, 100));
        gc.fillOval(offsetX - 4, offsetY - 4, 8, 8);
        
        // Draw landing point
        gc.fillOval(offsetX + range * scale - 4, offsetY - 4, 8, 8);
    }

    // Draw grid and ground line
    private void drawBaseGrid() {
        GraphicsContext gc = pathCanvas.getGraphicsContext2D();
        
        // Clear canvas with dark background
        gc.setFill(Color.rgb(26, 31, 43));
        gc.fillRect(0, 0, pathCanvas.getWidth(), pathCanvas.getHeight());
        
        // Draw grid
        gc.setStroke(Color.rgb(60, 70, 90, 0.3));
        gc.setLineWidth(0.5);
        
        for (int i = 0; i <= 10; i++) {
            double x = i * pathCanvas.getWidth() / 10;
            gc.strokeLine(x, 0, x, pathCanvas.getHeight());
            double y = i * pathCanvas.getHeight() / 10;
            gc.strokeLine(0, y, pathCanvas.getWidth(), y);
        }

        // Ground line
        gc.setStroke(Color.rgb(100, 200, 100));
        gc.setLineWidth(2);
        gc.strokeLine(0, pathCanvas.getHeight() - 20, pathCanvas.getWidth(), pathCanvas.getHeight() - 20);
    }

    // Animate trajectory - MUST have @FXML annotation
    @FXML
    void animateTrajectory(ActionEvent event) {
        // Stop any existing animations
        if (pathTransition != null) {
            pathTransition.stop();
        }
        if (fadeTransition != null) {
            fadeTransition.stop();
        }

        // Redraw the path
        drawTrajectoryPath();

        if (velocity == 0) return;

        // Make sure rocket is added to scene
        addRocketToScene();

        // Calculate trajectory parameters
        double rad = Math.toRadians(angle);
        double range = (velocity * velocity * Math.sin(2 * rad)) / GRAVITY;
        double maxHeight = (velocity * velocity * Math.sin(rad) * Math.sin(rad)) / (2 * GRAVITY);
        double totalTime = 2 * velocity * Math.sin(rad) / GRAVITY;

        double scaleX = (pathCanvas.getWidth() - 40) / range;
        double scaleY = (pathCanvas.getHeight() - 40) / (maxHeight + 10);
        double scale = Math.min(scaleX, scaleY);

        double offsetX = 20;
        double offsetY = pathCanvas.getHeight() - 20;

        // Get canvas position in parent coordinates
        double canvasX = pathCanvas.getLayoutX();
        double canvasY = pathCanvas.getLayoutY();
        
        // Create path for animation (adjusted for canvas position)
        Path animationPath = new Path();
        animationPath.getElements().add(new MoveTo(canvasX + offsetX, canvasY + offsetY));
        
        // Make the path invisible ( we only want to see the canvas-drawn trajectory ) 
        animationPath.setStroke(Color.TRANSPARENT);
        animationPath.setFill(Color.TRANSPARENT);
        animationPath.setVisible(false);

        double timeStep = 0.05;
        for (double t = timeStep; t <= totalTime; t += timeStep) {
            double x = velocity * Math.cos(rad) * t;
            double y = velocity * Math.sin(rad) * t - 0.5 * GRAVITY * t * t;

            if (y < 0) break;

            double screenX = canvasX + offsetX + x * scale;
            double screenY = canvasY + offsetY - y * scale;

            animationPath.getElements().add(new LineTo(screenX, screenY));
        }

        // Setup rocket position and visibility (adjusted for canvas position)
        rocket.setCenterX(canvasX + offsetX);
        rocket.setCenterY(canvasY + offsetY);
        rocket.setVisible(true);
        rocket.setOpacity(1.0);

        // Create PathTransition
        pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(totalTime));
        pathTransition.setPath(animationPath);
        pathTransition.setNode(rocket);
        pathTransition.setCycleCount(1);

        // Add fade out effect at the end
        pathTransition.setOnFinished(e -> {
            fadeTransition = new FadeTransition(Duration.seconds(0.5), rocket);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setOnFinished(ev -> rocket.setVisible(false));
            fadeTransition.play();
        });

        // Start animation
        pathTransition.play();
    }

    // Reset animation
    @FXML
    void resetAnimation(ActionEvent event) {
        if (pathTransition != null) {
            pathTransition.stop();
        }
        if (fadeTransition != null) {
            fadeTransition.stop();
        }
        
        rocket.setVisible(false);
        rocket.setOpacity(1.0);
        drawTrajectoryPath();
    }

    // Go back to main dashboard
    @FXML
    void backToDashboard(ActionEvent event) {
        try {
            // Stop all animations
            if (pathTransition != null) {
                pathTransition.stop();
            }
            if (fadeTransition != null) {
                fadeTransition.stop();
            }
            
            // Load main dashboard - check your actual file name
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/View/Aerodash.fxml")  // Changed to lowercase 'd'
            );
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) backButton.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to return to dashboard: " );
        }
    }
}