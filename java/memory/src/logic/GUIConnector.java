package logic;

import logic.PairsLogic.Symbol;

/**
 * Interface, welches die Logik des Spiels Memory benutzt, um mit der Oberfläche zu kommunizieren. 
 *
 * @author cei
 */
public interface GUIConnector {

    /**
     * Zeigt das Symbol einer Karte an der gegebenen Position. 
     *
     * @param pos die Position an der die Karte aufgedeckt werden soll 
     * @param symbol das Symbol das dargestellt werden soll an der gegebenen Position 
     */
    void showCard(int[] pos, Symbol symbol);

    /**
     * Versteckt das Symbol der Karte an der gegebenen Position. 
     *
     * @param pos Position an der das Symbol wieder versteckt werden soll 
     */
    void hideCard(int[] pos);

    /**
     * Setzt den Namen des aktuellen Spielers auf der Oberfläche.
     *
     * @param name Name des aktuellen Spielers
     */
    void setCurrentPlayer(String name);

    /**
	 * Wird aufgerufen, wenn das Spiel von einem Spieler gewonnen worden ist. Muss
	 * den Namen des Gewinners auf der Oberfläche darstellen und muss außerdem sicherstellen, 
	 * dass nicht weitergespielt werden kann (z.B. indem Komponenten deaktiviert werden). 
	 * Gibt es keinen Gewinner, da beide Spieler die gleiche Anzahl von Paaren gefunden
	 * haben, so ist "niemand" der Gewinner. 
     *
     * @param winnerName Name des Spielers, der gewonnen hat; null, wenn es keinen Gewinner
	 * gibt, aber das Spiel zu Ende ist (da alle Paare aufgedeckt worden sind) 
     */
    void onGameEnd(String winnerName);

}
