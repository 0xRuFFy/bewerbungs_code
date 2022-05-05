package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import logic.PairsLogic;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller für UserInterface.fxml
 * Verknüpfung von Logik und GUI
 *
 * Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class UserInterfaceController implements Initializable {

    /**Text Eingabe Felder*/
    @FXML
    public TextField txtFldPlayer1, txtFldPlayer2;

    /**Text Labels*/
    @FXML
    private Label lblCurrentPlayerText, lblCurrentPlayer, lblWinner, lblPlayerName1, lblPlayerName2;

    /**Alle Knöpfe*/
    @FXML
    private Button btn00, btn10, btn20, btn30, btn01, btn11, btn21, btn31,
            btn02, btn12, btn22, btn32, btnNewGame, btnStartGame;

    /**Main Grid Pane*/
    @FXML
    private GridPane grdPnField;

    /**Logik Klasse*/
    private PairsLogic game;

    private Button[][] btnsGamePlayer;

    /**
     * Erstellt ein neues Spiel mit gegebenen Spielernamen
     *
     * @param name1 Spielername 1
     * @param name2 Spielername 2
     * @return das neue Spiel
     */
    private PairsLogic newGame(String name1, String name2, Button[][] buttons) {
        lblCurrentPlayer.setText(name1);
        return new PairsLogic(
                name1, name2, grdPnField.getColumnCount(), grdPnField.getRowCount(),
                new JavaFXGUI(
                        buttons,
                        this.lblCurrentPlayer,
                        this.lblWinner,
                        this.lblCurrentPlayerText
                )
        );
    }

    /**
     * Funktion für die Memorykarten (Buttons)
     *
     * @param actionEvent Event
     */
    @FXML
    private void handleBtnGamePlay(ActionEvent actionEvent) {
        Button current = ((Button) actionEvent.getSource());
        Integer x = GridPane.getRowIndex(current);
        Integer y = GridPane.getColumnIndex(current);
        x = (x == null ? 0 : x);
        y = (y == null ? 0 : y);
        this.game.playerTurn(new int[]{x, y});
    }

    /**
     * Setzt die `Buttons` auf die Standardwerte zurück
     * @param btns zurück zusetzend `Buttons`
     */
    private void resetButtons(Button[][] btns) {
        for (Button[] row : btns) {
            for (Button btn : row) {
                btn.setDisable(false);
                btn.setStyle("-fx-background-color: #8075ff;");
                btn.setText("");
            }
        }
    }

    /**
     * Öffnet die `StartGame` Optionen
     *
     *
     * @param actionEvent Event
     */
    @FXML
    public void handleBtnNewGame(ActionEvent actionEvent) {
        for (Button[] row : btnsGamePlayer) {
            for (Button btn : row) {
                btn.setDisable(true);
            }
        }
        lblCurrentPlayerText.setDisable(true);
        lblCurrentPlayer.setDisable(true);
        lblWinner.setVisible(false);
        btnStartGame.setDisable(false);
        txtFldPlayer1.setDisable(false);
        txtFldPlayer2.setDisable(false);
        lblPlayerName1.setDisable(false);
        lblPlayerName2.setDisable(false);
        btnNewGame.setDisable(true);
    }


    /**
     * Zum start des ersten Games
     *
     * @param url URL
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.lblWinner.setVisible(false);
        btnStartGame.setDisable(true);
        txtFldPlayer1.setDisable(true);
        txtFldPlayer2.setDisable(true);
        lblPlayerName1.setDisable(true);
        lblPlayerName2.setDisable(true);
        btnsGamePlayer = new Button[][]{
                {btn00, btn10, btn20, btn30},
                {btn01, btn11, btn21, btn31},
                {btn02, btn12, btn22, btn32},
        };

        btnNewGame.setPadding(Insets.EMPTY);
        btnStartGame.setPadding(Insets.EMPTY);
        for (Button[] bnts : btnsGamePlayer)
            for (Button bnt : bnts) {
                bnt.setPadding((Insets.EMPTY));
            }

        this.game = newGame(txtFldPlayer1.getText(), txtFldPlayer2.getText(), btnsGamePlayer);
    }

    /**
     * Startet das neue Spiel mit Namen aus den Text Feldern
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void handleBtnStartGame(ActionEvent actionEvent) {
        resetButtons(btnsGamePlayer);
        game = newGame(txtFldPlayer1.getText(), txtFldPlayer2.getText(), btnsGamePlayer);

        lblCurrentPlayerText.setDisable(false);
        lblCurrentPlayer.setDisable(false);
        btnStartGame.setDisable(true);
        txtFldPlayer1.setDisable(true);
        txtFldPlayer2.setDisable(true);
        lblPlayerName1.setDisable(true);
        lblPlayerName2.setDisable(true);
        btnNewGame.setDisable(false);
    }
}
