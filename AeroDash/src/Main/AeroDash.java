 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author luoan
 */
public class AeroDash extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the main page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainView.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("Aerospace Dashboard");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}