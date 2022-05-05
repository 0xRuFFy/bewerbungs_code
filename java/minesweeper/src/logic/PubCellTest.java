package logic;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the cell class published with the assignment.
 *
 * @author cei
 */
public class PubCellTest {

    @Test
    public void testCellConstructor_Bomb() {
        Cell bombCell = new Cell(true);
        Assert.assertFalse(bombCell.isUncovered());
        Assert.assertTrue(bombCell.hasBomb());
        assertEquals(0, bombCell.getNoOfAdjacentBombs());
        Assert.assertFalse(bombCell.isSuspected());
        Assert.assertFalse(bombCell.isMarkedCorrectly());
    }

    @Test
    public void testCellConstructor_NoBomb() {
        Cell noBombCell = new Cell(false);
        Assert.assertFalse(noBombCell.isUncovered());
        Assert.assertFalse(noBombCell.hasBomb());
        assertEquals(0, noBombCell.getNoOfAdjacentBombs());
        Assert.assertFalse(noBombCell.isSuspected());
        Assert.assertTrue(noBombCell.isMarkedCorrectly());
    }

    @Test
    public void test_TestCellConstructor_Bomb_Covered() {
        Cell bombCell = new Cell(true, Cell.CellState.COVERED);
        Assert.assertFalse(bombCell.isUncovered());
        Assert.assertTrue(bombCell.hasBomb());
        assertEquals(0, bombCell.getNoOfAdjacentBombs());
        Assert.assertFalse(bombCell.isSuspected());
        Assert.assertFalse(bombCell.isMarkedCorrectly());
    }

    @Test
    public void test_TestCellConstructor_NoBomb_Covered() {
        Cell noBombCell = new Cell(false);
        Assert.assertFalse(noBombCell.isUncovered());
        Assert.assertFalse(noBombCell.hasBomb());
        assertEquals(0, noBombCell.getNoOfAdjacentBombs());
        Assert.assertFalse(noBombCell.isSuspected());
        Assert.assertTrue(noBombCell.isMarkedCorrectly());
    }

    @Test
    public void test_TestCellConstructor_Bomb_Suspected() {
        Cell bombCell = new Cell(true, Cell.CellState.SUSPECTED);
        Assert.assertFalse(bombCell.isUncovered());
        Assert.assertTrue(bombCell.hasBomb());
        assertEquals(0, bombCell.getNoOfAdjacentBombs());
        Assert.assertTrue(bombCell.isSuspected());
        Assert.assertTrue(bombCell.isMarkedCorrectly());
    }

    @Test
    public void test_TestCellConstructor_NoBomb_Suspected() {
        Cell noBombCell = new Cell(false, Cell.CellState.SUSPECTED);
        Assert.assertFalse(noBombCell.isUncovered());
        Assert.assertFalse(noBombCell.hasBomb());
        assertEquals(0, noBombCell.getNoOfAdjacentBombs());
        Assert.assertTrue(noBombCell.isSuspected());
        Assert.assertFalse(noBombCell.isMarkedCorrectly());
    }

    @Test
    public void test_TestCellConstructor_Bomb_Uncovered() {
        Cell bombCell = new Cell(true, Cell.CellState.UNCOVERED);
        Assert.assertTrue(bombCell.isUncovered());
        Assert.assertTrue(bombCell.hasBomb());
        assertEquals(0, bombCell.getNoOfAdjacentBombs());
        Assert.assertFalse(bombCell.isSuspected());
        Assert.assertFalse(bombCell.isMarkedCorrectly());
    }

    @Test
    public void test_TestCellConstructor_NoBomb_Uncovered() {
        Cell noBombCell = new Cell(false, Cell.CellState.UNCOVERED);
        Assert.assertTrue(noBombCell.isUncovered());
        Assert.assertFalse(noBombCell.hasBomb());
        assertEquals(0, noBombCell.getNoOfAdjacentBombs());
        Assert.assertFalse(noBombCell.isSuspected());
        Assert.assertTrue(noBombCell.isMarkedCorrectly());
    }

    @Test
    public void test_toggleSuspected_StartingWithCovered() {
        Cell cell = new Cell(true, Cell.CellState.COVERED);
        cell.toggleSuspected();
        Assert.assertFalse(cell.isUncovered());
        Assert.assertTrue(cell.isSuspected());
    }

    @Test
    public void test_toggleSuspected_StartingWithSuspected() {
        Cell cell = new Cell(true, Cell.CellState.SUSPECTED);
        cell.toggleSuspected();
        Assert.assertFalse(cell.isUncovered());
        Assert.assertFalse(cell.isSuspected());
    }

    @Test
    public void test_toggleSuspected_CellAlreadyUncovered() {
        Cell cell = new Cell(true, Cell.CellState.UNCOVERED);
        Assert.assertTrue(cell.isUncovered());
        Assert.assertFalse(cell.isSuspected());
    }
}