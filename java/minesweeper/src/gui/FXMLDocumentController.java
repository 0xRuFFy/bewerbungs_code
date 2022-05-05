package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.Minesweeper;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller Klasse für UserInterface.fxml -> Verknüpfung von gui und logik
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class FXMLDocumentController implements Initializable {

    /**GridPane ~ Minesweeper Spielfeld*/
    @FXML
    private GridPane grdPnMineField;

    /**Statusmeldungs Lable*/
    @FXML
    private Label lblStatusmeldungen;

    /**Trefferwahrscheinlichkeit in Procent*/
    private final int probability = 10;

    /**aktuelle Instanz eines Spiels*/
    private Minesweeper game;

    /**
     * Creates an array of imageviews corresponding to the gridPane. Each imageView becomes a child of the gridPane and
     * fills a cell. For proper resizing it is binded to the gridPanes width and height.
     *
     * @return an array of imageviews added to the gridPane
     */
    private ImageView[][] initImages(GridPane grdPn) {
        int colcount = grdPn.getColumnConstraints().size();
        int rowcount = grdPn.getRowConstraints().size();
        ImageView[][] imageViews = new ImageView[colcount][rowcount];
        // bind each Imageview to a cell of the gridpane
        int cellWidth = (int) grdPn.getWidth() / colcount;
        int cellHeight = (int) grdPn.getHeight() / rowcount;
        for (int x = 0; x < colcount; x++) {
            for (int y = 0; y < rowcount; y++) {
                //creates an empty imageview
                imageViews[x][y] = new ImageView();
                //image has to fit a cell and mustn't preserve ratio
                imageViews[x][y].setFitWidth(cellWidth);
                imageViews[x][y].setFitHeight(cellHeight);
                imageViews[x][y].setPreserveRatio(false);
                imageViews[x][y].setSmooth(true);
                //assign the correct indicees for this imageview
                GridPane.setConstraints(imageViews[x][y], x, y);
                //add the imageview to the cell
                grdPn.add(imageViews[x][y], x, y);
                //the image shall resize when the cell resizes
                imageViews[x][y].fitWidthProperty().bind(grdPn.widthProperty().divide(colcount));
                imageViews[x][y].fitHeightProperty().bind(grdPn.heightProperty().divide(rowcount));
            }
        }
        return imageViews;
    }

    /**
     * lädt die ImageView aus der initImages Funktion, erstellt JavaFXGUI
     *      -> daraus wird fie Minesweeper Logik Klasse erstellt
     *
     * jedes Feld des ImageView Arrays wird mit dem covered Image geladen
     */
    private void startNewGame() {
        ImageView[][] imageViews = initImages(grdPnMineField);
        JavaFXGUI javaFXGUI = new JavaFXGUI(imageViews, lblStatusmeldungen);
        game = new Minesweeper(javaFXGUI, imageViews.length, imageViews[0].length, (float)probability/100);
        for (int i = 0; i < imageViews.length; i++) {
            for (int j = 0; j < imageViews[0].length; j++) {
                javaFXGUI.coverCell(i, j);
            }
        }
    }

    /**
     * reacts on clicking the gridPane.
     *
     * @param mouseEvent event responsible for calling this method
     */
    @FXML
    private void onMouseClickedMineField(MouseEvent mouseEvent) {
        int x = -1;
        int y = -1;
        boolean leftClicked = mouseEvent.getButton() == MouseButton.PRIMARY;
        boolean rightClicked = mouseEvent.getButton() == MouseButton.SECONDARY;
        //determine the imageview of the grid that contains the coordinates of the mouseclick
        //to determine the board-coordinates
        for (Node node : grdPnMineField.getChildren()) {
            if (node instanceof ImageView) {
                if (node.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
                    //to use following methods the columnIndex and rowIndex
                    //must have been set when adding the imageview to the grid
                    x = GridPane.getColumnIndex(node);
                    y = GridPane.getRowIndex(node);
                }
            }
        }

        assert (x >= 0 && y >= 0) : "dem Klick ist keine Koordinate zuzuordnen";
        if (leftClicked) {
            game.uncover(x, y);
        } else if (rightClicked) {
            game.suspect(x, y);
        }
    }

    /**
     * End the game when clicking Menu "Game/Close"
     *
     * @param actionEvent event responsible for calling this method (not used here).
     */
    @FXML
    private void onClickMnItmClose(ActionEvent actionEvent) {
        // use a known node (here the gridPane because the menuitem isn't a node)
        // and get its stage
        Stage stage = (Stage) grdPnMineField.getScene().getWindow();
        stage.close();
    }

    /**
     * End the game when clicking Menu "Game/Close"
     *
     * @param actionEvent event responsible for calling this method (not used here).
     */
    @FXML
    private void onClickMnItmNew(ActionEvent actionEvent) {
        grdPnMineField.getChildren().clear();
        startNewGame();
    }

    /**
     * Zum start des ersten Games
     *
     * @param url URL
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startNewGame();
    }
}