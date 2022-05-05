package huffman;


import coding.Bit;
import coding.BitChain;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Ein (innerer) Huffman-Knoten.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
class Node extends Tree {

    /**
     * Gesamtzahl der repräsentierten Bytes des Baums
     */
    private final long count;
    /**
     * Linker Unterbaum
     */
    private final Tree leftT;
    /**
     * Rechter Unterbaum
     */
    private final Tree rightT;

    /**
     * Konstruktor für einen Knoten
     *
     * @param l Linker Unterbaum
     * @param r Rechter Unterbaum
     */
    Node(Tree l, Tree r) {
        this.leftT = l;
        this.rightT = r;

        this.count = l.getCount() + r.getCount();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getByte() {
        throw new IllegalArgumentException();
    }

    @Override
    public long getCount() {
        return this.count;
    }

    @Override
    public BitChain[] toCodetable() {
        // Kodierung für Unterbäume ergänzen
        leftT.getBitChain().append(this.getBitChain());
        rightT.getBitChain().append(this.getBitChain());
        leftT.getBitChain().append(Bit.ZERO);
        rightT.getBitChain().append(Bit.ONE);


        BitChain[] result = new BitChain[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
        BitChain[] left = leftT.toCodetable();
        BitChain[] right = rightT.toCodetable();
        int lengthl = 0, lengthr = 0;
        while (left[lengthl] != null) {
            lengthl++;
        }
        while (right[lengthr] != null) {
            lengthr++;
        }

        //Ergebnis Zusammenbauen
        System.arraycopy(left, 0, result, 0, lengthl);
        System.arraycopy(right, 0, result, lengthl, lengthr);

        //Sortieren
        Arrays.sort(result, (o1, o2) -> {
            if (o1 == null || o2 == null) {
                return 1;
            } else {
                return o1.toString().compareTo(o2.toString());
            }
        });
        return result;
    }


    @Override
    protected void toGraphviz(StringBuilder sb, String prefix) {
        assert sb != null;
        assert prefix != null;
        // Graphizdarstellung von dem Knoten
        sb.append(prefix).append(" [label=\"(").append(this.count).append(")\"]\n");
        // Graphizdarstellung zur Weiterführung nach links
        sb.append(prefix).append(" -> ").append(prefix).append("0").append(" [label=\"0\"]\n");
        // Graphizdarstellung zur Weiterführung nach rechts
        sb.append(prefix).append(" -> ").append(prefix).append("1").append(" [label=\"1\"]\n");

        leftT.toGraphviz(sb, prefix + "0");
        rightT.toGraphviz(sb, prefix + "1");

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
        for (int i = 0; i < numberOfBytes; i++) {
            if (iterator.next() == Bit.ZERO) {
                leftT.translate(iterator, 1, destination);
            } else {
                rightT.translate(iterator, 1, destination);
            }
        }
    }

    @Override
    public boolean isInvariant() {
        return (leftT.isInvariant() && rightT.isInvariant());
    }
}



