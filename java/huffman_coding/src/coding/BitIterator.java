package coding;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Ein Iterator, der einen InputStream bitweise durchläuft. Die Bits eines Bytes werden dabei vom
 * höchstwertigsten zum niedrigswertigsten durchlaufen.
 * <p>
 * Diese Klasse ist vollständig vorgegeben und darf nicht verändert werden.
 *
 * @author mhe
 */
public class BitIterator implements Iterator<Bit> {
    /**
     * InputStream, aus dem die Bits gelesen werden.
     */
    private final InputStream stream;

    /**
     * Das Puffer-Byte, enthält die 8 zuletzt eingelesenen Bits oder -1, wenn kein Byte mehr gelesen
     * werden konnte.
     */
    private int buffer = 0;

    /**
     * Position des nächsten Bits im Puffer-Byte.
     */
    private int pos = -1;

    /**
     * Konstruktor.
     *
     * @param stream InputStream, aus dem die Bits gelesen werden. Darf nicht null sein.
     */
    public BitIterator(InputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("stream darf nicht null sein");
        }
        this.stream = stream;
    }

    /**
     * Gibt zurück, ob ein weiteres Bit gelesen werden kann. Dazu wird, wenn nötig, ein Byte aus dem
     * InputStream gelesen.
     *
     * @return true, wenn ein weiteres Bit gelesen werden kann, ansonsten false
     * @throws RuntimeException Fehler beim Lesen
     */
    @Override
    public boolean hasNext() {
        if (this.pos == -1) {
            this.pos = Byte.SIZE - 1;
            try {
                this.buffer = stream.read();
            } catch (IOException e) {
                // Umwandlung in RuntimeException, da hasNext keine IOException werfen darf
                throw new RuntimeException(e);
            }
        }
        return this.buffer != -1;
    }

    /**
     * Gibt das nächste Bit zurück und bewegt den Iterator einen Schritt weiter. Dazu wird wenn
     * nötig ein Byte aus dem InputStream gelesen.
     *
     * @return Das nächste Bit
     * @throws RuntimeException Fehler beim Lesen
     */
    @Override
    public Bit next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return (this.buffer & 1 << this.pos--) == 0 ? Bit.ZERO : Bit.ONE;
    }
}
