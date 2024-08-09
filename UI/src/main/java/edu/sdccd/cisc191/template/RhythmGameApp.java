package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The {@code RhythmGameApp} class serves as the entry point for the JavaFX application
 * that represents the rhythm game. It extends the {@code Application} class and is responsible
 * for loading the FXML layout and setting up the primary stage.
 */
public class RhythmGameApp extends Application {

    /**
     * Starts the JavaFX application by setting up the primary stage and loading the FXML layout.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML layout
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/game_layout.fxml")));
        primaryStage.setTitle("JavaHero");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
