package coding;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasse für eine nebenläufige Kodierung mit Threads.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
public class EncoderTask implements Runnable {

    /**
     * Für die Kodierung zu nutzende Kodierungstabelle
     */
    private final BitChain[] codetable;

    /**
     * Array mit zu kodierenden Bytes
     */
    private final byte[] bytes;

    /**
     * Index des ersten zu kodierenden Bytes im übergebenen Array
     */
    private final int startIdx;

    /**
     * Anzahl der zu kodierenden Bytes
     */
    private final int numOfBytes;

    /**
     * Ergebnis der Kodierung
     */
    private byte[] result;

    /**
     * Anzahl der Bits im Ergebnis der Kodierung. Padding-Bits werden dabei nicht mitgezählt.
     */
    private long bitsInResult = 0;

    /**
     * Konstruktor.
     *
     * @param codetable  Für die Kodierung zu nutzende Kodierungstabelle, darf nicht null sein. Wird
     *                   in dieser Klasse nicht verändert.
     * @param bytes      Array mit zu kodierenden Bytes, darf nicht null sein. Wird in dieser Klasse
     *                   nicht verändert.
     * @param startIdx   Index des ersten zu kodierenden Bytes im übergebenen Array,
     *                   muss &ge; 0 sein
     * @param numOfBytes Anzahl der zu kodierenden Bytes, muss &ge; 0 sein
     */
    public EncoderTask(BitChain[] codetable, byte[] bytes, int startIdx, int numOfBytes) {
        if (codetable == null) {
            throw new IllegalArgumentException("codetable darf nicht null sein");
        }
        if (bytes == null) {
            throw new IllegalArgumentException("bytes darf nicht null sein");
        }
        if (startIdx < 0) {
            throw new IllegalArgumentException("startIdx muss >= 0 sein");
        }
        if (numOfBytes < 0) {
            throw new IllegalArgumentException("blockLength muss >= 0 sein");
        }

        this.codetable = codetable;
        this.bytes = bytes;
        this.startIdx = startIdx;
        this.numOfBytes = numOfBytes;
    }

    @Override
    public void run() {
        try {
            // Output
            ByteArrayOutputStream output = new ByteArrayOutputStream(numOfBytes);

            // Liste mit Symbolen in Reihenfolger der Kodierungstabelle
            ArrayList<Integer> symbol = new ArrayList<>();
            int j = 0;
            while (codetable[j] != null) {
                symbol.add(codetable[j].getSymbol());
                j++;
            }

            // Kodieren
            BitChain kodierungssequenz = new BitChain();
            for (int i = 0; i < this.numOfBytes; i++) {
                kodierungssequenz.append(codetable[symbol.indexOf(
                        Byte.toUnsignedInt(bytes[i + startIdx]))]);
                // Schreibt Kodierungssequenz Byteweise in Output
                kodierungssequenz.writeTo(output);
            }
            // Geschriebene Bits berechnen
            bitsInResult = (long) Byte.SIZE * output.size() + kodierungssequenz.length();


            // Anfügen der Paddingbits
            while (kodierungssequenz.length() % Byte.SIZE != 0) {
                kodierungssequenz.append(Bit.ZERO);
            }
            // Rest der Kodierungssequenz (mit Paddingbits) in Output schreiben
            kodierungssequenz.writeTo(output);

            this.result = output.toByteArray();

        } catch (IOException e) {
            // Umwandlung in RuntimeException, da run() keine IOException werfen darf
            throw new RuntimeException(e);
        }
    }

    /**
     * Liefert das Ergebnis der Kodierung.
     * <p>
     * Darf erst aufgerufen werden, wenn der umschließende Thread beendet ist.
     *
     * @return Referenz auf das Ergebnis der Kodierung
     */
    public byte[] getResult() {
        return this.result;
    }

    /**
     * Liefert die Anzahl der Bits im Ergebnis der Kodierung. Padding-Bits werden dabei nicht
     * mitgezählt.
     * <p>
     * Darf erst aufgerufen werden, wenn der umschließende Thread beendet ist.
     *
     * @return Anzahl der Bits im Ergebnis der Kodierung
     */
    public long getBitsInResult() {
        return this.bitsInResult;
    }
}
