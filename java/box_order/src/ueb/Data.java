package ueb;

/**
 * enthält die zu analysierenden Daten:
 * private Konstanten:
 * - die Maße einer Box mit Länge, Breite und Maximalgewicht
 * - eine Liste von Bestellungen,
 *   jede Bestellung enthält eine Liste von Produkten,
 *   jedes Produkt enthält eine Liste von Maßen
 *
 * öffentliche Konstanten bestimmen die Reihenfolge der Maße in einer Produktangabe
 *
 * öffentliche Getter
 * - die Maße einer Box
 * - die Anzahl der Bestellungen
 * - die Produktliste einer Bestellung
 *
 * @author Gerit und Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class Data {

    /** bestimmt den Index der entsprechenden Information im Array eines Produkts */
    public static final int LEN = 0;
    public static final int WID = 1;
    public static final int WT  = 2;
    public static final int ID  = 3;

    /** Maße einer Box Länge[cm], Breite[cm], Maximalgewicht[g] */
    private static final int[] BOX_DIMENSIONS = {10, 5, 1000};

    /**
     * Array aus Bestellungen, jede Bestellung enthält ein Array von Produkten,
     * jedes Produkt besteht aus einem Array seiner Maßangaben
     * Länge[cm], Breite[cm], Gewicht[g], ID
     */
    private static final int[][][] ORDERS = {
        {/* {Länge, Breite, Gewicht, ID */
            { 1,  1,   25, 1},
            { 2,  3,  150, 2},
            {10,  1,  250, 3},
            { 3,  5,  375, 4},
            { 3,  2,  150, 5}
        },
        {   { 2,  2,   40, 0},
            { 2,  2,   40, 1},
            { 8,  1,   80, 2}, //nicht in P3
            { 2,  5,  100, 3},
            { 6,  2,  100, 4},
            { 2,  1,  200, 5},
            { 1,  2,  200, 6}
        },
        {   { 2,  1,  500, 0}, //passt als Einziges in P1, P2
            { 1,  1,  600, 1}, //passt nirgends (zu schwer, wenn 0 bereits enthalten)
            { 5, 10,  200, 2}, //passt nie
            {10,  5,  500, 3}  //passt in P3, dann alleine
        }        
    };

    /**
     * Liefert die Maße einer Box.
     *
     * @return die Maße einer Box
     */
    public static int[] getBoxDimensions() {
        return BOX_DIMENSIONS.clone();
    }

    /**
     * Liefert die Anzahl der Bestellungen.
     *
     * @return die Anzahl der Bestellungen
     */
    public static int getCountOfOrders() {
        return ORDERS.length;
    }

    /**
     * Liefert eine tiefe(!) Kopie der Produktliste einer Bestellung.
     * Ist der Index nicht gültig, wird eine IllegalArgumentException ausgelöst.
     *
     * @param idx Index der Bestellung
     * @return eine Kopie der Produktliste einer Bestellung;
     * @throws IllegalArgumentException wenn der Index ungültig ist
     */
    public static int[][] getOrder(int idx) {
        if (idx < 0 || idx >= getCountOfOrders()) {
            throw new IllegalArgumentException("Ungültiger Index " + idx);
        }
        int[][] copy = new int[ORDERS[idx].length][];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = ORDERS[idx][i].clone();
        }

        return copy;
    }
}
