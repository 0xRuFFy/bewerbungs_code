package ueb;

/**
 * Programmstruckturen 2 Aufgabe 3, Thema: Instanzen, verkettete Liste mit null
 * Klasse für ein Listen Element
 * Funktionen:
 *  - Element(char value) / Element(char value, Element next) -> Konstruktor der Klasse
 *  - getValue()                                -> char
 *  - getNext()                                 -> Element
 *  - setNext()                                    ist void
 *  - size()                                    -> int
 *  - isSorted()                                -> boolean
 *  - containsElement(char value)               -> boolean
 *  - toString()                                -> String
 *  - getElementAt(int index)                   -> char
 *  - prependElement(char value)                -> Element
 *  - insertElementAt(char value, int index)    -> Element
 *  - appendElement(char Element)               -> Element
 *  - insertElement (char value)                -> Element
 *  - deleteElement (char value)                -> Element
 *
 * Werte:
 *  - value         -> char
 *  - next          -> Element
 *  - INVALID_VALUE -> final char
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class Element {

    /**Wert des Listen Elements*/
    private char value;
    /**Folgendes Element in der Liste null wenn kein Folgendes*/
    private Element next;
    /**Ausgabewert für ungülige Eingaben*/
    public static final char INVALID_VALUE = 0;

    /**
     * Konstruktor 1 der Klasse Element
     *
     * @param value Wert des Elements
     */
    public Element (char value) {
        this.value = value;
    }

    /**
     * konstruktor 2 der Klasse Element
     *
     * @param value Wert des Elements
     * @param next Nächstes Element
     */
    public Element (char value, Element next) {
        this.value = value;
        this.next = next;
    }

    /**
     * Getter für value
     *
     * @return value des Elements
     */
    public char getValue() {
        return value;
    }

    /**
     * Getter für next
     *
     * @return Nächstes Element
     */
    public Element getNext() {
        return next;
    }

    /**
     * Setter für next
     *
     * @param next Nächstes Element
     */
    public void setNext(Element next) {
        this.next = next;
    }

    /**
     * Gibt die Anzahl der Elemente in der Liste aus
     *
     * @return Länge der Liste
     */
    public int size() {
        if (this.next == null)
            return 1;
        return 1 + this.next.size();
    }

    /**
     * Gibt an ob die Werte der Liste von groß nach klein sortiert sind.
     *
     * @return true ist sortiert sonst false
     */
    public boolean isSorted() {
        if (this.next == null)
            return true;
        if (this.value > this.next.value)
            return false;

        return this.next.isSorted();
    }

    /**
     * Gibt an ob ein Wert in der Liste ist.
     *
     * @param value Gesuchter Wert
     * @return true Wert gefunden sonst false
     */
    public boolean containsElement(char value) {
        if (this.value == value)
            return true;
        if (this.next == null)
            return false;

        return this.next.containsElement(value);
    }

    /**
     * Gibt die Liste als String aus
     *
     * @return Die Liste als String mit Leerzeichen als Trenstück
     */
    public String toString() {
        if (this.next == null)
            return "" + this.value;

        return this.value + " " + this.next;
    }

    /**
     * Gibt den Wert an einem gegebenem Index wieder
     *
     * @param index stelle des anzugebenen Wertes
     * @return Wert an der stell index, INVALID_VALUE wenn index ungültig
     */
    public char getElementAt(int index) {
        if (index == 0)
            return this.value;
        if (this.next == null || index < 0)
            return INVALID_VALUE;

        return this.next.getElementAt(index-1);
    }

    /**
     * Fügt ein neues Element am Anfang ein
     *
     * @param value Wert des neune Elements
     * @return das neue Element
     */
    public Element prependElement(char value) {
        return new Element(value, this);
    }

    /**
     * Fügt ein neues Element am gegebenem Index ein sollte dieser nicht gültig sein, würd einfach die unveränderte
     * Liste ausgegeben
     *
     * @param value Wert des neune Elements
     * @param index Index der neuen Position
     * @return Die gegenfalls angepasste Liste
     */
    public Element insertElementAt(char value, int index) {
        if (index < 0 || this.next == null && index > 1)
            return this;

        if (index == 0)
            return new Element(value, this);

        if (index == 1 && this.next == null) {
            this.next = new Element(value);
            return this;
        }

        this.next = this.next.insertElementAt(value, index-1);

        return this;
    }

    /**
     *  Hängt ein neues Element mit gegebenem Wert an die Liste
     *
     * @author hs
     * @param value Wert des Elements
     * @return Das Element welches vor dem Aufruf das letztz Element der Liste war.
     */
    public Element appendElement (char value) {
        if (this.next == null)
            this.next = new Element(value);
        else
            this.next = this.next.appendElement(value);

        return this;
    }

    /**
     * Fügt ein neues Element mit gegebenem Wert in die Liste sortiert ein
     *
     * @author hs
     * @param value Wert des neuen Elements
     * @return Das eingefügte Element
     */
    public Element insertElement (char value) {
        if (this.value >= value)
            return new Element(value, this);
        else
            if (this.next == null) {
                this.next = new Element(value);
                return this;
            } else {
                this.next = this.next.insertElement(value);
                return this;
            }
    }

    /**
     * Entfernt Das erste Auftreten eines gegebenen Wertes
     *
     * @author hs
     * @param value Wert des zu entfernenen Elements
     * @return Das Folgende Element der Liste nach dem zu entfernenden Element
     */
    public Element deleteElement (char value) {
        if (this.value == value)
            return this.next;
        else {
            if (this.next != null)
                this.next = this.next.deleteElement(value);
            return this;
        }
    }

}
