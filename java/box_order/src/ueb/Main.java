package ueb;

import static ueb.Data.*;
import static ueb.Analyze.*;

/**
 * Programmstruckturen 2 Aufgabe 2, Thema: Arrays, Klassen, Referenzdatentypen, Testen mit JUnit
 * @website https://www.fh-wedel.de/
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952) --- ps2_84
 */
public class Main {

    /**
     * Wendet putIntoTheBox mit den angegebenden Produkten an und gibt die Anzahl der erfolgreich in die box gepackten
     * Prdukten sowie das Gewicht der Box aus.
     * Zeigt ebenfalls die gefüllte Box.
     *
     * @param products liste der Produkte
     */
    private static void boxInfo(int[][] products) {

        emptyBox();
        int count = putIntoTheBox(products);
        int weight = getWeightOfBox();
        showBox();
        System.out.printf("%d Produkte untergebracht, Packgewicht: %.2fkg.%n", count, (float)weight / 1000);
    }


    /**
     * erstellt eine tiefe Kopie eines 2D Arrays
     *
     * @param list zu kopierende Liste
     * @return die Kopie
     */
    private static int[][] deepCopy2D(int[][] list) {
        int[][] copy = list.clone();
        for (int i = 0; i < copy.length; i++) {
            copy[i] = list[i].clone();
        }
        return copy;
    }

    /**
     * Körper des Programms : geht Sämtliche Produkte in den Data.ORDERS durch und legt diese in die vordefiniert Box
     * einmal auf normale weiße
     * einmal mit arrangierten Produkten
     * einmal mit nach Fläche sortierten Produkten
     *
     * @param args Konsolen Argumente
     */
    public static void main(String[] args) {

        int[] dim = getBoxDimensions();

        System.out.printf("Gefüllt wird eine Box mit der Länge %dcm und der Breite %dcm.%nZulässiges Gesamtgewicht" +
                " ist %.2fkg.%n%s%n", dim[LEN], dim[WID], (float) dim[WT] / 1000, "*".repeat(46));

        int[][] temp;
        for (int i = 0; i < getCountOfOrders(); i++) {
            System.out.printf("%nBestellung %d mit unveränderten Produkten:%n", i+1);
            boxInfo(getOrder(i));

            System.out.printf("%nBestellung %d mit arrangierten Produkten:%n", i+1);
            temp = deepCopy2D(getOrder(i));
            arrangeEveryProduct(temp);
            boxInfo(temp);

            System.out.printf("%nBestellung %d mit nach Fläche sortierten Produkten:%n", i+1);
            temp = deepCopy2D(getOrder(i));
            sortProductsByArea(temp);
            boxInfo(temp);

        }

        System.out.println("*".repeat(46));
    }

}
