package ueb.rooms;

/**
 * Die Basisklasse für einen Raum.
 *
 * Implementiere alle öffentlichen Methoden. Die gegebenen Methoden dürfen nicht geändert werden.
 *
 * Füge keine öffentlichen Methoden hinzu. Private und protected-Methoden dürfen hinzugefügt werden.
 *
 * Alle Methoden können von erbenden Klassen überschrieben werden.
 *
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class Room {
    /**Name des Raums*/
    private final String name;
    /**Besachreibung des Raums*/
    private final String description;

    /**
     * Beim Generieren der für den Spieler verfügbaren Optionen beginne mit dieser Überschrift.
     */
    protected static final String OPTION_HEADER = "Hier gibt es diese Möglichkeiten:\n";

    /**
     * Verbindungen zu den anderen Räumen. Per Default leer.
     */
    protected Room[] connectedRooms = new Room[]{};

    /**
     * Dieser Konstruktor erzwingt für jeden Raum einen Namen und eine Beschreibung.
     *
     * @param name Der Name des Raumes.
     * @param description Die Beschreibung, die angezeigt wird, wenn ein Spieler den Raum betritt.
     */
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Der Name eines Raumes muss exakt übereinstimmen mit der Angabe in `texte.txt`,
     * da er in den Tests genutzt wird.
     *
     * @return Der den Raum identifizierende Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Wird hier true geliefert, steht dieser Raum für das Spielende.
     *
     * @return true, wenn dieser Raum das Spielende repräsentiert.
     */
    public boolean isFinal() {
        return false;
    }

    /**
     * Beschreibt den Raum.
     * Diese Methode kann den Status des Raums verändern.
     *
     * @return Textuelle Beschreibung des Raums.
     */
    public String describe() {
        return description;
    }

    /**
     * Ersetze die aktuellen Raumverbindungen durch die gegebenen.
     *
     * @param connectedRooms Die Räume, mit denen dieser verbunden ist. Sind keine Räume verbunden,
     *                       muss hier ein leeres Array übergeben werden.
     *                       Wird `null` übergeben, wird eine `IllegalArgumentException` ausgelöst.
     */
    public void setConnections(Room[] connectedRooms) {
        if (connectedRooms == null) {
            throw new IllegalArgumentException("connectedRooms may be empty but must not the null");
        }

        this.connectedRooms = connectedRooms;
    }

    /**
     * Erstellt einen String mit den Optionen, aus denen der Spieler wählen kann.
     *
     * Diese Methode kann überschrieben werden, um absichtlich Optionen vor dem Spieler zu verstecken.
     * Es kann auch zusätzliche Optionen geben, die sich nicht aus den verbundenen Räumen ergeben.
     *
     * Diese Methode ist ein Getter. Sie darf den Status des Raumes nicht ändern.
     *
     * Beispielausgabe:
     * "Hier gibt es diese Möglichkeiten:\n0: Gehe zurück\n"
     *
     * @return Der String mit der Beschreibung der verfügbaren Optionen. Endet immer mit newline `\n`.
     */
    public String getOptions() {
        StringBuilder opt = new StringBuilder(OPTION_HEADER);
        for (int i = 0; i < connectedRooms.length; i++) {
            opt.append(String.format("%d: %s\n", i, connectedRooms[i].getName()));
        }
        return opt.toString();
    }

    /**
     * Verarbeitet die Nutzereingabe.
     *
     * Erwartet einen String, der eine einzelne Ziffer enthält. Diese Methode kann überschrieben werden,
     * um kompliziertere Eingaben zu verarbeiten.
     *
     * Diese Methode kann den Status des Raumes verändern.
     *
     * @param userInput Die Benutzereingabe als String.
     *
     * @return Der neue Raum, in den der Spieler wandert gemäß der verbundenen Räume.
     *         Enthält der Parameter kein Zeichen, wird dieser Raum zurückgegeben (der Spieler bewegt sich nicht).
     */
    public Room processDecision(String userInput) {
        if (userInput != null && userInput.length() > 0) {
            char c = userInput.charAt(0);
            int idx = c - '0';
            return proceedToRoom(idx);
        } else {
            return this;
        }
    }

    /**
     * Wählt den Raum aus dem Array der verbundenen Räume.
     *
     * Prüft, ob der übergebene Index valide ist. Diese Methode kann überschrieben werden,
     * um kompliziertere Überprüfungen durchzuführen.
     *
     * Diese Methode kann den Status des Raums verändern.
     *
     * @param connectionIndex Der Index, zu dem der verbundene Raum geliefert werden soll.
     *
     * @return Der zum übergebenen Index passende Raum.
     *   Ist der Index nicht valide, wird dieser Raum zurückgegeben (der Spieler bewegt sich nicht).
     */
    protected Room proceedToRoom(int connectionIndex) {
        return (connectionIndex < 0 || connectionIndex >= connectedRooms.length) ? this :
                connectedRooms[connectionIndex];
    }
}
