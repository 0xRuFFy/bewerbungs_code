package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Main Klasse für die JavaFX Anwendung "Minesweeper"
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class Main extends Application {

    /**
     * Initialisiert das Fenster
     *
     * @param primaryStage Stage
     * @throws Exception Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("UserInterface.fxml"));
        primaryStage.setMinWidth(550);
        primaryStage.setMinHeight(450);
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }

    /**
     * main zum lauch des Fensters
     *
     * @param args Konsolen Argumente
     */
    public static void main(String[] args) {
        launch(args);
    }
}
