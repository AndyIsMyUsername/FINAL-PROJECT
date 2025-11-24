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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AeroDashView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("AeroDash Simulation");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
