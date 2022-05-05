package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.GUIConnector;


/**
 * Klasse für Funktion zur GUI / veränderung des ImageView und Label
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class JavaFXGUI implements GUIConnector {

    private final ImageView[][] imageView;
    private final Label status;

    JavaFXGUI(ImageView[][] imageView, Label status) {
        this.imageView = imageView;
        this.status = status;
    }

    // Konstanten für die Bilder
    private static final Image IMG_BOMB = new Image("gui/img/bomb_explodes.png");
    private static final Image IMG_COVER = new Image("gui/img/covered.png");
    private static final Image[] IMG_NUM = {
            new Image("gui/img/imgEmpty.png"),
            new Image("gui/img/img1.png"),
            new Image("gui/img/img2.png"),
            new Image("gui/img/img3.png"),
            new Image("gui/img/img4.png"),
            new Image("gui/img/img5.png"),
            new Image("gui/img/img6.png"),
            new Image("gui/img/img7.png"),
            new Image("gui/img/img8.png"),
    };
    private static final Image IMG_MARKED = new Image("gui/img/marked_suspected.png");

    /**
     * Covers the given cell again, so it is neither suspected nor can the user see if the cell contains a bomb or how
     * many bombs are neighboured.
     *
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     */
    @Override
    public void coverCell(int x, int y) {
        imageView[x][y].setImage(IMG_COVER);
    }

    /**
     * Marks cell at the specified coordinate as suspected.
     *
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     */
    @Override
    public void markCellAsSuspected(int x, int y) {
        imageView[x][y].setImage(IMG_MARKED);
    }

    /**
     * Reveals what is hidden at the given position
     *
     * @param x                 x-coordinate of the position
     * @param y                 y-coordinate of the position
     * @param isBomb            if a bomb is placed at the cell
     * @param neighbouringBombs how many neighbouring bombs this cell has
     */
    @Override
    public void uncoverCell(int x, int y, boolean isBomb, int neighbouringBombs) {
        if (isBomb)
            imageView[x][y].setImage(IMG_BOMB);
        else {
            imageView[x][y].setImage(IMG_NUM[neighbouringBombs]);
        }
    }

    /**
     * Called once the game has ended.
     *
     * @param won if the user won the game
     */
    @Override
    public void gameEnded(boolean won) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game has ended");
        alert.setHeaderText(won ? "You Won \uD83D\uDC51" : "You Lost \uD83D\uDE14");
        alert.setContentText("If you want you can start a new game.");
        alert.showAndWait();
    }

    /**
     * Displays the number of bombs placed for a new game on the screen.
     *
     * @param noOfBombs number of Bombs placed
     */
    @Override
    public void displayNoOfBombs(int noOfBombs) {
        status.setText(String.format("Number of bombs placed on the field: %d", noOfBombs));
    }

}
