package ueb;

import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class PubDataTest {

    @Test
    public void getBoxDimensions() {
        assertArrayEquals(new int[]{10, 5, 1000}, Data.getBoxDimensions());
    }

    @Test
    public void getBoxDimensions_deepCopy() {
        int[] dim = Data.getBoxDimensions();            // Kopie holen
        int orgValue = dim[0];                          // Originalwert merken
        dim[0] = 999;                                   // Wert in der Kopie ändern
        int nowInConstant = Data.getBoxDimensions()[0]; // was steht jetzt als Wert in der Konstante?
        dim[0] = orgValue;                              // für folgende Tests wieder herstellen, falls keine tiefe Kopie gemacht wurde
        assertEquals(orgValue, nowInConstant);
    }

    @Test
    public void getCountOfOrders() {
        assertEquals(3, Data.getCountOfOrders());
    }

    @Test
    public void getOrder0() {
        assertTrue(Arrays.deepEquals(new int[][]
                {  { 1,  1,   25, 1},
                   { 2,  3,  150, 2},
                   {10,  1,  250, 3},
                   { 3,  5,  375, 4},
                   { 3,  2,  150, 5}
                }, Data.getOrder(0)));
    }

    @Test
    public void getOrder1() {
        assertTrue(Arrays.deepEquals(new int[][]
                {  { 2,  2,   40, 0},
                   { 2,  2,   40, 1},
                   { 8,  1,   80, 2},
                   { 2,  5,  100, 3},
                   { 6,  2,  100, 4},
                   { 2,  1,  200, 5},
                   { 1,  2,  200, 6}
                }, Data.getOrder(1)));
    }

    @Test
    public void getOrder2() {
        assertTrue(Arrays.deepEquals(new int[][]
                {   { 2,  1,  500, 0},
                    { 1,  1,  600, 1},
                    { 5, 10,  200, 2},
                    {10,  5,  500, 3}
                }, Data.getOrder(2)));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getOrder_InvalidIndexNegative() {
        int[][] unused = Data.getOrder(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getOrder_InvalidIndexTooBig() {
        int[][] unused = Data.getOrder(3);
    }

    @Test
    public void getOrder_deepCopy() {
        // wenn tiefe Kopie gemacht wurde, wird Originalwert beibehalten
        int[][] copy = Data.getOrder(0);            // Kopie holen
        int orgValue = copy[0][0];                      // Originalwert merken
        copy[0][0]   = 999;	                            // Wert in der Kopie ändern
        int nowInConstant = Data.getOrder(0)[0][0]; // was steht jetzt als Wert in der Konstante?
        copy[0][0]   = orgValue;                        // für folgende Tests wieder herstellen, falls keine tiefe Kopie gemacht wurde
        assertEquals(orgValue, nowInConstant);
    }
}