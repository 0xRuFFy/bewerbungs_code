package logic;

import java.util.Random;

/**
 * Logik Klasse für das Minesweeper Spiel / Feld
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class Minesweeper {

    /**Referenz zum GUIConnector*/
    private final GUIConnector guiConnector;

    /**zweidimensionales Array mit Zellen*/
    private final Cell[][] field;

    /** boolesche Variable, ob das Spiel beendet ist*/
    private boolean gameOver;


    /**
     * Konstruktor der einen GUIConnector, die Anzahl der Spalten, die Anzahl der Reihen und die Wahrscheinlichkeit
     * zum Treffen einer Bombe erhält. Beim Erstellen des Feldes wird für jede Zelle entschieden,
     * ob sie eine Bombe enthält, indem ein Zufallswert mit der übergebenen Wahrscheinlichkeit verglichen wird.
     *
     * @param guiConnector Referenz zum GUIConnector
     * @param rowCount Anzahl an Zeilen
     * @param collumCount Anzahl an Spalten
     * @param bombProbability Wahrscheinlichkeit einer Bombe (0 < float < 1) ~ 1 sind 100%
     */
    public Minesweeper(GUIConnector guiConnector, int rowCount, int collumCount, float bombProbability) {
        if (bombProbability > 1 || bombProbability < 0) {
            throw new IllegalArgumentException("bombProbability muss zwischen 0 und 1 liegen");
        }

        this.guiConnector = guiConnector;
        this.gameOver = false;
        field = new Cell[rowCount][collumCount];

        Random rand = new Random();

        int bombCount = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < collumCount; j++) {
                field[i][j] = new Cell(rand.nextFloat() <= bombProbability);
                if (field[i][j].hasBomb())
                    bombCount++;
            }
        }
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < collumCount; j++) {
                field[i][j].setNoOfAdjacentBombs(getNoOfAdjacentBombs(i, j));
            }
        }

        guiConnector.displayNoOfBombs(bombCount);
    }

    /**
     * Test Konstruktor der einen GUIConnector und eine Stringdarstellung der Zellen erhält
     *
     * Reihen sind mit "\n", Spalten mit " " getrennt
     * ('?' → verdeckt, 'S' → markiert (suspected), '!' → aufgedeckt)
     * Enthält die Zelle eine Bombe, ist das zweite Zeichen ein 'B', enthält die Zelle keine Bombe,
     * gibt es kein zweites Zeichen.
     *
     * @param guiConnector Referenz zum GUIConnector
     * @param fieldString Stringdarstellung der Zellen
     */
    public Minesweeper(GUIConnector guiConnector, String fieldString) {
        this.guiConnector = guiConnector;
        this.gameOver = false;
        String[] rows = fieldString.split("\n");
        field = new Cell[rows.length][rows[0].split(" ").length];

        int bombCount = 0;
        for (int i = 0; i < field.length; i++) {
            String[] row = rows[i].split(" ");
            for (int j = 0; j < field[0].length; j++) {

                Cell.CellState state = Cell.CellState.COVERED;
                if (row[j].startsWith("S"))
                    state = Cell.CellState.SUSPECTED;
                else if (row[j].startsWith("!"))
                    state = Cell.CellState.UNCOVERED;

                field[j][i] = new Cell(row[j].endsWith("B"), state);
                if (field[j][i].hasBomb())
                    bombCount++;
            }
        }

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j].setNoOfAdjacentBombs(getNoOfAdjacentBombs(i, j));
            }
        }
        guiConnector.displayNoOfBombs(bombCount);
    }

    /**
     * Nur für Test : Getter für Cell[][] field
     * @return this.field
     */
    Cell[][] getCells() {
        return this.field;
    }

    /**
     * wird aufgerufen, wenn der Nutzer eine Zelle an der übergebenen Position aufdecken will. Hat der Nutzer eine
     * Zelle gewählt, die bereits aufgedeckt ist, passiert nichts. Gleiches gilt für bereits markierte Zellen.
     * @param x Spalte
     * @param y Zeile
     */
    public void uncover(int x, int y) {
        if (!gameOver && !field[y][x].isUncovered() && !field[y][x].isSuspected()) {
            field[y][x].uncover();
            guiConnector.uncoverCell(x, y, field[y][x].hasBomb(), field[y][x].getNoOfAdjacentBombs());

            if (field[y][x].hasBomb() || won()) {
                gameOver = true;
                guiConnector.gameEnded(!field[y][x].hasBomb());
            }

            else if (field[y][x].getNoOfAdjacentBombs() == 0) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) // nicht sich selbst mit einbeziehen
                            if (areValidCoords(x + i, y + j))
                                uncover(x + i, y + j);
                    }
                }
            }
        }
    }

    /**
     * wird aufgerufen, wenn der Nutzer an der übergebenen Stelle eine Bombe vermutet und die Zelle markieren möchte.
     * Eine markierte Zelle kann nicht aufgedeckt werden. Eine aufgedeckte Zelle kann nicht markiert werden.
     * @param x Spalte
     * @param y Zeile
     */
    public void suspect(int x, int y) {
        if (!gameOver && !field[y][x].isUncovered()) {
            if (field[y][x].isSuspected())
                guiConnector.coverCell(x, y);
            else
                guiConnector.markCellAsSuspected(x, y);

            field[y][x].toggleSuspected();

            if (won()) {
                guiConnector.gameEnded(true);
                gameOver = true;
            }
        }
    }

    /**
     * bestimmt die Anzahl der in den direkt benachbarten Zellen versteckten Bomben
     * Rückgabewert kann nie mehr als 8 sein
     *
     * @param x Spalte
     * @param y Zeile
     * @return Anzahl an benachbarten Bomben
     */
    public int getNoOfAdjacentBombs(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) // nicht sich selbst mit einbeziehen
                    if (areValidCoords(y + i, x + j) && field[x+j][y+i].hasBomb())
                        count++;
            }
        }
        return count;
    }

    /**
     * true, wenn die übergebene Position valide für das Spielfeld ist
     * @param x Spalte
     * @param y Zeile
     * @return true -> valide, sonst -> false
     */
    boolean areValidCoords(int x, int y) {
        return (y >= 0 && y < field.length) && (x >= 0 && x < field[0].length);
    }

    /**
     * prüft, ob alle Zellen, die Bomben enthalten, markiert sind und keine Zelle ohne Bombe markiert ist
     *
     * @return true -> nur alle Bomben markiert, sonst -> false
     */
    boolean won() {
        boolean won = true;
        for (int i = 0; i < field.length && won; i++) {
            for (int j = 0; j < field[0].length && won; j++) {
                won = field[i][j].isMarkedCorrectly();
            }
        }
        return won;
    }
}
