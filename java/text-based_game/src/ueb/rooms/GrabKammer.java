package ueb.rooms;

/**
 * Exendent Class für Room
 *
 * GrabKammer ~ Hat Optionen welche nicht den Raum verlassen müssen
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class GrabKammer extends Room {
    /**
     * Public Konstruktor von Zettel -> Setzt Namen auf Zettel
     *
     * beschreibung würd ausschlißlich in describe geregelt.
     *
     */
    public GrabKammer(String description) {
        super("Grabkammer", description);
    }

    /**Wurden Notizen gemachtt?*/
    private boolean looked = false;
    /**wurde geplündert?*/
    private boolean stolen = false;
    /**ist gestorben*/
    private boolean died = false;

    /**
     * Beschreibt den Raum.
     *
     * @return Textuelle Beschreibung des Raums.
     */
    @Override
    public String describe() {
        return  super.describe() + "\n" + (stolen ?
                "\nOh Nein! Das Grab hat sich geöffnet und die Mumie verfolgt dich.\n" : (looked ?
                "\nDu hast Photos gemacht und alles dokumentiert.\n" : ""));
    }

    /**
     * Erstellt einen String mit den Optionen, aus denen der Spieler wählen kann.
     *
     * @return Der String mit der Beschreibung der verfügbaren Optionen. Endet immer mit newline `\n`.
     */
    @Override
    public String getOptions() {
        String out = OPTION_HEADER;

        if (!died) {
            String PICS_TEXT = "Notiere deine Beobachtung und mache ein paar Photos.";
            String STEAL_TEXT = "Stehle den Schatz.";
            out += "0: " + connectedRooms[0].getName();

            if (!stolen || !looked) {
                out += "\n1: ";
                if (looked) {
                    out += STEAL_TEXT;
                } else {
                    out += PICS_TEXT;
                    if (!stolen) {
                        out += "\n2: " + STEAL_TEXT;
                    }
                }
            }
        } else {
            out += "0: " + connectedRooms[1].getName();
        }

        return out + "\n";
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

            switch (userInput) {
                case "1":
                    if (looked) {
                        stolen = true;
                        return this;
                    }
                    if (stolen) {
                        died = true;
                        return this;
                    }

                    looked = true;
                    return this;
                case "2":
                    if (!looked && !stolen) {
                        stolen = true;
                        return this;
                    }
            }
            if (userInput.equals("0")) {
                return connectedRooms[died ? 1 : 0];
            }
            return this;
        } else
            return this;
    }


}
