package rsa;

import java.math.BigInteger;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Stellt Methoden zur Dateiarbeit in Verbindung mit den Klassen PrivateKey und PublicKey zur
 * Verfügung.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
public class FileIO {

    /**
     * Wert für den Exponenten e des öffentlichen Schlüssels (vierte Fermatsche Zahl 2^16+1)
     */
    public static final long DEFAULT_E = 65537;

    /**
     * Liest ein Byte-Array aus der Datei mit dem übergebenen Namen ein.
     *
     * @param filename Name der Datei, aus der gelesen werden soll
     * @return Byte-Array mit allen eingelesenen Bytes
     * @throws IOException Fehler, die beim Lesen auftreten
     * @pre filename != null
     */
    public static byte[] readBytes(String filename) throws IOException {
        assert filename != null;

        return Files.readAllBytes(Paths.get(filename));
    }

    /**
     * Schreibt die Elemente des übergebenen Byte-Arrays in die Datei mit dem übergebenen Namen.
     *
     * @param bytes    zu schreibende Bytes
     * @param filename Name der Datei, in die geschrieben werden soll
     * @throws IOException Fehler, die beim Schreiben auftreten
     * @pre bytes != null
     * @pre filename != null
     */
    public static void writeBytes(byte[] bytes, String filename) throws IOException {
        assert bytes != null;
        assert filename != null;

        Files.write(Paths.get(filename), bytes);
    }

    /**
     * Liest BigInteger-Werte fester Länge aus der Datei mit dem übergebenen Namen in ein
     * BigInteger-Array ein.
     * <p>
     * Führende Nullen sind hierbei erlaubt und werden ignoriert.
     *
     * @param len      Länge jeweils eines zu lesenden BigInteger-Wertes in Byte
     * @param filename Name der Datei, aus der gelesen werden soll
     * @return Array mit allen in der angegebenen Datei enthaltenen Zahlen
     * @throws IOException Fehler, die beim Lesen auftreten
     * @pre len &gt; 0
     * @pre filename != null
     */
    public static BigInteger[] readBigIntegers(int len, String filename) throws IOException {
        assert filename != null;
        assert len > 0;

        byte[] bytes = Files.readAllBytes(Paths.get(filename));

        assert bytes.length % len == 0;

        BigInteger[] bigIntegers = new BigInteger[bytes.length / len];
        int startPos = 0;

        for (int i = 0; i < bigIntegers.length; i++) {
            bigIntegers[i] = new BigInteger(Arrays.copyOfRange(bytes, startPos, startPos + len));
            startPos += len;
        }

        return bigIntegers;
    }


    /**
     * Erstellt die Byte-Repräsentation des übergebenen BigInteger-Wertes. Das Array ist bei Bedarf
     * vorne mit Nullen aufgefüllt, sodass es die übergebene Länge hat.
     *
     * @param b   Zahl, deren Byte-Repräsentation erstellt werden soll
     * @param len Länge (Anzahl der Bytes), die das Ergebnis haben soll
     * @return Byte-Repräsentation von b
     * @pre b != null
     * @pre b kann durch die angegebene Anzahl an Bytes dargestellt werden (die Länge der
     * Byte-Repräsentation von b ist kleiner/gleich len)
     * @pre len &gt; 0
     */

    public static byte[] bigIntegerToBytes(BigInteger b, int len) {
        assert b != null;
        assert b.toByteArray().length <= len;
        assert len > 0;

        byte[] src = b.toByteArray();
        byte[] result = new byte[len];  // Beim Initialisieren wird alles mit Nullen aufgefüllt
        System.arraycopy(src, 0, result, result.length - src.length, src.length);

        return result;
    }

    /**
     * Schreibt die Elemente des übergebenen BigInteger-Arrays in die Datei mit dem übergebenen
     * Namen. Die Byte-Repräsentationen der Elemente werden dabei bei Bedarf vorne mit Nullen
     * aufgefüllt, sodass sie die übergebene Länge haben.
     *
     * @param bs       zu schreibende BigInteger-Werte
     * @param len      Länge jeweils eines BigInteger-Wertes in Byte
     * @param filename Name der Datei, in die geschrieben werden soll
     * @throws IOException Fehler, die beim Schreiben auftreten
     * @pre bs != null
     * @pre filename != null
     * @pre len &gt; 0
     * @pre Für jedes Element b aus bs gilt: b kann durch die angegebene Anzahl an Bytes dargestellt
     * werden (die Länge der Byte-Repräsentation von b ist kleiner/gleich len)
     */
    public static void writeBigIntegers(BigInteger[] bs, int len, String filename)
            throws IOException {
        assert bs != null;
        assert filename != null;
        assert len > 0;

        byte[] bytes = new byte[bs.length * len];
        for (int i = 0; i < bs.length; i++) {
            assert bs[i].toByteArray().length <= len;

            byte[] bi = bigIntegerToBytes(bs[i], len);
            System.arraycopy(bi, 0, bytes, i * len, bi.length);
        }

        writeBytes(bytes, filename);


    }

    /**
     * Erzeugt das Chiffrat der angegebenen zu verschlüsselnden Datei und schreibt es in die
     * angegebene Zieldatei.
     *
     * @param publicKey         zu verwendender öffentlicher Schlüssel
     * @param plaintextFilename Name der zu verschlüsselnden Datei
     * @param encryptedFilename Name der zu erzeugenden Datei
     * @throws IOException Fehler, die beim Lesen oder Schreiben auftreten
     * @pre publicKey != null
     * @pre plaintextFilename != null
     * @pre encryptedFilename != null
     */
    public static void encrypt(PublicKey publicKey, String plaintextFilename,
                               String encryptedFilename) throws IOException {
        assert publicKey != null;
        assert plaintextFilename != null;
        assert encryptedFilename != null;

        byte[] bytes = readBytes(plaintextFilename);
        int len = (publicKey.getN().subtract(BigInteger.ONE)).toByteArray().length;

        writeBigIntegers(publicKey.encrypt(bytes), len, encryptedFilename);
    }

    /**
     * Entschlüsselt die angegebene chiffrierte Datei und schreibt das Ergebnis in die angegebene
     * Zieldatei.
     *
     * @param privateKey        zu verwendender privater Schlüssel
     * @param encryptedFilename Name der zu entschlüsselnden Datei
     * @param plaintextFilename Name der zu erzeugenden Datei
     * @throws IOException Fehler, die beim Lesen oder Schreiben auftreten
     * @pre privateKey != null
     * @pre plaintextFilename != null
     * @pre encryptedFilename != null
     */
    public static void decrypt(PrivateKey privateKey, String encryptedFilename,
                               String plaintextFilename) throws IOException {
        assert privateKey != null;
        assert plaintextFilename != null;
        assert encryptedFilename != null;

        int len = (privateKey.getN().subtract(BigInteger.ONE)).toByteArray().length;
        BigInteger[] bigIntegers = readBigIntegers(len, encryptedFilename);

        writeBytes(privateKey.decrypt(bigIntegers), plaintextFilename);
    }

    /**
     * Erzeugt die Signatur der angegebenen Datei und schreibt das Ergebnis in die angegebene
     * Zieldatei.
     *
     * @param privateKey        zu verwendender privater Schlüssel
     * @param plaintextFilename Name der zu signierenden Datei
     * @param signatureFilename Name der zu erzeugenden Datei
     * @throws IOException Fehler, die beim Lesen oder Schreiben auftreten
     * @pre privateKey != null
     * @pre plaintextFilename != null
     * @pre signatureFilename != null
     */
    public static void createSignature(PrivateKey privateKey, String plaintextFilename,
                                       String signatureFilename) throws IOException {
        assert privateKey != null;
        assert plaintextFilename != null;
        assert signatureFilename != null;

        byte[] bytes = readBytes(plaintextFilename);
        int len = (privateKey.getN().subtract(BigInteger.ONE)).toByteArray().length;

        writeBytes(bigIntegerToBytes(privateKey.hashAndSign(bytes), len), signatureFilename);
    }

    /**
     * Überprüft, ob die Signatur aus der angegebenen Signatur-Datei zu der angegebenen
     * Klartext-Datei gehört.
     * <p>
     * Fehler, die nach dem Einlesen einer leeren Datei entstehen, werden nicht abgefangen.
     *
     * @param publicKey         zu verwendender öffentlicher Schlüssel
     * @param signatureFilename Name der Datei mit der zu überprüfenden Signatur
     * @param plaintextFilename Name der Datei, deren Signatur überprüft werden soll
     * @return true, wenn die Signatur stimmt, ansonsten false
     * @throws IOException Fehler, die beim Lesen oder Schreiben auftreten
     * @pre publicKey != null
     * @pre signatureFilename != null
     * @pre plaintextFilename != null
     */
    public static boolean checkSignature(PublicKey publicKey, String signatureFilename,
                                         String plaintextFilename) throws IOException {
        assert publicKey != null;
        assert plaintextFilename != null;
        assert signatureFilename != null;

        byte[] bytes = readBytes(plaintextFilename);
        BigInteger signature = new BigInteger(readBytes(signatureFilename));

        return publicKey.checkSignature(bytes, signature);
    }

    /**
     * Liest das RSA-Modul N eines öffentlichen Schlüssels aus der Datei mit dem übergebenen Namen
     * ein und erzeugt daraus eine PublicKey-Instanz. Als Exponent e wird für alle eingelesenen
     * Schlüssel die vierte Fermatsche Zahl (2^16+1 = 65537) angenommen.
     * <p>
     * Fehler, die durch das Einlesen einer leeren Datei entstehen, werden nicht abgefangen.
     *
     * @param filename Name der Datei, aus der der RSA-Modul N gelesen werden soll
     * @return Der eingelesene Schlüssel
     * @throws IOException Fehler, die beim Lesen auftreten
     * @pre filename != null
     */
    public static PublicKey readPublicKey(String filename) throws IOException {
        assert filename != null;

        BigInteger n = new BigInteger(readBytes(filename));

        return new PublicKey(n, BigInteger.valueOf(DEFAULT_E));
    }

    /**
     * Schreibt das RSA-Modul N eines öffentlichen Schlüssels in die Datei mit dem übergebenen
     * Namen. Der Exponent e des übergebenen Schlüssels muss gleich der vierten Fermatschen Zahl
     * (2^16+1 = 65537) sein.
     *
     * @param publicKey zu schreibender Schlüssel
     * @param filename  Name der Datei, in die der RSA-Modul N geschrieben werden soll
     * @throws IOException Fehler, die beim Schreiben auftreten
     * @pre publicKey != null
     * @pre filename != null
     * @pre Der Exponent e von publicKey muss gleich 65537 sein.
     */
    public static void writePublicKey(PublicKey publicKey, String filename) throws IOException {
        assert publicKey != null;
        assert filename != null;
        assert publicKey.getE().compareTo(BigInteger.valueOf(DEFAULT_E)) == 0;

        writeBytes(publicKey.getN().toByteArray(), filename);
    }

    /**
     * Liest den Exponenten d eines privaten Schlüssel aus der Datei mit dem übergebenen Namen ein
     * und erzeugt daraus eine PrivateKey-Instanz. Deren RSA-Modul N entspricht dem des übergebenen
     * öffentlichen Schlüssels.
     * <p>
     * Fehler, die durch das Einlesen einer leeren Datei entstehen, werden nicht abgefangen.
     *
     * @param publicKey zu nutzender öffentlicher Schlüssel
     * @param filename  Name der Datei, aus der der Exponent d gelesen werden soll
     * @return Der eingelesene Schlüssel
     * @throws IOException IOException Fehler, die beim Lesen auftreten
     * @pre publicKey != null
     * @pre filename != null
     * @pre Der Exponent e von publicKey muss gleich 65537 sein.
     */
    public static PrivateKey readPrivateKey(PublicKey publicKey, String filename)
            throws IOException {
        assert publicKey != null;
        assert filename != null;
        assert publicKey.getE().compareTo(BigInteger.valueOf(DEFAULT_E)) == 0;

        BigInteger d = new BigInteger(readBytes(filename));

        return new PrivateKey(publicKey, d);
    }

    /**
     * Schreibt den Exponenten d des übergebenen privaten Schlüssel in die Datei mit dem übergebenen
     * Namen. Der zugehörige öffentliche Schlüssel sowie der RSA-Modul N werden hierbei ignoriert
     * (abgesehen von der Überprüfung der Vorbedingung).
     *
     * @param privateKey zu schreibender Schlüssel
     * @param filename   Name der Datei, in die der Exponent d geschrieben werden soll
     * @throws IOException Fehler, die beim Schreiben auftreten
     * @pre privateKey != null
     * @pre filename != null
     * @pre Der Exponent e des zugehörigen öffentlichen Schlüssels muss gleich 65537 sein.
     */
    public static void writePrivateKey(PrivateKey privateKey, String filename) throws IOException {
        assert privateKey != null;
        assert filename != null;
        assert privateKey.getPublicKey().getE().equals(BigInteger.valueOf(DEFAULT_E));

        writeBytes(privateKey.getD().toByteArray(), filename);
    }


}
