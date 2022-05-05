
/**
 * Programmstruckturen 2 Übung Aufgabe 1, Thema: Einführung, Verzweigungen, Schleifen, statische Methoden
 * 05.05.2021 Tobias Schrock (inf104926) und Konstantin Opora (inf104952) --- ps2_84
 * https://www.fh-wedel.de/
 */


public class Main {

    /**
     * Liefert die anzahlder Ziffer einer Zahl (inkusive - als Vorzeichen).
     *
     * @param num die Zahl, von der eine länge ermittelt werden soll
     * @return die Anzahl der Ziffern in num
     */
    private static int getCountOfDigits(int num) {
        int count = 0;
        if (num < 0) {
            num *= -1;
            count++;
        }
        while (num > 0) {
            count++;
            num /= 10;
        }
        return count;
    }

    /**
     * Überprüft die Zahl auf die Sequenz 666.
     *
     * @param num die Zahl, die überprüft werde soll
     * @return Ob es die Sequenz gefunden hat
     * true: ja
     * false: nein
     */
    private static boolean hasDevil(int num) {
        int count = 0;
        if (num < 0) {
            num *= -1;
        }
        while (num > 0 && count < 3) {
            if (num % 10 == 6) {
                count++;
            } else {
                count = 0;
            }
            num /= 10;
        }
        return (count == 3);
    }

    /**
     * Gibt an ob eine Zahl Prim ist.
     *
     * @param num die Zahl, die überprüft werde soll
     * @return Ob die Zahl Prim ist,
     * true: ja
     * false: nein
     */
    private static boolean isPrim(int num) {

        boolean prim = num > 1;

        for (int i = 2; i <= num / 2; i++) {
            if (num % i == 0) {
                prim = false;
            }
        }
        return prim;
    }

    /**
     * Gibt die Primzahl an welche direkt vor der Zahl liegt.
     *
     * @param num die Zahl, die als Referenz gilt
     * @return die vorige Primzahl
     */
    private static int lastPrim(int num) {
        int lowerPrim = 0, i = 1;
        while (i < num && lowerPrim == 0) {
            if (isPrim(num - i)) {
                lowerPrim = num - i;
            }
            i++;
        }
        return lowerPrim;
    }

    /**
     * Gibt die Primzahl an welche direkt nach der Zahl liegt.
     *
     * @param num die Zahl, die als Referenz gilt
     * @return die folgende Primzahl
     */
    private static int nextPrim(int num) {
        int higherPrim = 0, i = 1;
        while (higherPrim == 0) {
            if (isPrim(num + i)) {
                higherPrim = num + i;
            }
            i++;
        }
        return higherPrim;
    }

    /**
     * Gibt an ob die Zahl eine "gute" Primzahl ist.
     *
     * @param num die Zahl, die überprüft werde soll
     * @return Ob die Zahl eine "gute" Primzahl ist,
     * true: ja
     * false: nein
     */
    private static boolean isGoodPrim(int num) {
        return num > 2 && isPrim(num) && (num * num > lastPrim(num) * nextPrim(num));
    }

    /**
     * Gibt eine String Repräsentation der Zahl in binär wieder
     *
     * @param num die Zahl, in Binär angegeben werden soll
     * @return Einen String in der Binärzahl
     */
    private static StringBuilder toBitString(int num) {
        StringBuilder seq = new StringBuilder();
        boolean trigger = false;
        int bit;

        for (int i = Integer.SIZE - 1; i >= 0; i--) { 

            bit = (num >> i) & 1;
            if (trigger || bit == 1) {
                seq.append(bit == 1 ? "1" : "0");
                if (i % 8 == 0 && i != 0) {
                    seq.append("_");
                }
                trigger = true;
            }
        }
        if (!trigger) {
            seq.append(0);
        }
        return seq;
    }

    /**
     * Wandelt jeden Bit einer Zahl (32 bit), außer die führenden Nullen, in Einsen umwandelt
     *
     * @param num die Zahl, von welche wir ausgehen
     * @return die Zahl mit ausschließlich Einsen in den Bits
     */
    private static int setToOne(int num) {
        num |= (num >> 1);
        num |= (num >> 2);
        num |= (num >> 4);
        num |= (num >> 8);
        num |= (num >> 16);
        return num;
    }

    /**
     * Sucht in einer Zahl nach einer Bitsequenz
     *
     * @param num die Zahl, in der gesucht wird
     * @param seq die Sequenz, welche gesucht wird
     * @return Ob die Sequenz gefunden wurde
     * true: ja
     * false: nein
     */
    private static boolean findBitSeq(int num, int seq) {

        int activator = setToOne(seq);
        boolean found = false;

        for (int i = 0; i < Integer.SIZE - 1; i++) {
            if (((num >> i) & activator) == seq) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Hauptteil: Gibt für die angegebenen Limits und Sequenz alle Zahlen aus die mindestens
     * eine der drei Bedingungen erfüllt:
     *  hasDevil, isGoodPrim, findBitSeq
     *
     * @param args constolen Argumente
     */
    public static void main(String[] args) {

        final int START = -8, END = 10, SEQ = 0b11111111111111111111111111111;

        final int START_DIG_COUNT = getCountOfDigits(START);
        final int END_DIG_COUNT = getCountOfDigits(END);
        final int MAX_DIG_COUNT = END_DIG_COUNT > START_DIG_COUNT ? END_DIG_COUNT : START_DIG_COUNT;

        System.out.println(
                "Prüfung von " + START + " bis " + END + " auf die Sequenz " + toBitString(SEQ)
                        + ":");
        boolean devil, gPrim, bSeq;

        for (int i = START; i <= END; i++) {
            devil = hasDevil(i);
            gPrim = isGoodPrim(i);
            bSeq = findBitSeq(i, SEQ);
            if (devil || gPrim || bSeq) {
                System.out.printf("%" + MAX_DIG_COUNT + "d: " + "%-15s" + "%-30s" + "%s%n", i,
                        (devil ? "Teufelszahl" : ""),
                        (gPrim ? "gute Primzahl " + lastPrim(i) + "/" + nextPrim(i) : ""), (bSeq ?
                                "enthält Bitsequenz (" + toBitString(i) + " <- " + toBitString(SEQ)
                                        + ")" :
                                ""));
            }
        }
    }
}
