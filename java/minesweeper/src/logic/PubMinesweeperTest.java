package logic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for class Minesweeper published with assignment. Contains only tests for 2x2 field (with one exception).
 *
 * @author cei
 */
public class PubMinesweeperTest {

    @Test
    public void test0TestConstructor_2x2_CoveredNoBombs() {
        String field =
                "? ?\n" +
                "? ?\n";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        Cell[][] cells = game.getCells();
        for (int x = 0; x < cells.length; ++x) {
            for (int y = 0; y < cells[x].length; ++y) {
                assertFalse(cells[x][y].hasBomb());
                assertFalse(cells[x][y].isUncovered());
            }
        }
    }

    @Test
    public void test0TestConstructor_2x2_CoveredOneBomb() {
        String field =
                "? ?\n" +
                "? ?B\n";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        Cell[][] cells = game.getCells();
        assertFalse(cells[0][0].hasBomb());
        assertFalse(cells[1][0].hasBomb());
        assertFalse(cells[0][1].hasBomb());
        assertTrue(cells[1][1].hasBomb());
        for (int x = 0; x < cells.length; ++x) {
            for (int y = 0; y < cells[x].length; ++y) {
                assertFalse(cells[x][y].isUncovered());
            }
        }
    }

    @Test
    public void test0TestConstructor_2x2SuspectedOneBomb() {
        String field =
                "S ?\n" +
                "? ?B\n";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        Cell[][] cells = game.getCells();
        assertFalse(cells[0][0].hasBomb());
        assertFalse(cells[1][0].hasBomb());
        assertFalse(cells[0][1].hasBomb());
        assertTrue(cells[1][1].hasBomb());

        assertTrue(cells[0][0].isSuspected());
        assertFalse(cells[1][0].isUncovered());
        assertFalse(cells[0][1].isUncovered());
        assertFalse(cells[1][1].isUncovered());
    }

    @Test
    public void test0TestConstructor_2x2UncoveredOneBomb() {
        String field =
                "? ?\n" +
                "! ?B";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        Cell[][] cells = game.getCells();
        assertFalse(cells[0][0].hasBomb());
        assertFalse(cells[1][0].hasBomb());
        assertFalse(cells[0][1].hasBomb());
        assertTrue(cells[1][1].hasBomb());

        assertFalse(cells[0][0].isUncovered());
        assertFalse(cells[1][0].isUncovered());
        assertTrue(cells[0][1].isUncovered());
        assertFalse(cells[0][1].isSuspected());
        assertFalse(cells[1][1].isUncovered());
    }

    @Test
    public void test0GetNoOfAdjacentBombs_2x2_NoBombs() {
        String field =
                "? ?\n" +
                "? ?\n";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        assertEquals(0, game.getNoOfAdjacentBombs(0, 0));
        assertEquals(0, game.getNoOfAdjacentBombs(1, 0));
        assertEquals(0, game.getNoOfAdjacentBombs(0, 1));
        assertEquals(0, game.getNoOfAdjacentBombs(1, 1));

    }

    @Test
    public void test0GetNoOfAdjacentBombs_2x2_OneBomb() {
        String field =
                "? ?\n" +
                "? ?B\n";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        assertEquals(1, game.getNoOfAdjacentBombs(0, 0));
        assertEquals(1, game.getNoOfAdjacentBombs(1, 0));
        assertEquals(1, game.getNoOfAdjacentBombs(0, 1));
        assertEquals(0, game.getNoOfAdjacentBombs(1, 1));
    }


    @Test
    public void testTestConstructor_2x2_NoBombs_BombCountCorrect() {
        String field =
                "? ?\n" +
                "? ?";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        Cell[][] cells = game.getCells();
        assertEquals(0, cells[0][0].getNoOfAdjacentBombs());
        assertEquals(0, cells[1][0].getNoOfAdjacentBombs());
        assertEquals(0, cells[0][1].getNoOfAdjacentBombs());
        assertEquals(0, cells[1][1].getNoOfAdjacentBombs());
    }

    @Test
    public void testTestConstructor_2x2_OneBombs_BombCountCorrect() {
        String field =
                "? ?\n" +
                "? ?B";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        Cell[][] cells = game.getCells();
        assertEquals(1, cells[0][0].getNoOfAdjacentBombs());
        assertEquals(1, cells[1][0].getNoOfAdjacentBombs());
        assertEquals(1, cells[0][1].getNoOfAdjacentBombs());
        assertEquals(0, cells[1][1].getNoOfAdjacentBombs());
    }

    @Test
    public void testTestConstructor_2x2_TwoBombs_BombCountCorrect() {
        String field =
                "? ?B\n" +
                "? ?B";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        Cell[][] cells = game.getCells();
        assertEquals(2, cells[0][0].getNoOfAdjacentBombs());
        assertEquals(1, cells[1][0].getNoOfAdjacentBombs());
        assertEquals(2, cells[0][1].getNoOfAdjacentBombs());
        assertEquals(1, cells[1][1].getNoOfAdjacentBombs());
    }


    @Test
    public void test0AreValidCoords_2x2() {
        String field =
                "? ?B\n" +
                "? ?B";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        assertTrue(game.areValidCoords(1, 1));
        assertTrue(game.areValidCoords(1, 0));
        assertFalse(game.areValidCoords(2, 2));
        assertFalse(game.areValidCoords(-1, 0));
    }

    @Test
    public void test0AreValidCoords_4x4() {
        String field =
                "? ? ? ?\n" +
                "? ?B ? ?\n" +
                "? ? ? ?\n" +
                "? ? ? ?B";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        assertTrue(game.areValidCoords(3, 3));
        assertTrue(game.areValidCoords(1, 3));
        assertFalse(game.areValidCoords(4, 4));
        assertFalse(game.areValidCoords(-3, 0));
    }

    @Test
    public void testWon_2x2_Not() {
        String field =
                "? ?B\n" +
                "? ?B";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        assertFalse((game.won()));
    }

    @Test
    public void testWon_2x2_NoBombsMarkedCorrectly() {
        String field =
                "? ?\n" +
                "? ?";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        assertTrue((game.won()));
    }

    @Test
    public void testWon_2x2_NoBombsNotMarkedCorrectly() {
        String field =
                "S ?\n" +
                "? ?";
        Minesweeper game = new Minesweeper(new FakeGUI(), field);
        assertFalse((game.won()));
    }

}