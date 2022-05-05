package coding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import huffman.Tree;

/**
 * Klasse für eine nebenläufige Dekodierung mit Threads.
 * <p>
 * Diese Klasse ist bereits komplett vorgegeben und darf nicht verändert werden.
 *
 * @author mhe
 */
public class DecoderTask implements Runnable {
    /**
     * Für die Dekodierung zu nutzender Baum
     */
    private final Tree tree;

    /**
     * Iterator über die zu dekodierenden Bits
     */
    private final Iterator<Bit> bits;

    /**
     * Anzahl der aus der Dekodierung resultierenden Bytes
     */
    private final int byteCount;

    /**
     * Ergebnis der Dekodierung
     */
    private byte[] result;

    /**
     * Konstruktor.
     *
     * @param tree      Für die Dekodierung zu nutzender Baum, darf nicht null sein. Wird in dieser
     *                  Klasse nicht verändert.
     * @param bits      Iterator über die zu dekodierenden Bits, darf nicht null sein. Darf
     *                  Padding-Bits liefern. Wird in dieser Klasse verändert.
     * @param byteCount Anzahl der aus der Dekodierung resultierenden Bytes, muss &ge; 0 sein
     */
    public DecoderTask(Tree tree, Iterator<Bit> bits, int byteCount) {
        if (tree == null) {
            throw new IllegalArgumentException("tree darf nicht null sein");
        }
        if (bits == null) {
            throw new IllegalArgumentException("bits darf nicht null sein");
        }
        if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount muss >= 0 sein");
        }

        this.tree = tree;
        this.bits = bits;
        this.byteCount = byteCount;
    }

    @Override
    public void run() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream(this.byteCount);

            this.tree.translate(this.bits, this.byteCount, output);

            this.result = output.toByteArray();

        } catch (IOException e) {
            // Umwandlung in RuntimeException, da run() keine IOException werfen darf
            throw new RuntimeException(e);
        }
    }

    /**
     * Liefert das Ergebnis der Dekodierung.
     * <p>
     * Darf erst aufgerufen werden, wenn der umschließende Thread beendet ist.
     *
     * @return Referenz auf das Ergebnis der Dekodierung
     */
    public byte[] getResult() {
        return result;
    }
}
