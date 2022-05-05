package huffman;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import coding.Bit;
import coding.BitChain;
import coding.BitIterator;

/**
 * Ein Huffman-Baum.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
public abstract class Tree {

    private final BitChain bitChain = new BitChain();

    /**
     * Gibt einen leeren Baum zurück.
     *
     * @return Ein leerer Baum
     */
    public static Tree empty() {
        return new Empty();
    }

    /**
     * Erzeugt einen Baum, der nur das übergebene Byte enthält.
     *
     * @param b     Das zu nutzende Byte, muss zwischen 0 und 255 sein
     * @param count Die Anzahl von b, muss mindestens 1 sein
     * @return Der neue Baum
     */
    public static Tree fromByte(int b, long count) {
        assert b >= 0;
        assert b <= Byte.MAX_VALUE - Byte.MIN_VALUE;
        assert count >= 1;

        return new Leaf(b, count);
    }

    /**
     * Erzeugt einen neuen Baum(knoten) mit den zwei übergebenen Bäumen als Unterbäume.
     *
     * @param l Der linke Unterbaum, darf nicht null sein
     * @param r Der rechte Unterbaum, darf nicht null sein
     * @return Der Baum, der den neu erzeugten Baum(knoten) als Wurzel hat
     */
    public static Tree merge(Tree l, Tree r) {
        if (l == null || r == null) {
            throw new IllegalArgumentException("tree darf nicht null sein");
        }
        return new Node(l, r);
    }

    /**
     * Gibt zurück, ob der Baum leer ist.
     *
     * @return true, falls der Baum leer ist, sonst false
     */
    public abstract boolean isEmpty();

    /**
     * Wenn der Baum ein Blatt ist, wird das darin genutzte Byte zurückgegeben.
     *
     * @return Das Byte des Blatts
     * @throws IllegalArgumentException wenn der Baum kein Blatt ist
     */
    public abstract int getByte();

    /**
     * Gibt die Gesamtanzahl der repräsentierten Bytes des Baums zurück.
     *
     * @return Die Gesamtanzahl der repräsentierten Bytes des Baums
     */
    public abstract long getCount();

    /**
     * Erstellt eine Kodierungstabelle aus den im Baum enthaltenen Bytes. Das resultierende Array
     * enthält 256 Plätze für die möglichen Bytes, die aufsteigend nach ihrem Wert sortiert sind
     * und jeweils eine {@link BitChain} mit der Kodierung für das entsprechende Byte enthalten.
     * Nicht genutzte Plätze werden mit null belegt.
     *
     * @return Die Kodierungstabelle
     */
    public abstract BitChain[] toCodetable();


    /**
     * Gibt die Graphviz-Repäsentation des Baums als String zurück.
     *
     * @return Die Graphviz-Repäsentation des Baums
     */
    public String toGraphviz() {
        final StringBuilder sb = new StringBuilder("digraph G {\n");
        this.toGraphviz(sb, "_");
        sb.append("}\n");

        return sb.toString();
    }

    /**
     * Schreibt die Graphviz-Repräsentation des Baums in den übergebenen StringBuilder.
     *
     * @param sb     Der StringBuilder, in den die Repräsentation des Baum geschrieben werden soll
     * @param prefix Das Prefix für das entsprechende Element des Baums
     * @pre sb != null
     * @pre prefix != null
     */
    protected abstract void toGraphviz(StringBuilder sb, String prefix);

    /**
     * Gibt das in der Graphviz-Repräsentation zu nutzende Label für ein übergebenes Byte zurück.
     * <p>
     * Für druckbare Zeichen ist dies das Zeichen selbst (wenn nötig an die Graphviz-Ausgabe
     * angepasst), für andere Werte der Index.
     *
     * @param b Das umzuwandelnde Zeichen
     * @return Das zu nutzende Label
     */
    protected static String byteToLabel(int b) {
        final int start = 0x20, end = 0x7E;

        if ((char) b == '"') {
            return "'\\\"'"; // doppeltes Anführungszeichen maskieren
        } else if ((char) b == '\\') {
            return "'\\\\'"; // Backslash maskieren
        } else if (b >= start && b <= end) {
            return "'" + (char) b + "'"; // druckbares Zeichen
        } else {
            return "" + b; // Index des Zeichens
        }
    }

    /**
     * Traversiert den Baum anhand der Bitsequenz aus dem übergebenen {@link BitIterator} und
     * schreibt gefundene Bytes in den übergebenen OutputStream. Es werden 'numberOfBytes' Bytes
     * geschrieben.
     *
     * @param iterator      Iterator, der die Bitsequenz enthält, darf nicht null sein
     * @param numberOfBytes zu schreibende Anzahl an Bytes, muss &ge; 0 sein
     * @param destination   Zielstrom, darf nicht null sein
     * @throws IOException Fehler beim Lesen oder Schreiben der Dateien
     */
    public abstract void translate(Iterator<Bit> iterator, long numberOfBytes,
                                   OutputStream destination) throws IOException;

    /**
     * Getter für die Bitchain
     *
     * @return Bitchain
     */
    protected BitChain getBitChain() {
        return bitChain;
    }

    /**
     * Prüft ob der Tree invariant ist:
     * Der rechte und linke Teilbaum sind leer
     * oder der Baum ist ein Blatt
     * oder der Baum ist leer.
     *
     * @return True falls invariante, False falls nicht
     */
    public abstract boolean isInvariant();
}
