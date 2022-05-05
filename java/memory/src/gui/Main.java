package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


/**
 * Main Klasse zu initialisieren und laufen des Fensters
 *
 * Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserInterface.fxml")));
        primaryStage.setTitle("Memory");
        primaryStage.setMinWidth(1200.0);
        primaryStage.setMinHeight(700.0);
        primaryStage.setScene(new Scene(root, primaryStage.getMinWidth(), primaryStage.getMinHeight()));
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
