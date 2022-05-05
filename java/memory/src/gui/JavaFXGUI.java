package gui;

import javafx.scene.control.Label;
import logic.GUIConnector;
import logic.PairsLogic.Symbol;
import javafx.scene.control.Button;

import java.util.Objects;

/**
 * Mit Methoden dieser Klasse kann die Logik Veränderungen auf der Oberfläche erzeugen.
 * Die JavaFXGUI wird vom FXMLController erzeugt und als Parameter an die Logik übergeben.
 * <p>
 * Weitere private oder protected Methoden dürfen hier zugefügt werden.
 * @author cei | Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class JavaFXGUI implements GUIConnector {

    /**
     * Die Buttons auf dem Spielfeld untergebracht in einem Array.
     * Position im Array entspricht der Position auf der Oberfläche
     */
    private final Button[][] btnsField;

    /**Label des für die Anzeige des aktuellen Spielers*/
    private final Label lblCurrentPlayer;

    /**Label zur Anzeige des Gewinners*/
    private final Label lblWinner;

    /**"Current Player" Lable*/
    private final Label lblCurrentPlayerText;

    /**Array mit allen emojis*/
    private final String[] emojis = new String[]{
            "🐝", "🍯", "🐻", "🐖", "🎂", "🦉", "👻", "📖", "🦇", "💩", "🐕", "🦄", "🐛"
    };
    
    /**
     * Der Konstruktor. Erhält alle Komponenten der Oberfläche, die nach
     * Änderungen in der Logik aktualisiert werden müssen.
     *
     * @param btns die Buttons des Spielfelds (können in ihrem Text die Symbole der Karten anzeigen)
     */
    public JavaFXGUI(Button[][] btns, Label lblCurrentPlayer, Label lblWinner, Label lblCurrentPlayerText) {
        this.btnsField = btns;
        this.lblCurrentPlayer = lblCurrentPlayer;
        this.lblWinner = lblWinner;
        this.lblCurrentPlayerText = lblCurrentPlayerText;
    }

    // JavaDoc für alle @Override -> GUIConnector

    @Override
    public void showCard(int[] coord, Symbol symbol) {
        btnsField[coord[0]][coord[1]].setStyle("-fx-background-color: #F8F0FB;");
        btnsField[coord[0]][coord[1]].setText(emojis[symbol.ordinal()]);
    }

    @Override
    public void hideCard(int[] pos) {
        btnsField[pos[0]][pos[1]].setStyle("-fx-background-color: #8075ff;");
        btnsField[pos[0]][pos[1]].setText("");
    }
    
    @Override
    public void setCurrentPlayer(String name) {
        this.lblCurrentPlayer.setText(name);
    }

    @Override
    public void onGameEnd(String winnerName) {
        lblWinner.setVisible(true);
        lblWinner.setText("Winner\nis " + Objects.requireNonNullElse(winnerName, "no one"));
        lblCurrentPlayer.setDisable(true);
        lblCurrentPlayerText.setDisable(true);
        for (Button[] btns : btnsField)
            for (Button btn : btns)
                btn.setDisable(true);
    }

}
