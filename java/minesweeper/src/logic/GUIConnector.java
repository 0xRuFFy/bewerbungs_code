package logic;

/**
 * Connection of logic to gui for Minesweeper program.
 *
 * @author cei
 */
public interface GUIConnector {

    /**
     * Covers the given cell again, so it is neither suspected nor can the user see if the cell contains a bomb or how
     * many bombs are neighboured.
     *
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     */
    void coverCell(int x, int y);

    /**
     * Marks cell at the specified coordinate as suspected.
     *
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     */
    void markCellAsSuspected(int x, int y);

    /**
     * Reveals what is hidden at the given position
     *
     * @param x                 x-coordinate of the position
     * @param y                 y-coordinate of the position
     * @param isBomb            if a bomb is placed at the cell
     * @param neighbouringBombs how many neighbouring bombs this cell has
     */
    void uncoverCell(int x, int y, boolean isBomb, int neighbouringBombs);

    /**
     * Called once the game has ended.
     *
     * @param won if the user won the game
     */
    void gameEnded(boolean won);

    /**
     * Displays the number of bombs placed for a new game on the screen.
     *
     * @param noOfBombs number of Bombs placed
     */
    void displayNoOfBombs(int noOfBombs);
}
