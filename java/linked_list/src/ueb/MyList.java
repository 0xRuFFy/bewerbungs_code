package ueb;

/**
 * Programmstruckturen 2 Aufgabe 3, Thema: Instanzen, verkettete Liste mit null
 * Klasse für ein LinkedList
 * Funktionen:
 *  - isEmpty()                                 -> boolean
 *  - size()                                    -> int
 *  - isSorted()                                -> boolean
 *  - containsElement(char value)               -> boolean
 *  - toString()                                -> String
 *  - getValues()                               -> char[]
 *  - getElementAt(int index)                   -> char
 *  - prependElement(char value)                   ist void
 *  - insertElementAt(char value, int index)       ist void
 *  - insertSortedIfUnique(char value)             ist void
 *  - append_Element (char Element)                ist void
 *  - insert_Element (char value)                  ist void
 *  - delete_Element (char value)                  ist void
 *
 * Werte:
 *  - elements -> Element
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class MyList {
    /**Erstes Element der Liste (mit .next gesammte Liste)*/
    private Element elements;

    /**
     * Gibt aus ob die Liste leer ist.
     *
     * @author hs
     * @return true wenn leer sonst false
     */
    public boolean isEmpty() {
        return elements == null;
    }

    /**
     * liefert die Anzahl der Listenelemente
     *
     * @return Länge von elements
     */
    public int size() {
        if (isEmpty())
            return 0;
        return elements.size();
    }

    /**
     * Gibt an ob die Werte der Liste von groß nach klein sortiert sind.
     *
     * @return true ist sortiert sonst false
     */
    public boolean isSorted() {
        if (isEmpty())
            return true;
        return elements.isSorted();
    }

    /**
     * Gibt an ob ein WErt in der Liste ist.
     *
     * @param value Gesuchter Wert
     * @return true Wert gefunden sonst false
     */
    public boolean containsElement(char value) {
        if (isEmpty())
            return false;
        return elements.containsElement(value);
    }

    /**
     * Gibt die Liste als String aus
     *
     * @return Die Liste als String mit Leerzeichen als Trennstück
     */
    public String toString() {
        if (isEmpty())
            return "{}";
        return "{" + elements.toString() + "}";
    }

    /**
     * Wandelt die Liste in ein Array um
     *
     * @return Array mit Listenelementen
     */
     char[] getValues() {
        char[] arr = new char[size()];
        int i = 0;
        for (Element x = elements; x != null; x = x.getNext()) {
            arr[i++] = x.getValue();
        }
        return arr;
    }

    /**
     * Gibt den Wert an einem gegebenem Index wieder
     *
     * @param index stelle des anzugebenen Wertes
     * @return Wert an der stell index, INVALID_VALUE wenn index ungültig
     */
    public char getElementAt(int index) {
        if (isEmpty())
            return Element.INVALID_VALUE;

        return elements.getElementAt(index);
    }

    /**
     * Fügt ein neues Element am Anfang ein
     *
     * @param value Wert des neune Elements
     */
    public void prependElement(char value) {
        if (isEmpty())
            elements = new Element(value);
        else
            elements = elements.prependElement(value);
    }

    /**
     * Fügt ein neues Element am gegebenem Index ein sollte dieser nicht gültig sein, würd einfach die unveränderte
     * Liste ausgegeben
     *
     * @param value Wert des neune Elements
     * @param index Index der neuen Position
     */
    public void insertElementAt(char value, int index) {
        if (!isEmpty())
            elements = elements.insertElementAt(value, index);
        else if (index == 0)
            elements = new Element(value);
    }

    /**
     * fügt ein Element mit dem Wert value aufsteigend sortiert ein. Ist bereits ein Element mit diesem Wert
     * vorhanden, bleibt die Liste unverändert.
     *
     * @param value einzufügender Wert
     */
    public void insertSortedIfUnique(char value) {
        if (isEmpty())
            elements = new Element(value);
        else if (!elements.containsElement(value))
            elements = elements.insertElement(value);
    }

    /**
     * Hängt an die List ein neues Element mit gegebem value an.
     *
     * @author hs
     * @param value Wert des neuen Elements
     */
    public void appendElement(char value) {
        if (this.isEmpty())
            elements = new Element(value, null);
        else
            elements = elements.appendElement(value);
    }

    /**
     * Fügt ein neues Element mit gegebenem Wert an der richtigen Stelle ein.
     *
     * @author hs
     * @param value Wert des neuen Elements
     */
    public void insertElement(char value) {
        if (this.isEmpty())
            elements = new Element(value, null);
        else
            elements = elements.insertElement(value);
    }

    /**
     * entfernt das erste Element mit dem gegebenem Wert aus der Liste
     *
     * @author hs
     * @param value Wert des neuen Elements
     */
    public void deleteElement(char value) {
        if (! isEmpty())
            elements = elements.deleteElement(value);
    }
}
