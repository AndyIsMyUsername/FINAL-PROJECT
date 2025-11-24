package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainPageController {

    @FXML private Button startButton;

    @FXML
    private void initialize() {
        startButton.setOnAction(e -> openAeroDash());
    }

    private void openAeroDash() {
        try {
            // Load the AeroDash FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Aerodash.fxml"));
            Parent root = loader.load();

            // Get current stage and set new scene
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("AeroDash Simulation");
            stage.setMaximized(true); // optional: open full-screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
