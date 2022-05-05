package ueb.rooms;

/**
 * Exendent Class für Room
 *
 * PinRoom ~ Spert den nächsten gang anhand eines Pins der gefunden werden kann
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class PinRoom extends Room {
    /**Beschreibung*/
    private final String BASE;
    /**Optionale Beschreibung 1*/
    private final String OPT_1;
    /**Optionale Beschreibung 2*/
    private final String OPT_2;
    /**freischalt Pin*/
    private final String PIN;
    /**ist verspert boolean*/
    private boolean locked = true;

    /**
     * Konstruktor für extendet von Room
     *
     * Bildet eine Fortführung der Klasse Room
     *
     * @param name Raumname
     * @param base Beschreibung
     * @param opt1 Optionale Beschreibung 1
     * @param opt2 Optionale Beschreibung 1
     * @param pin freischalt Pin
     */
    public PinRoom(String name, String base, String opt1, String opt2, String pin) {
        super(name, null);
        this.BASE = base;
        this.OPT_1 = opt1;
        this.OPT_2 = opt2;
        this.PIN = pin;
    }

    /**
     * Beschreibt den Raum.
     *
     * @return Textuelle Beschreibung des Raums.
     */
    @Override
    public String describe() {
        return BASE + "\n\n" + (locked ? OPT_1 : OPT_2) + '\n';
    }

    /**
     * Erstellt einen String mit den Optionen, aus denen der Spieler wählen kann.
     *
     * @return Der String mit der Beschreibung der verfügbaren Optionen. Endet immer mit newline `\n`.
     */
    @Override
    public String getOptions() {
        StringBuilder opt = new StringBuilder(OPTION_HEADER);
        for (int i = 0; i < (connectedRooms.length) - (locked ? 1 : 0) ; i++) {
            opt.append(String.format("%d: %s\n", i, connectedRooms[i].getName()));
        }
        return opt.toString();
    }

    /**
     * Verarbeitet die Nutzereingabe.
     *
     * Erwartet einen String, der eine einzelne Ziffer enthält.
     *
     * @param userInput Die Benutzereingabe als String.
     *
     * @return Der neue Raum, in den der Spieler wandert gemäß der verbundenen Räume.
     *         Enthält der Parameter kein Zeichen, wird dieser Raum zurückgegeben (der Spieler bewegt sich nicht).
     */
    @Override
    public Room processDecision(String userInput) {
        if (userInput != null && userInput.length() > 0) {
            if (userInput.equals(PIN)) {
                locked = false;
                return this;
            }
            return super.processDecision(userInput);
        } else
            return this;
    }

    /**
     * Wählt den Raum aus dem Array der verbundenen Räume.
     *
     * Prüft, ob der übergebene Index valide ist.
     *
     * @param connectionIndex Der Index, zu dem der verbundene Raum geliefert werden soll.
     *
     * @return Der zum übergebenen Index passende Raum.
     *   Ist der Index nicht valide, wird dieser Raum zurückgegeben (der Spieler bewegt sich nicht).
     */
    @Override
    protected Room proceedToRoom(int connectionIndex) {
        if ((connectionIndex < 0 || connectionIndex >= connectedRooms.length) ||
                (connectionIndex == connectedRooms.length-1 && locked))
            return this;

        return connectedRooms[connectionIndex];

    }

}
