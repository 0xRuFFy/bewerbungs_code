package ueb;

import ueb.rooms.*;

import java.util.Scanner;

/**
 * Die Hauptklasse für ein konsolenbasiertes Text-Adventure.
 *
 * Programmstruckturen 2 Aufgabe 5, Thema: Vererbung, erweiternde Klassen, überschreibende Methoden
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class Main {

    /**
     * Erstellt alle Räume des Pyramiden-Textadventures und liefert den Startraum.
     *
     * @return Der Raum, in dem das Spiel startet.
     */
    public static Room createPyramidWorld() {
        // erstelle alle Räume
        Room quit = new QuitGame();

        Room start = new Room(
                "Draußen",
                "Du stehst vor der alten Pyramide.\n"
        );

        Room death = new Room(
                "Sterben",
                "Du bist nicht geflohen, nachdem du den Schatz der Mumie gestohlen hast. Nun wird " +
                "die Pyramide auch dein Grab sein.\n"
        );

        Room longCorridor = new Room(
                "Langer Gang",
                "Dein Weg führt dich zu einer Kreuzung. Links befindet sich ein schmaler Raum. Rechts " +
                "befindet sich ein großer Raum. Dazwischen ist ein dunkler Korridor.\n"
        );

        Room slimRoom = new Room(
                "Schmaler Raum",
                "Der Boden des schmalen Raumes ist mit Trümmern übersät und alles ist mit Staub bedeckt." +
                "\n\nEin paar Hieroglyphen sind erkennbar: „Ziffern sind lächerlich, Sterblicher. " +
                "Rufe SEINEN Namen und tritt ein“.\n"
        );

        Room bigRoom = new Room(
                "Großer Raum",
                "Der große Raum ist aufwendig verziert. Mit goldener Farbe steht „CHEOPS“ überall " +
                "an den Wänden.\n"
        );

        Room note = new countingRoom(
                "Zettel",
                "Auf dem Zettel steht „PIN: 0000“.\n\nDieser Zettel wurde %d mal gelesen.\n"
        );

        Room entrance = new PinRoom(
                "Eingang",
                "Du stehst am Eingang. An der Wand neben dir klebt ein Zettel.",
                "Du musst die Tür entriegeln, bevor du die Pyramide betreten kannst. Auf dem Schloss steht " +
                "„Bitte PIN eingeben“.",
                "Die Tür ist offen. Vor dir ist ein langer Gang, der in die düsteren Tiefen der alten Pyramide" +
                " führt.",
                "0000"
        );

        Room darkCorridor = new PinRoom(
                "Dunkler Korridor",
                "Der dunkle Korridor führt tiefer in die Pyramide.",
                "Ein schwerer, großer Granitblock versperrt den Weg.",
                "Der Granitblock hat den Weg freigegeben!",
                "CHEOPS"
        );
        Room tomb = new GrabKammer(
                "Du bewunderst die Schätze der Grabkammer im Herzen der Pyramide."
        );


        // erstelle die Verbindungen zwischen den Räumen
        start.setConnections(new Room[]{quit, entrance});
        entrance.setConnections(new Room[]{start, note, longCorridor});
        note.setConnections(new Room[]{entrance});
        longCorridor.setConnections(new Room[]{entrance, slimRoom, darkCorridor, bigRoom});
        slimRoom.setConnections(new Room[]{longCorridor});
        bigRoom.setConnections(new Room[]{longCorridor});
        darkCorridor.setConnections(new Room[]{longCorridor, tomb});
        tomb.setConnections(new Room[]{darkCorridor, death});
        death.setConnections(new Room[]{quit});


        // liefere den Startraum
        return start;
    }

    /**
     * Gestaltet den gesamten Spielablauf beginnden beim Startraum.
     *
     * Diese Methode darf nicht geändert werden. {@code System.out} darf ausschließlich
     * in dieser Methode genutzt werden, keine andere Stelle darf Ausgaben tätigen.
     *
     * @param args – ungenutzt
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Room currentRoom = createPyramidWorld();

        while (!currentRoom.isFinal()) {
            System.out.print(currentRoom.describe());
            System.out.println();
            System.out.print(currentRoom.getOptions());
            System.out.print("Deine Wahl: ");
            currentRoom = currentRoom.processDecision(sc.next());
        }
        System.out.print(currentRoom.describe());
        sc.close();
    }
}
