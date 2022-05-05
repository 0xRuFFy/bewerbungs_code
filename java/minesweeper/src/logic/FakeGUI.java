package logic;

import logic.GUIConnector;

/**
 * Required for testing. Methods of the gui should still be called, but because we do not test the gui through JUnit,
 * nothing should happen in these cases. It would be possible to add boolean flags to check if necessary gui methods are
 * called in the logic.
 *
 * @author cei
 */
public class FakeGUI implements GUIConnector {
    @Override
    public void coverCell(int x, int y) {
        //nothing should happen
    }

    @Override
    public void markCellAsSuspected(int x, int y) {
        //nothing should happen
    }

    @Override
    public void uncoverCell(int x, int y, boolean isBomb, int neighbouringBombs) {
        //nothing should happen
    }

    @Override
    public void gameEnded(boolean won) {
        //nothing should happen
    }

    @Override
    public void displayNoOfBombs(int noOfBombs) {
        //nothing should happen
    }
}
