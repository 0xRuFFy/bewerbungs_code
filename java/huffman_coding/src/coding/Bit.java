package coding;

/**
 * Aufzählungstyp für ein Bit
 * 
 * Dieses Enum ist vollständig vorgegeben und darf nicht verändert werden.
 * 
 * @author mhe
 */
public enum Bit {
    ZERO(0), ONE(1);

    private final int value;

    /**
     * Konstruktor
     *
     * @param value Der Wert des Bits
     */
    Bit(int value) {
        this.value = value;
    }

    /**
     * Gibt den Wert des Bits zurück
     *
     * @return Der Wert des Bits
     */
    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
