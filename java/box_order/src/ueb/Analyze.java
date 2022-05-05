package ueb;

import static ueb.Data.*;

/**
 * Beinhalte Funktionen zur Daten Analyse aus Data
 *
 * Funktionen:
 *  - emptyBox -> initialisiert die Box und das gewicht.
 *  - produktFitsAt -> überprüft ein Produkt an einer gegebenen stelle.
 *  - getFreePositionToFit -> Findet, falls möglich die erste frei stell für ein Produkt.
 *  - putProductIntoBox -> legt das Produkt an die gefundene Stelle von getFreePositionToFit.
 *  - showBox -> druck die Box in die Konsole.
 *  - putIntoTheBox -> führt putProductIntoBox für jedes Produkt einer Bestellubng aus.
 *  - swap(int[]) -> Tauscht LEN und WID eines Produkts
 *  - swap(int[][]) -> Tauscht die Werte an zwei gegebenen Positionen
 *  - arrangeEveryProduct -> richtet jedes Produkt einer Besttellung nach LEN > WID aus
 *  - sortProductsByArea -> Sortiert die Produkte nach Fläche (LEN * WID)
 *  - getWeightOfBox -> getter für das Gewicht der Box
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class Analyze {

    private static final int EMPTY = -1; // Zahl für leeres Feld in der Box
    private static final String EMPTY_SIGN = "-"; // Char für leeres Feld in der Box
    private static final int BOX_LENGTH = getBoxDimensions()[LEN]; // Länge der Box
    private static final int BOX_WIDTH = getBoxDimensions()[WID]; // Breite der Box
    private static int remainingWeight = getBoxDimensions()[WT];
    private static int[][] box = new int[BOX_LENGTH][BOX_WIDTH];


    /**
     * Setzt jedes feld von box auf den Wert von EMPTY,
     * Setzt das Restgewicht zurück auf den Ursprungs Wert.
     */
    static void emptyBox() {
        for (int i = 0; i < BOX_LENGTH; i++) {
            for (int j = 0; j < BOX_WIDTH; j++) {
                box[i][j] = EMPTY;
            }
        }
        remainingWeight = getBoxDimensions()[WT];
    }

    /**
     * Liefert true, wenn das übergebene Produkt an der gegebenen Position (x, y) in die Box (in das Raster der Box)
     * passt. Die Routine liefert für ein Produkt, das dieselben Maße hat wie die Box, ausschließlich für die
     * Position 0,0 true.
     *
     * @param x zu überprüfende x Koordinate
     * @param y zu überprüfende y Koordinate
     * @param product Maße des zu setzenden Produkts
     * @return Ob das Product an der angegebenen Stelle Platz hat true = Ja, false = Nein
     */
    static boolean productFitsAt(int x, int y, int[] product) {
        boolean fits = !(x + product[LEN] > BOX_LENGTH ||  y + product[WID] > BOX_WIDTH);
        for (int i = x; i < x + product[LEN] && fits; i++) {
            for (int j = y; j < y + product[WID] && fits; j++) {
                fits = box[i][j] == EMPTY;
            }
        }
        return fits;
    }

    /**
     * liefert für das Produkt die erste freie Position; null, wenn das Produkt nicht passt.
     *
     *
     * @param product Maße des zu setzenden Produkts
     * @return Die erste frei Stell für das Produkt wenn kein Platz -> null
     */
    static int[] getFreePositionToFit(int[] product) {
        int[] pos = null;

        if (remainingWeight >= product[WT]) {
            for (int i = 0; i < BOX_LENGTH && pos == null; i++) {
                for (int j = 0; j < BOX_WIDTH && pos == null; j++) {
                    if (productFitsAt(i, j, product)) {
                        pos = new int[]{i, j};
                    }
                }
            }
        }
        return pos;
    }

    /**
     * Wenn das Produkt von Umfang und Gewicht in die Box passt, wird dieses in die Box gelegt und true
     * zurückgegeben; andernfalls wird false zurückgegeben und die Box bleibt unverändert.
     *
     * @param product Maße des zu setzenden Produkts
     * @return ob das Produkt in die Box gepackt wurde true = Ja, false = Nein
     */
    static boolean putProductIntoBox(int[] product) {
        boolean in = false;
        int[] pos = getFreePositionToFit(product);

        if (pos != null) {
            in = true;
            remainingWeight -= product[WT];
            for (int i = pos[0]; i < pos[0] + product[LEN]; i++) {
                for (int j = pos[1]; j < pos[1] + product[WID]; j++) {
                    box[i][j] = product[ID];
                }
            }
        }

        return in;
    }

    /**
     * Gibt eine Darstellung der Box aus. Die Länge ist dabei horizontal darzustellen, die Breite vertikal.
     */
    static public void showBox() {
        for (int i = 0; i < BOX_WIDTH; i++) {
            for (int j = 0; j < BOX_LENGTH; j++) {
                if (box[j][i] == EMPTY) {
                    System.out.printf("%-2s", EMPTY_SIGN);
                } else {
                    System.out.printf("%-2d", box[j][i]);
                }
            }
            System.out.printf("%n");
        }
    }

    /**
     * legt jedes Produkt der übergebenen Liste in der gegebenen Reihenfolge und Orientierung in die Box, sofern es
     * reinpasst, und liefert die Anzahl der untergebrachten Produkte
     *
     * @param products liste mit Maßen der zu setzenden Produkte
     * @return Anzahl der Prokukt die in die Box gelegt wurden
     */
    public static int putIntoTheBox(int[][] products) {
        emptyBox();
        int count = 0;
        for (int[] product : products) {
            count += putProductIntoBox(product) ? 1 : 0;
        }
        return count;
    }

    /**
     * Tauscht zwei elemente einer Liste miteinander
     *
     * @param list liste mit zu tauschenden Werten
     */
    private static void swap(int[] list) {
        int tmp = list[LEN];
        list[LEN] = list[WID];
        list[WID] = tmp;
    }

    /**
     * Gleiche Funktion mit 2D Array
     */
    private static void swap(int[][] list, int first, int second) {
        int[] tmp = list[first];
        list[first] = list[second];
        list[second] = tmp;
    }

    /**
     * überprüft jedes Produkt in der Liste und ändert gegebenenfalls seine Orientierung, sodass immer die Länge
     * größer als oder gleich der Breite ist
     *
     * @param products liste mit Maßen der zu setzenden Produkte
     */
    public static void arrangeEveryProduct(int[][] products) {
        for (int[] product : products) {
            if (product[LEN] < product[WID]) {
                swap(product);
            }
        }
    }

    /**
     * sortiert die Produkte in der Liste (BubbleSort -> langsam) so, dass die Produkte der Fläche nach absteigend
     * vorliegen
     *
     * @param products liste mit Maßen der zu setzenden Produkte
     */
    public static void sortProductsByArea(int[][] products) {
        for (int i = products.length; i > 1; i--) {
            for (int j = 0; j < i-1; j++) {
                if (products[j][LEN] * products[j][WID] < products[j+1][LEN] * products[j+1][WID]) {
                    swap(products, j+1, j);
                }
            }
        }
    }

    /**
     * Gibt die Summe der Gewichte aller Produkt in der Box aus
     *
     * @return Das Gewicht aller Produkte in der Box
     */
    public static int getWeightOfBox() {
        return getBoxDimensions()[WT] - remainingWeight;
    }

}
