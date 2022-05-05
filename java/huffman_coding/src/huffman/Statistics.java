package huffman;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Eine Statistik, die für alle 256 möglichen Symbole deren Häufigkeit enthält. Die Einträge sind
 * aufsteigend nach dem Ordinalwert des Symbols sortiert.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888.
 */
public class Statistics {

    /**
     * Array mit 256 Einträgen zur Häufigkeit des jeweiligen Bytes
     */
    private final long[] haeufigkeit = new long[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];

    /**
     * Erstellt eine Statistik aus den übergebenen Bytes.
     *
     * @param input Bytes, aus welchen die Statistik erstellt werden soll, darf nicht null sein.
     * @return Die Statistik mit 256 Einträgen
     */
    public static Statistics analyse(byte[] input) {
        if (input == null) {
            throw new IllegalArgumentException("input darf nicht null sein");
        }
        Statistics result = new Statistics();
        for (byte b : input) {
            result.haeufigkeit[Byte.toUnsignedInt(b)]++;
        }
        return result;
    }

    /**
     * Liest die Statistik aus dem übergebenen Datenstrom und gibt sie zurück.
     * <p>
     * Die Statistik besteht aus 256 Long-Werten.
     *
     * @param input Der zu nutzende Datenstrom, darf nicht null sein. Steht zu Beginn der Methode
     *              auf dem ersten Statistikeintrag und nach ihrem Ende auf dem ersten Byte nach dem
     *              letzten Statistikeintrag.
     * @return Die gelesene Statistik
     * @throws IOException Fehler beim Lesen
     */
    public static Statistics readFrom(DataInput input) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("input darf nicht null sein");
        }
        Statistics result = new Statistics();
        for (int i = 0; i < result.haeufigkeit.length; i++) {
            result.haeufigkeit[i] = input.readLong();
        }
        return result;
    }

    /**
     * Schreibt die Statistik in den übergebenen Datenstrom.
     *
     * @param output Der zu nutzende Datenstrom, darf nicht null sein
     * @throws IOException Fehler beim Schreiben
     */
    public void writeTo(DataOutput output) throws IOException {
        if (output == null) {
            throw new IllegalArgumentException("output darf nicht null sein");
        }
        for (long l : haeufigkeit) {
            output.writeLong(l);
        }
    }

    /**
     * Erstellt einen {@link Forest} aus dieser Statistik. Der Forest enthält nur einelementige
     * Bäume (Blätter) für Bytes, deren Statistikeintrag mindestens 1 ist.
     *
     * @return Der Wald mit einelementigen Bäumen
     */
    public Forest toForest() {
        Forest result = new Forest();
        for (int i = 0; i < haeufigkeit.length; i++) {
            if (haeufigkeit[i] > 0) {
                result.insert(new Leaf(i, haeufigkeit[i]));
            }
        }
        return result;
    }

    /**
     * Gibt den Statistikeintrag an der übergebenen Position zurück.
     *
     * @param pos Position des Eintrags, muss zwischen 0 und einschließlich 255 liegen
     * @return Der Statistikeintrag
     */
    public long getValue(int pos) {
        if (pos < 0 || pos > Byte.MAX_VALUE - Byte.MIN_VALUE) {
            throw new IllegalArgumentException("Pos muss zwischen 0 und einschließlich 255 liegen");
        }
        return haeufigkeit[pos];
    }

    /**
     * Liefert die Summe der Statistikwerte zurück. Überläufe des Datentyps long werden hierbei
     * ignoriert.
     *
     * @return Die Summe der Statistikwerte
     */
    public long sum() {
        long sum = 0;
        for (long l : haeufigkeit) {
            sum += l;
        }
        return sum;
    }
}
