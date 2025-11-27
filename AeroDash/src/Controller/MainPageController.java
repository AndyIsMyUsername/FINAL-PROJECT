package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MainPageController {

    @FXML private Button startButton;
    private MediaPlayer mainMusic;

    @FXML
    private void initialize() {
        startButton.setOnAction(e -> openAeroDash());
        playMainMusic();
    }

    private void openAeroDash() {
        try {
            
            //stop music 
            mainMusic.stop();
            
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
    
    //play sound when race is finished
    private void playMainMusic() { 
        try {
            Media mainMenuSound = new Media(getClass().getResource("/Sound/mainMenuSound.mp3").toExternalForm());
            mainMusic = new MediaPlayer(mainMenuSound);
            mainMusic.setVolume(0.5);
            mainMusic.setCycleCount(1);
            mainMusic.play();
        } catch(Exception e) { 
            System.out.println("COULD NOT PLAY MUSIC ");
        }
    }
}
