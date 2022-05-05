package ueb;

import org.junit.Test;

import static org.junit.Assert.*;
import static ueb.Data.*;

public class PubAnalyzeTest {

    //<editor-fold desc="productFitsAt">
    @Test
    public void productFitsAt_FillsComplete() {
        Analyze.emptyBox();
        int[] box = Data.getBoxDimensions();
        int[] product = new int[]{box[LEN], box[WID], 1000, 0};
        assertTrue(Analyze.productFitsAt(0, 0, product));
    }

    @Test
    public void productFitsAt_TooBig() {
        Analyze.emptyBox();
        int[] box = Data.getBoxDimensions();

        int[] product = new int[]{box[LEN] + 1, box[WID], 1000, 0};
        assertFalse(Analyze.productFitsAt(0, 0, product));

        product = new int[]{box[LEN], box[WID] + 1, 1000, 0};
        assertFalse(Analyze.productFitsAt(0, 0, product));

        product = new int[]{box[LEN], box[WID] , 1001, 0};
        assertTrue(Analyze.productFitsAt(0, 0, product)); //Gewicht irrelevant
    }

    @Test
    public void productFitsAt_BottomRight() {
        Analyze.emptyBox();
        int[] box = Data.getBoxDimensions();
        int[] product = new int[]{1, 1, 1000, 0};
        assertTrue(Analyze.productFitsAt(box[LEN]-1, box[WID]-1, product));
    }

    @Test
    public void productFitsAt_Not() {
        Analyze.emptyBox();
        int[] box = Data.getBoxDimensions();
        int[] product = new int[]{2, 2, 1000, 0};
        assertFalse(Analyze.productFitsAt(box[LEN]-1, box[WID]-1, product));
    }
    //</editor-fold>

    //<editor-fold desc="getFreePositionToFit">
    // nur mit leerer Box testbar, da die ein Attribut der Klasse ist
    @Test
    public void getFreePositionToFit_FillsComplete() {
        Analyze.emptyBox();
        int[] box = Data.getBoxDimensions();
        int[] product = new int[]{box[LEN], box[WID], 1000, 0};
        assertArrayEquals(new int[]{0, 0}, Analyze.getFreePositionToFit(product));
    }

    @Test
    public void getFreePositionToFit_SmallThing() {
        Analyze.emptyBox();
        int[] product = new int[]{1, 1, 1, 0};
        assertArrayEquals(new int[]{0, 0}, Analyze.getFreePositionToFit(product));
    }

    @Test
    public void getFreePositionToFit_TooBig() {
        Analyze.emptyBox();
        int[] box = Data.getBoxDimensions();
        int[] product = new int[]{box[LEN] + 1, box[WID] + 1, 1000, 0};
        assertNull(Analyze.getFreePositionToFit(product));
    }
    //</editor-fold>

    //<editor-fold desc="putIntoTheBox">
    @Test
    public void putIntoTheBox0() {
        int[][] order = Data.getOrder(0);
        assertEquals(4, Analyze.putIntoTheBox(order));
    }

    @Test
    public void putIntoTheBox1() {
        int[][] order = Data.getOrder(1);
        assertEquals(7, Analyze.putIntoTheBox(order));
    }

    @Test
    public void putIntoTheBox2() {
        int[][] order = Data.getOrder(2);
        assertEquals(1, Analyze.putIntoTheBox(order));
    }
    //</editor-fold>

    //<editor-fold desc="arrangeEveryProduct">
    @Test
    public void arrangeEveryProduct0() {
        int[][] order = Data.getOrder(0);
        Analyze.arrangeEveryProduct(order);
        assertArrayEquals(new int[]{ 1,  1,   25, 1}, order[0]);
        assertArrayEquals(new int[]{ 3,  2,  150, 2}, order[1]);
        assertArrayEquals(new int[]{10,  1,  250, 3}, order[2]);
        assertArrayEquals(new int[]{ 5,  3,  375, 4}, order[3]);
        assertArrayEquals(new int[]{ 3,  2,  150, 5}, order[4]);
    }

    @Test
    public void arrangeEveryProduct1() {
        int[][] order = Data.getOrder(1);
        Analyze.arrangeEveryProduct(order);
        assertArrayEquals(new int[] { 2,  2,   40, 0}, order[0]);
        assertArrayEquals(new int[] { 2,  2,   40, 1}, order[1]);
        assertArrayEquals(new int[] { 8,  1,   80, 2}, order[2]);
        assertArrayEquals(new int[] { 5,  2,  100, 3}, order[3]);
        assertArrayEquals(new int[] { 6,  2,  100, 4}, order[4]);
        assertArrayEquals(new int[] { 2,  1,  200, 5}, order[5]);
        assertArrayEquals(new int[] { 2,  1,  200, 6}, order[6]);
    }

    @Test
    public void arrangeEveryProduct2() {
        int[][] order = Data.getOrder(2);
        Analyze.arrangeEveryProduct(order);
        assertArrayEquals(new int[] { 2,  1,  500, 0}, order[0]);
        assertArrayEquals(new int[] { 1,  1,  600, 1}, order[1]);
        assertArrayEquals(new int[] {10,  5,  200, 2}, order[2]);
        assertArrayEquals(new int[] {10,  5,  500, 3}, order[3]);
    }
    //</editor-fold>

    //<editor-fold desc="sortProductsByArea">
    @Test
    public void sortProductsByArea0() {
        int[][] order = Data.getOrder(0);
        Analyze.sortProductsByArea(order);
        assertArrayEquals(new int[]{ 3,  5,  375, 4}, order[0]);
        assertArrayEquals(new int[]{10,  1,  250, 3}, order[1]);
        assertArrayEquals(new int[]{ 2,  3,  150, 2}, order[2]);
        assertArrayEquals(new int[]{ 3,  2,  150, 5}, order[3]);
        assertArrayEquals(new int[]{ 1,  1,   25, 1}, order[4]);
    }

    @Test
    public void sortProductsByArea1() {
        int[][] order = Data.getOrder(1);
        Analyze.sortProductsByArea(order);
        assertArrayEquals(new int[] { 6,  2,  100, 4}, order[0]);
        assertArrayEquals(new int[] { 2,  5,  100, 3}, order[1]);
        assertArrayEquals(new int[] { 8,  1,   80, 2}, order[2]);
        assertArrayEquals(new int[] { 2,  2,   40, 0}, order[3]);
        assertArrayEquals(new int[] { 2,  2,   40, 1}, order[4]);
        assertArrayEquals(new int[] { 2,  1,  200, 5}, order[5]);
        assertArrayEquals(new int[] { 1,  2,  200, 6}, order[6]);
    }

    @Test
    public void sortProductsByArea2() {
        int[][] order = Data.getOrder(2);
        Analyze.sortProductsByArea(order);
        assertArrayEquals(new int[] { 5, 10,  200, 2}, order[0]);
        assertArrayEquals(new int[] {10,  5,  500, 3}, order[1]);
        assertArrayEquals(new int[] { 2,  1,  500, 0}, order[2]);
        assertArrayEquals(new int[] { 1,  1,  600, 1}, order[3]);
    }
    //</editor-fold>

}