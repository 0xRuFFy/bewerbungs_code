package rsa;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

/**
 * Ein öffentlicher Schlüssel. Stellt Methoden zum Verschlüsseln von Nachrichten und zum Überprüfen
 * von Signaturen zur Verfügung.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
public class PublicKey {

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
    private final BigInteger e;

    /**
     * Konstruktor.
     *
     * @param n RSA-Modul
     * @param e Exponent
     * @pre n != null
     * @pre e != null
     * @pre n &gt; U_INT_MAX, damit Signaturen überprüft werden können
     * @pre e &gt; 0
     */
    public PublicKey(BigInteger n, BigInteger e) {
        assert n != null;
        assert e != null;
        assert n.compareTo(U_INT_MAX) > 0;
        assert e.signum() > 0;

        this.n = n;
        this.e = e;
    }

    /**
     * @return RSA-Modul N
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * @return Exponent e
     */
    public BigInteger getE() {
        return e;
    }

    /**
     * Verschlüsselt eine Nachricht, die nur aus einer einzelnen Zahl besteht.
     *
     * @param m Nachricht (Klartext-Zahl)
     * @return verschlüsselte Nachricht
     * @pre m != null
     * @pre m &ge; 0
     * @pre m &lt; n
     */
    public BigInteger encrypt(BigInteger m) {
        assert m != null;
        assert m.signum() >= 0;
        assert m.compareTo(this.n) < 0;

        return m.modPow(this.e, this.n); // m^e (mod n)
    }

    /**
     * Verschlüsselt eine Nachricht, die aus mehreren Zahlen (ein Byte-Array) besteht.
     *
     * @param message Nachricht (Klartext)
     * @return verschlüsselte Nachricht
     * @pre message != null
     */
    public BigInteger[] encrypt(byte[] message) {
        assert message != null;

        BigInteger[] cypher = new BigInteger[message.length];
        for (int i = 0; i < message.length; i++) {
            cypher[i] = encrypt(BigInteger.valueOf(message[i]));
        }
        return cypher;
    }

    /**
     * Entpackt den Hash-Wert aus einer Signatur.
     *
     * @param s Signatur
     * @return in s verpackter Hash-Wert
     * @pre s != null
     * @pre s &ge; 0
     * @pre s &lt; n
     */
    public BigInteger getHashCode(BigInteger s) {
        assert s != null;
        assert s.signum() >= 0;
        assert s.compareTo(this.n) < 0;

        return s.modPow(this.e, this.n);
    }

    /**
     * Überprüft die Signatur einer Nachricht.
     * <p>
     * Vorsicht: Diese Methode verwendet die kryptographisch unsichere Hash-Funktion
     * Arrays.hashCode(byte[]).
     *
     * @param message   Nachricht
     * @param signature zu überprüfende Signatur
     * @return true, wenn signature die Signatur von m ist, ansonsten false
     * @pre message != null
     * @pre signature != null
     * @pre signature &ge; 0
     * @pre signature &lt; n
     */
    public boolean checkSignature(byte[] message, BigInteger signature) {
        assert message != null;
        assert signature != null;
        assert signature.signum() >= 0;
        assert signature.compareTo(this.n) < 0;

        BigInteger hashedMessage = BigInteger.valueOf(
                (long) Arrays.hashCode(message) - Integer.MIN_VALUE
        );
        return getHashCode(signature).equals(hashedMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, e);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PublicKey)) {
            return false;
        }
        PublicKey other = (PublicKey) obj;
        return Objects.equals(n, other.n) && Objects.equals(e, other.e);
    }

    @Override
    public String toString() {
        return "PublicKey [n=" + n + ", e=" + e + "]";
    }

}
