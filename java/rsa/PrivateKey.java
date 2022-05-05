package rsa;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

/**
 * Ein privater Schlüssel. Enthält eine Referenz auf den zugehörigen öffentlichen Schlüssel. Stellt
 * Methoden zum Entschlüsseln von Nachrichten und zum Erstellen von Signaturen zur Verfügung.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
public class PrivateKey {

    /**
     * Größter in einem vorzeichenlosen Int ablegbarer Wert
     */
    public static final BigInteger U_INT_MAX = BigInteger.valueOf(4294967295L);

    /**
     * RSA-Modul
     */
    private final BigInteger n;

    /**
     * Exponent
     */
    private final BigInteger d;

    /**
     * zugehöriger öffentlicher Schlüssel
     */
    private final PublicKey publicKey;

    /**
     * Konstruktor, der die zu nutzenden Werte aus den Parametern übernimmt.
     *
     * @param publicKey zu nutzender öffentlicher Schlüssel
     * @param d         Exponent des privaten Schlüssels
     * @pre publicKey != null
     * @pre d != null
     * @pre d &gt; 0
     */
    public PrivateKey(PublicKey publicKey, BigInteger d) {
        assert publicKey != null;
        assert d != null;
        assert d.signum() > 0;

        this.publicKey = publicKey;
        this.n = publicKey.getN();
        this.d = d;
    }

    /**
     * Konstruktor, der die zu nutzenden Werte aus den Parametern generiert.
     *
     * @param p Primzahl p
     * @param q Primzahl q
     * @param e Exponent des zu generierenden öffentlichen Schlüssels
     * @pre p != null
     * @pre q != null
     * @pre e != null
     * @pre p &gt; 0
     * @pre q &gt; 0
     * @pre p und q sind Primzahlen
     * @pre p und q sind ungleiche Zahlen
     * @pre p * q &gt; U_INT_MAX, damit Signaturen erstellt werden können
     * @pre e &gt; 0
     * @post d &gt; 0
     */
    public PrivateKey(BigInteger p, BigInteger q, BigInteger e) {
        assert p != null;
        assert q != null;
        assert e != null;
        assert q.signum() > 0;
        assert p.signum() > 0;
        assert e.signum() > 0;
        assert isPrime(q);
        assert isPrime(p);
        assert !p.equals(q);
        assert (p.multiply(q)).compareTo(U_INT_MAX) > 0;


        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        this.n = p.multiply(q);
        this.d = computeD(e, phi);
        this.publicKey = new PublicKey(this.n, e);

        assert this.d.signum() > 0;
    }

    /**
     * Gibt zurück, ob die übergebene Zahl eine Primzahl ist.
     *
     * @param i zu überprüfende Zahl
     * @return true, wenn i eine Primzahl ist, ansonsten false
     * @pre i != null
     * @pre i &ge; 0
     */
    public static boolean isPrime(BigInteger i) {
        assert i != null;
        assert i.signum() >= 0;

        if (i.compareTo(BigInteger.ONE) <= 0) {
            return false;
        }

        boolean prime = true;
        BigInteger j = BigInteger.valueOf(2);

        while (j.pow(2).compareTo(i) <= 0 && prime) {
            prime = !(i.mod(j).equals(BigInteger.ZERO));
            j = j.add(BigInteger.ONE);
        }
        return prime;
    }

    /**
     * Bestimmt den ersten Bézout-Koeffizienten s für die zwei übergebenen Zahlen.
     * <p>
     * Lemma von Bézout: ggt(a,b) = s * a + t * b
     *
     * @param a erste Zahl
     * @param b zweite Zahl
     * @return erster Bézout-Koeffizient s bezüglich a und b
     * @pre a != null
     * @pre b != null
     * @pre a &gt; 0
     * @pre b &gt; 0
     */
    public static BigInteger firstBezout(BigInteger a, BigInteger b) {
        assert a != null;
        assert b != null;
        assert a.signum() > 0;
        assert b.signum() > 0;

        BigInteger s = BigInteger.ZERO, t = BigInteger.ONE, r = b,
                oldS = BigInteger.ONE, oldT = BigInteger.ZERO, oldR = a,
                quotient, newS, newT, newR;

        while (!r.equals(BigInteger.ZERO)) {
            quotient = oldR.divide(r);
            newR = oldR.subtract(quotient.multiply(r));
            oldR = r;
            r = newR;
            newS = oldS.subtract(quotient.multiply(s));
            oldS = s;
            s = newS;
            newT = oldT.subtract(quotient.multiply(t));
            oldT = t;
            t = newT;
        }

        return oldS;
    }

    /**
     * Berechnet den Exponenten d aus den Werten e und phi mit Hilfe der Methode firstBezout.
     *
     * @param e   Der öffentliche Exponent e
     * @param phi Der Wert phi
     * @return resultierendes d
     * @pre e != null
     * @pre phi != null
     * @pre e &gt; 0
     * @pre phi &gt; 0
     */
    public static BigInteger computeD(BigInteger e, BigInteger phi) {
        assert e != null;
        assert phi != null;
        assert e.signum() > 0;
        assert phi.signum() > 0;

        return firstBezout(e, phi).mod(phi);
    }

    /**
     * @return RSA-Modul N
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * @return Exponent d
     */
    public BigInteger getD() {
        return d;
    }

    /**
     * @return zugehöriger öffentlicher Schlüssel
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Entschlüsselt eine Nachricht, die aus einer einzelnen Zahl besteht.
     *
     * @param c verschlüsselte Nachricht
     * @return entschlüsselte Nachricht (Klartext-Zahl)
     * @pre c != null
     * @pre c &ge; 0
     * @pre c &lt; n
     */
    public BigInteger decrypt(BigInteger c) {
        assert c != null;
        assert c.signum() >= 0;
        assert c.compareTo(this.n) < 0;

        return c.modPow(this.d, this.n); // c^d (mod n)
    }

    /**
     * Entschlüsselt eine Nachricht aus mehreren Zahlen (BigInteger-Array).
     *
     * @param cipher verschlüsselte Nachricht
     * @return entschlüsselte Nachricht (Klartext-Bytes)
     * @pre cipher != null
     * @pre Jedes Element aus c ist &ge; 0 und &lt; n
     */
    public byte[] decrypt(BigInteger[] cipher) {
        assert cipher != null;


        byte[] list = new byte[cipher.length];
        for (int i = 0; i < list.length; i++) {
            assert cipher[i].signum() >= 0;
            assert cipher[i].compareTo(this.n) < 0;

            //cipher[i] immer kleiner als 256 da hier Zeichen des ASCII Zeichensatzes benutzt werden
            byte[] bytes = decrypt(cipher[i]).toByteArray();

            // Zahlen < 256 können mit einem byte dargestellt werden
            assert bytes.length == 1;

            list[i] = bytes[0];
        }
        return list;
    }

    /**
     * Erstellt die Signatur für einen Hash-Wert.
     *
     * @param h zu signierender Hash-Wert
     * @return Signatur von h
     * @pre h != null
     * @pre h &ge; 0
     * @pre h &lt; n
     */
    public BigInteger sign(BigInteger h) {
        assert h != null;
        assert h.signum() >= 0;
        assert h.compareTo(this.n) < 0;

        return h.modPow(this.d, this.n);
    }

    /**
     * Erstellt die Signatur für eine Nachricht.
     * <p>
     * Vorsicht: Diese Methode verwendet die kryptographisch unsichere Hash-Funktion
     * Arrays.hashCode(byte[]).
     *
     * @param message Nachricht
     * @return Signatur
     * @pre message != null
     */
    public BigInteger hashAndSign(byte[] message) {
        assert message != null;

        long hashcode = (long) Arrays.hashCode(message) - Integer.MIN_VALUE;
        return sign(BigInteger.valueOf(hashcode));
    }

    @Override
    public int hashCode() {
        return Objects.hash(d, n, publicKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PrivateKey)) {
            return false;
        }
        PrivateKey other = (PrivateKey) obj;
        return Objects.equals(d, other.d) && Objects.equals(n, other.n)
                && Objects.equals(publicKey, other.publicKey);
    }

    @Override
    public String toString() {
        return "PrivateKey [n=" + n + ", d=" + d + ", publicKey=" + publicKey + "]";
    }

}
