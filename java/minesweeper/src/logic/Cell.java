package logic;

/**
 * Klasse für eine Zelle des Minesweeper Feldes
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class Cell {

    /**
     * enum für die verschiedene Modi einer Zelle
     */
    protected enum CellState {
        COVERED, SUSPECTED, UNCOVERED
    }

    /**der aktuelle Status der Zelle*/
    private CellState state;

    /**ob sie eine Bombe enthält*/
    private final boolean hasBomb;

    /**die Anzahl der Bomben in den 8 benachbarten Zellen*/
    private int neighbourBombCount;


    /**
     * Konstruktor mit ausschließlich einem Parameter für hasBomb
     *
     * @param hasBomb ob sie eine Bombe enthält
     */
    public Cell(boolean hasBomb) {
        this.hasBomb = hasBomb;
        state = CellState.COVERED;
    }

    /**
     * Konstruktor mit aeinem Parameter für hasBomb und einen für state
     *
     * @param hasBomb ob sie eine Bombe enthält
     * @param state der aktuelle Status der Zelle
     */
    public Cell(boolean hasBomb, CellState state) {
        this(hasBomb);
        this.state = state;
    }

    /**
     * Getter für hasBomb
     *
     * @return hasBomb
     */
    public boolean hasBomb() {
        return hasBomb;
    }

    /**
     * Getter für neighbourBombCount
     *
     * @return neighbourBombCount
     */
    public int getNoOfAdjacentBombs() {
        return neighbourBombCount;
    }

    /**
     * Ob die Zelle makiert wurde
     *
     * @return Ob die Zelle makiert wurde
     */
    public boolean isSuspected() {
        return state.equals(CellState.SUSPECTED);
    }

    /**
     * Ob die Zelle aufgedeckt ist
     *
     * @return Ob die Zelle aufgedeckt ist
     */
    public boolean isUncovered() {
        return state.equals(CellState.UNCOVERED);
    }

    /**
     * Gibt an ob die Zelle korrekt markiert ist
     * eine Zelle ist korrekt markiert, wenn sie markiert ist und eine Bombe enthält ODER wenn sie nicht markiert
     * ist und keine Bombe enthält
     *
     * @return true -> korrekt markiert, sonst -> false
     */
    public boolean isMarkedCorrectly() {
        return isSuspected() && hasBomb() || !isSuspected() && !hasBomb();
    }

    /**
     * deckt die Zelle auf (ändert den Status)
     */
    public void uncover() {
        state = CellState.UNCOVERED;
    }

    /**
     * setzt die Anzahl der mit Bomben belegten Nachbarzellen
     * @param count Anzahl der Bomben der Nachbarn
     */
    public void setNoOfAdjacentBombs(int count) {
        neighbourBombCount = count;
    }

    /**
     * ändert den Status einer Zelle (von verdeckt zu markiert und anders herum)
     */
    public void toggleSuspected() {
        if (!isUncovered())
            state = isSuspected() ? CellState.COVERED : CellState.SUSPECTED;
    }

}
