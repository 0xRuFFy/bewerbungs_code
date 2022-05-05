package logic;

import java.util.Arrays;
import java.util.Random;

/**
 * Logik des Spiels "Memory". In diesem Spiel spielen zwei Spieler gegeneinander
 * auf einem 4x3 Spielfeld. Die Spieler wählen abwechselnd zwei Karten aus, von 
 * denen sie die Symbole sehen wollen. Stimmen die Symbole überein, hat der 
 * jeweilige Spieler ein Paar gefunden. Ziel des Spiels ist es mehr Paare als der
 * Gegenspieler zu finden.  
 *
 * @author cei | Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class PairsLogic {

    /**Namen der Spieler in einem Array. Länge muss 2 sein.*/
    private final String[] players;

    /**
	 * Index des aktuellen Spielers. Muss entweder 0 oder 1 sein. Startet immer mit 0. 
	 * (Streng genommen ist die Initialisierung nicht notwendig, wir machen es aber
	 * trotzdem). 
     */
    private int idxCurrPlayer = 0;

    /**Verbindung zur GUI.*/
    private final GUIConnector gui;

    /**
	 * Aufzählungsdatentyp für die Symbole der Karten. Mehr Symbole als notwendig wären
	 * für ein Spielfeld der Größe 4x3, um das Spiel etwas interessanter zu gestalten. 
     */
    public enum Symbol {
        BEE, HONEY, BEAR, PIG,
        CAKE, OWL, GHOST, BOOK, BAT,
        POO, DOG, UNICORN, BUG
    }

    /**Das 2-dimensionale Spielfeld mit Karten.*/
    private final Symbol[][] cards;

    /**
	 * Aufzählung um zu vermerken, wer ein Paar gefunden (gelöst) hat. Entweder war es
	 * der erste Spieler, der zweite oder das Paar ist noch nicht gefunden worden.
	 * Der Ordinal wert des Enumwertes für einen Spieler korrespondiert mit dem Index
	 * des Spielers im Spielerarray. 
     */
    enum Solved {
        FST, SND, NOT
    }

    /**
	 * Um sich zu merken, an welchen Positionen bereits Paare gefunden worden sind. 
	 * Wichtig, sobald alle Paare gefunden worden sind. 
     */
    private final Solved[][] cardsSolved;

    /**
	 * Die Positionen der Karten, die gerade aufgedeckt worden sind. Dieses Array soll
	 * immer die Länge zwei haben! Sind die beiden enthaltenen Arrays beide leer, dann
	 * hat das Spiel garde anfangen oder ein Spieler hat eben ein Paar gefunden. Ist
	 * nur die erste Position mit einem Array belegt, hat der aktuelle Spieler bisher nur
	 * seine erste Karte ausgewählt und es muss darauf gewartet werden, dass er/sie einem
	 * zweite Karte auswählt. Sind zwei Positionen gespeichert, dann hatte der vorherige 
	 * Spieler zwei Karten ausgewählt, die kein Paar waren. 
     */
    private int[][] posOpenCards = new int[2][0];

    /**
     * Konstruktor für ein Memoryspiel. Initialisiert das Feld. 
     *
     * @param p1 name of the first player
     * @param p2 name of the second player
     * @param width width of the game field
     * @param height height of the game field
     * @param gui connection to the gui
     */
    public PairsLogic(String p1, String p2, int width, int height, GUIConnector gui) {
		if (width < 1 || height < 1 || width * height % 2 != 0)
			throw new AssertionError();
        this.players = new String[]{p1, p2};
        this.cardsSolved = new Solved[width][height];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                this.cardsSolved[i][j] = Solved.NOT;

        this.cards = fillWithRandomCards(new Symbol[width][height]);
        this.gui = gui;
    }

    /**
     * Test Konstruktor
     *
     * @param pNames Spieler Namen
     * @param cardSymbols Karten Symbole
     * @param solved Karten Status
     * @param gui Verbindung zum GUI
     */
    PairsLogic(String[] pNames, Symbol[][] cardSymbols, Solved[][] solved, GUIConnector gui) {
        this.players = pNames;
        this.cards = cardSymbols;
        this.cardsSolved = solved;
        this.gui = gui;
    }


    /**
     * Gibt ein zufälliges Symbol zurück
     *
     * @return ein zufälliges Symbol zurück
     */
    private Symbol getRandomSymbol() {
        Symbol[] symbols = Symbol.values();
        return Symbol.values()[new Random().nextInt(symbols.length)];
    }

    /**
     * Überprüft ob in symbol in einer Liste von Symbolen vorhanden ist
     *
     * @param symbol das Symbole welche gesucht ist
     * @param symbols Array in dem gesucht wird
     * @return true -> Symbol ist im Array sonst -> false
     */
    private boolean contains(Symbol symbol, Symbol[] symbols) {
        if (symbols == null || symbol == null)
            throw new NullPointerException();
        boolean out = false;
        for (int i = 0; i < symbols.length && !out; i++)
            out = symbol == symbols[i];
        return out;
    }

    /**
     * Gibt ein Array mit sich nicht wiederholenden Symbolen aus
     *
     * @param length Länge des Arrays
     * @return Array mit sich nicht wiederholenden Symbolen
     */
    private Symbol[] getRandomSymbolArray(int length) {
        Symbol[] symbols = new Symbol[length];
        Symbol temp;
        for (int i = 0; i < length; i++) {
            do {
                temp = getRandomSymbol();
            } while (contains(temp, symbols));
            symbols[i] = temp;
        }
        return symbols;
    }

    /**
     * Liefert eine freie Position (durch null repräsentiert) im gegebenen Array. Gibt es keine freie Position im Array,
     * kann die Methode endlos laufen.
     *
     * @param cards Das Array in dem gesucht wird
     * @return die Position als int[] -> {x, y}
     */
    private int[] getRandomFreePos(Symbol[][] cards) {
        if (cards == null)
            throw new NullPointerException();

        Random rand = new Random();
        int[] pos = new int[2];
        do {
            pos[0] = rand.nextInt(cards.length);
            pos[1] = rand.nextInt(cards[0].length);
        } while(cards[pos[0]][pos[1]] != null);
        return pos;
    }

    /**
     * füllt das gegebene Array mit zufälligen Paaren von Symbolen an zufälligen Stellen und gibt dieses zurück.
     *
     * @param cards das zu füllende Array
     * @return das gefüllte Array
     */
    private Symbol[][] fillWithRandomCards(Symbol[][] cards) {
        if (cards == null)
            throw new NullPointerException();

        Symbol[] symbols = getRandomSymbolArray(cards.length * cards[0].length / 2);
        int[] fstCard, sndCard;

        for (Symbol symbol : symbols) {
            fstCard = getRandomFreePos(cards);
            cards[fstCard[0]][fstCard[1]] = symbol;

            sndCard = getRandomFreePos(cards);
            cards[sndCard[0]][sndCard[1]] = symbol;
        }
        return cards;
    }

    /**
     * Prüft ob alle Paare aufgedeckt wurden
     *
     * @return true -> alles aufgedeckt sonst -> false
     */
    protected boolean allSolved() {
        boolean out = true;
        for (int i = 0; i < cardsSolved.length && out; i++) {
            for (int j = 0; j < cardsSolved[i].length && out; j++) {
                out = cardsSolved[i][j] != Solved.NOT;
            }
        }
        return out;
    }

    /**
     * liefert den Namen des Gewinners. Bei "Unentschieden" wird null zurückgegeben.
     * Brechen Sie das Programm mit einem AssertionError ab, falls noch nicht alle Karten aufgedeckt wurden.
     *
     * @return Namen des Gewinners, Bei "Unentschieden" null
     * @throws AssertionError wenn noch nicht alles aufgedeckt wurde
     */
    protected String getWinnerName() {
        if (!allSolved())
            throw new AssertionError("Noch nicht alles Aufgedeckt");

        int fstCount = 0, sndCount = 0;
        for (Solved[] solveds : cardsSolved) {
            for (Solved solved : solveds) {
                if (solved == Solved.FST)
                    fstCount += 1;
                else
                    sndCount += 1;
            }
        }

        if (fstCount == sndCount)
            return null;
        return fstCount > sndCount ? players[0] : players[1];
    }

    /**
     * liefert den Namen des aktuellen Spielers.
     * Außerhalb dieser Klasse sollte diese Methode ausschließlich zum Testen verwendet werden.
     *
     * @return Namen des aktuellen Spielers
     */
    String getNameCurrentPlayer() {
        return players[idxCurrPlayer];
    }

    /**
     * liefert eine String Repräsentation des Spielfeldes. Spalten der Breite 5 werden zusätzlich mit einem
     * Leerzeichen getrennt, Zeilen durch einen Zeilenumbruch (\n). Eine Beispielausgabe können Sie den gegebenen
     * Tests entnehmen. Diese Methode soll ausschließlich zum Testen/Debuggen genutzt werden.
     *
     * @return String Repräsentation des Spielfeldes
     */
    String cardsToString() {
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < cards[0].length; i++) {
            for (Symbol[] card : cards) {
                field.append(String.format("%-5s", card[i].toString())).append(" ");
            }
            field.append('\n');
        }
        return field.toString();
    }

    /**
	 * Kümmert sich um den Zug eines Spielers. Ist die ausgewählte Zelle nicht 
	 * leer, passiert nichts (der Spieler kann eine andere Zelle auswählen). War die
	 * Zelle leer und es war die erste Karte, die der Spieler ausgewählt hat, so 
	 * passiert nicht viel (außer, dass sich die Informationen gemerkt werden). War
	 * die Zelle leer und der Spieler hat seine zweite Karte gewählt, so ist der Zug
	 * des Spielers beendet und der nächste Spieler ist am Zug. Wurde ein Paar
	 * gefunden, muss überprüft werden, ob jemand das Spiel gewonnen hat. Im 
	 * Gegensatz zu den üblichen Memoryregeln, jemand der ein Paar gefunden hat, ist 
	 * nicht direkt wieder an der Reihe. 
     *
     * @param pos Koordinate der Zelle, die der aktuelle Spieler aufdecken möchte
     */
    public void playerTurn(int[] pos) {
        if (pos == null || pos.length != 2)
			throw new AssertionError();

        if (cardsSolved[pos[1]][pos[0]] == Solved.NOT) {
            resetForNewTurn();

            if (posOpenCards[0].length == 0) {
                setCardAndShow(0, pos);
            } else if (!Arrays.equals(posOpenCards[0], pos)) {
                setCardAndShow(1, pos);

                if (match()) {
                    setCardsSolved();
                    checkForPossibleGameOver();
                }

                nextPlayer();
            }

        }
    }

    /**
     * Prüft ob ein paar gezogen wurde
     *
     * @return true -> es ist ein Paar sonst -> false
     */
    private boolean match() {
        return cards[posOpenCards[0][1]][posOpenCards[0][0]] == cards[posOpenCards[1][1]][posOpenCards[1][0]];
    }

    /**
     * setzt das cardsSolved Array auf die richtigen Werte
     */
    private void setCardsSolved() {
        cardsSolved[posOpenCards[0][1]][posOpenCards[0][0]] = Solved.values()[idxCurrPlayer];
        cardsSolved[posOpenCards[1][1]][posOpenCards[1][0]] = Solved.values()[idxCurrPlayer];
    }

    /**
     * Bereitet den nächste zug vor bei einem Spieler wechsel
     */
    private void resetForNewTurn() {
        if (posOpenCards[1].length != 0) {
            if (!match()) {
                gui.hideCard(posOpenCards[0]);
                gui.hideCard(posOpenCards[1]);
            }
            posOpenCards = new int[2][0];
        }
    }

    /**
     * Überprüft ob es ein gameOver gibt und leitet weiter an die GUI
     */
    private void checkForPossibleGameOver() {
        if (allSolved()) {
            gui.onGameEnd(getWinnerName());
        }
    }

    /**
     * setzt die posOpenCards und benutze gui.showCard
     *
     * @param id id für die posOpenCards
     * @param pos Karten Position
     */
    private void setCardAndShow(int id, int[] pos) {
        posOpenCards[id] = pos;
        gui.showCard(pos, cards[pos[1]][pos[0]]);
    }

    /**
     * Übergibt dem nächste Spieler den zug
     */
    private void nextPlayer() {
        idxCurrPlayer = (idxCurrPlayer + 1) % players.length;
        gui.setCurrentPlayer(players[idxCurrPlayer]);
    }

}
