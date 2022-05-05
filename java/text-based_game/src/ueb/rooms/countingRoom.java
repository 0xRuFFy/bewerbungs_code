package ueb.rooms;

/**
 * Exendent Class für Room
 *
 * countingRoom ~ zählt die aufrufe des Raumes
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class countingRoom extends Room {
    /**
     * Public Konstruktor von Zettel -> Setzt Namen auf Zettel
     *
     * beschreibung würd ausschließlich in describe geregelt.
     *
     * @param name Name des Raums
     * @param description formatierfertige Beschreibung
     */
    public countingRoom(String name, String description) {
        super(name, description);
    }

    /**Anzahl der Raum Aufrufe*/
    private int count = 41;


    /**
     * Beschreibt den Raum.
     * Diese Methode kann den Status des Raums verändern.
     *
     * @return Textuelle Beschreibung des Raums.
     */
    @Override
    public String describe() {
        return String.format(super.describe(), count += 1);
    }
}
