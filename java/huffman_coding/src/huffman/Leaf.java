package huffman;


import coding.Bit;
import coding.BitChain;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * Ein Huffman-Blatt.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
class Leaf extends Tree {
    /**
     * Gesamtzahl des Symbols
     */
    private final long count;
    /**
     * Symbol das repräsentiert wird
     */
    private final int b;

    /**
     * Konstruktor für ein Blatt
     *
     * @param b     Symbol
     * @param count Anzahl
     */
    Leaf(int b, long count) {
        this.count = count;
        this.b = b;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getByte() {
        return this.b;
    }

    @Override
    public long getCount() {
        return this.count;
    }

    @Override
    public BitChain[] toCodetable() {
        BitChain elem = new BitChain(this.getBitChain(), getByte());
        BitChain[] result = new BitChain[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
        result[0] = elem;
        return result;
    }

    @Override
    protected void toGraphviz(StringBuilder sb, String prefix) {
        assert sb != null;
        assert prefix != null;
        sb.append(prefix);
        sb.append(" [label=\"");
        sb.append(byteToLabel(this.b));
        sb.append(" (");
        sb.append(this.count);
        sb.append(")\"]\n");
    }

    @Override
    public void translate(Iterator<Bit> iterator, long numberOfBytes,
                          OutputStream destination) throws IOException {
        if (iterator == null) {
            throw new IllegalArgumentException("Iterator darf nicht null sein");
        }
        if (numberOfBytes < 0) {
            throw new IllegalArgumentException("Zu schreibende Anzahl muss größer/gleich 0 sein");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Zielstrom darf nicht null sein");
        }
        //Schreibt Byte in den Ausgabestrom
        for (int i = 0; i < numberOfBytes; i++) {
            destination.write(getByte());
        }
    }

    @Override
    public boolean isInvariant() {
        return true;
    }

}
