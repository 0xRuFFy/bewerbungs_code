package coding;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Eine Kette von aneinandergereihten {@link Bit}s.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
public class BitChain {
    /**
     * Liste der Bits
     */
    private final LinkedList<Bit> list;
    /**
     * Symbol
     */
    private int symbol;

    /**
     * Konstruktor für eine Bitchain
     */
    public BitChain() {
        this.list = new LinkedList<>();
    }

    /**
     * Konstruktor für Bitchain mit Symbol
     *
     * @param bitChain Bitchain
     * @param symbol   Symbol, für das die Bitchain steht
     */
    public BitChain(BitChain bitChain, int symbol) {
        this();
        this.append(bitChain);
        this.symbol = symbol;
    }

    /**
     * Hängt das übergebene Bit hinten an diese Kette an.
     *
     * @param bit Das anzuhängende Bit
     */
    public void append(Bit bit) {
        list.add(bit);
    }

    /**
     * Hängt die Bits der übergebenen BitChain hinten an diese Kette an.
     *
     * @param other Die BitChain mit den anzuhängenden Bits, wird nicht verändert
     * @pre other != null
     */
    public void append(BitChain other) {
        assert other != null;
        list.addAll(other.copy().list);
    }

    /**
     * Erzeugt eine Kopie des aktuellen Objekts, welche die gleichen Bits enthält. Änderungen der
     * Kopie dürfen keine Auswirkung auf das aktuelle Objekt haben.
     *
     * @return Kopie des aktuellen Objekts
     */
    public BitChain copy() {
        BitChain result = new BitChain();
        result.list.addAll(this.list);

        return result;
    }

    /**
     * Gibt die Länge dieser BitChain zurück.
     *
     * @return Länge, also die Anzahl der Bits in dieser Kette
     */
    public int length() {
        return this.list.size();
    }

    /**
     * Schreibt die Bits dieser BitChain byteweise in den übergebenen Datenstrom und entfernt
     * geschriebene Bits aus dieser BitChain. Bits, die nicht mehr in ein Byte passen (also weniger
     * als Byte.SIZE), verbleiben in dieser BitChain.
     * <p>
     * Diese Methode muss zwingend Bitoperationen zum Zusammenbauen eines Bytes verwenden.
     *
     * @param output Zu nutzender Datenstrom, darf nicht null sein
     * @throws IOException Fehler beim Schreiben
     * @pre output != null
     * @post length() &lt; Byte.SIZE
     */
    public void writeTo(OutputStream output) throws IOException {
        assert output != null;
        while (length() >= Byte.SIZE) {
            int result = 0;
            for (int i = 0; i < Byte.SIZE; i++) {
                Bit bit = this.list.poll();
                if (bit != null) {
                    // Zusammenbauen eines Byte
                    result = result | bit.getValue() << Byte.SIZE - 1 - i;
                }
            }
            // Schreiben in Output
            output.write((byte) result);
        }
        assert length() < Byte.SIZE;
    }

    // Diese Methoden sind Hilfsmethoden zum Testen und dürfen auch nur in Tests verwendet werden:

    /**
     * Konvertiert diese BitChain in ein Array. Die Reihenfolge der enthaltenen Bits wird
     * beibehalten.
     *
     * @return Das der BitChain entsprechende Array
     */
    public Bit[] toArray() {
        return this.list.toArray(new Bit[0]);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BitChain)) {
            return false;
        }
        BitChain other = (BitChain) obj;

        // Um in dieser Klasse die konkrete Implementierung nicht vorzuschreiben,
        // wird der ineffiziente Aufruf der Methode toArray() genutzt:
        return Arrays.deepEquals(this.toArray(), other.toArray());
    }

    @Override
    public int hashCode() {
        // Um in dieser Klasse die konkrete Implementierung nicht vorzuschreiben,
        // wird der ineffiziente Aufruf der Methode toArray() genutzt:
        return Arrays.hashCode(this.toArray());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Um in dieser Klasse die konkrete Implementierung nicht vorzuschreiben,
        // wird der ineffiziente Aufruf der Methode toArray() genutzt:
        for (Bit b : this.toArray()) {
            sb.append(b);
        }

        return sb.toString();
    }

    public int getSymbol() {
        return symbol;
    }
}
