package db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * Eine Datenbanktabelle hat einen Namen bzw. Bezeichner und eine feste Spaltenanzahl, die ebenso
 * wie die Bezeichner der einzelnen Spalten und deren Reihenfolge bei der Erzeugung festgelegt
 * werden. Die Tabelle besteht darüber hinaus noch aus einer flexiblen Anzahl von Zeilen, in denen
 * jeweils genau so viele Werte in Form von Zeichenketten stehen, wie es Spalten gibt. Eine neue
 * Zeile wird immer nach der letzten Zeile an die Tabelle angehängt.
 * <p>
 * Der Bezeichner der Datenbanktabelle und die Bezeichner der Spalten müssen einem vorgegebenen
 * Muster folgen um gültig zu sein. Ein valider Bezeichner besteht stets aus einem Zeichen aus der
 * Menge [a-zA-Z] gefolgt von einer beliebigen Anzahl von Zeichen aus der Menge [a-zA-Z0-9_].
 *
 * @author kar, mhe, tti, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
public final class DBTable {

    /**
     * Tabellenbezeichner
     */
    private final String id;
    /**
     * Spaltenbezeichner
     */
    private final List<String> cols;

    /**
     * Werte der Datenbanktabelle
     */
    private final List<List<String>> values;

    /**
     * Erzeugt eine leere Datenbanktabelle mit dem Bezeichner anId und den Spaltenbezeichnern
     * someColIds. Ein Iterator der Collection someColIds muss die Spaltennamen in der Reihenfolge
     * liefern, in der sie in der Tabelle stehen sollen.
     *
     * @param anId       Bezeichner der Datenbanktabelle, die erzeugt werden soll.
     * @param someColIds Spaltenbezeichner
     * @pre anId != null
     * @pre someColIds != null
     * @pre der Bezeichner anId muss gültig sein.
     * @pre someColIds muss mindestens einen Wert enthalten
     * @pre Alle Werte in someColIds müssen gültige Spaltenbezeichner sein
     * @pre Alle Spaltenbezeichner müssen eindeutig sein
     */
    public DBTable(final String anId, final Collection<String> someColIds) {
        assert anId != null;
        assert someColIds != null;
        assert isValidIdentifier(anId);
        assert !someColIds.isEmpty();
        assert areValidIdentifiers(someColIds);
        assert areOnlyUniqueValues(someColIds);


        this.cols = new ArrayList<>();
        this.cols.addAll(someColIds);
        this.id = anId;
        this.values = new ArrayList<>();
    }

    /**
     * Liefert den Bezeichner der Datenbanktabelle.
     *
     * @return Bezeichner der Datenbanktabelle.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Liefert die Spaltenanzahl der Datenbanktabelle.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der Spaltenbezeichner dieser Tabelle
     * und f(N) = 1.
     *
     * @return Spaltenanzahl der Datenbanktabelle.
     */
    public int getColCnt() {
        //O(1)
        return this.cols.size();
    }

    /**
     * Liefert die Zeilenanzahl der Datenbanktabelle.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Zeilen in der Tabelle
     * und f(N) = 1.
     *
     * @return Zeilenanzahl der Datenbanktabelle.
     */
    public int getRowCnt() {
        //O(1)
        return values.size();
    }

    /**
     * Prüft, ob die Tabelle eine Spalte mit dem Bezeichner aColId hat.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der Spaltenbezeichner dieser Tabelle
     * und f(N) = N.
     *
     * @param aColId Bezeichner, der geprüft wird
     * @return boolscher Wert, der angibt, ob die Tabelle eine Spalte mit dem Bezeichner aColId hat
     * @pre aColId != null
     * @pre der Bezeichner aColId muss gültig sein
     */
    public boolean hasCol(final String aColId) {
        assert aColId != null;
        assert isValidIdentifier(aColId);

        //O(N)
        return this.cols.contains(aColId);
    }

    /**
     * Prüft, ob die Zeichenketten in someColIds Spaltenbezeichner dieser Tabelle sind.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der Spaltenbezeichner der übergebenen
     * Collection und f(N) = N^2.
     *
     * @param someColIds Bezeichner, die geprüft werden
     * @return boolscher Wert, der angibt, ob alle Zeichenketten in someColIds Spaltenbezeichner
     * dieser Tabelle sind
     * @pre someColIds != null
     * @pre someColIds muss mindestens einen Wert enthalten
     * @pre Alle Werte in someColIds müssen gültige Spaltenbezeichner sein
     */
    public boolean hasCols(final Collection<String> someColIds) {
        assert someColIds != null;
        assert !someColIds.isEmpty();
        assert areValidIdentifiers(someColIds);

        //O(M*N)
        return this.cols.containsAll(someColIds);
    }

    /**
     * Liefert eine seiteneffektfreie Liste der Spaltenbezeichner. Die Reihenfolge entspricht dabei
     * der Reihenfolge der Spalten in der Tabelle.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der Spaltenbezeichner dieser Tabelle
     * und f(N) = N.
     *
     * @return Liste der Spaltenbezeichner.
     */
    public List<String> getColIds() {
        List<String> clone = new ArrayList<>(this.getColCnt());
        //O(N)
        clone.addAll(this.cols);
        return clone;
    }

    /**
     * Fügt die Werte von row in der angegebenen Reihenfolge als letzte Zeile in die Tabelle ein.
     * Ein Iterator der Collection someColIds muss die Inhalte der Zeile in der Reihenfolge liefern,
     * in der sie in der Tabelle stehen sollen.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Zeilen in der Tabelle
     * und f(N) = 1.
     *
     * @param row Werte, für die letzte Zeile
     * @pre row != null
     * @pre Die Anzahl der Werte in row muss der Spaltenanzahl der Tabelle entsprechen.
     */
    public void appendRow(final Collection<String> row) {
        assert row != null;
        assert row.size() == getColCnt();

        List<String> elem = new ArrayList<>(row.size());
        //O(M)
        elem.addAll(row);
        //O(1)
        this.values.add(elem);
    }

    /**
     * Entfernt alle Zeilen aus dieser Tabelle, bei denen ein Test über dem Wert in der Spalte, die
     * mit aColId bezeichnet ist, erfolgreich ist.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Zeilen in der Tabelle
     * und f(N) = N^2.
     *
     * @param aColId Bezeichner der Spalte, deren Werte für den Test herangezogen werden sollen.
     * @param p      Ein Predicate-Objekt zum Testen des jeweiligen Spaltenwertes
     * @pre aColId != null
     * @pre p != null
     * @pre der Bezeichner aColId muss gültig sein
     * @pre die Tabelle muss eine Spalte mit dem Bezeichner aColId haben
     */
    public void removeRows(final String aColId, final Predicate<String> p) {
        assert aColId != null;
        assert p != null;
        assert isValidIdentifier(aColId);
        assert this.cols.contains(aColId);

        List<List<String>> result = new ArrayList<>();
        int colID = this.cols.indexOf(aColId);
        for (List<String> l : this.values) {
            if (!p.test(l.get(colID))) {
                //O(1)
                result.add(l);
            }

        }
        this.values.clear();
        //O(N)
        this.values.addAll(result);
    }

    /**
     * Löscht alle Zeilen der Tabelle.
     *
     * @post die Tabelle enthält keine Zeilen.
     */
    public void removeAllRows() {
        this.values.clear();

        assert this.values.isEmpty();
    }

    /**
     * Sortiert die Zeilen dieser Tabelle anhand der Werte in der Spalte mit dem Bezeichner aColId
     * in der Sortierreihenfolge sortDir.
     *
     * @param aColId  Bezeichner der Spalte, nach der sortiert werden soll.
     * @param sortDir Reihenfolge, nach der sortiert werden soll.
     * @pre aColId != null
     * @pre sortDir != null
     * @pre der Bezeichner aColId muss gültig sein
     * @pre die Tabelle muss eine Spalte mit dem Bezeichner aColId haben
     */
    public void sort(final String aColId, final SortDirection sortDir) {
        assert aColId != null;
        assert sortDir != null;
        assert isValidIdentifier(aColId);
        assert this.cols.contains(aColId);

        int colId = this.cols.indexOf(aColId);
        Comparator<List<String>> comp = new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                return o1.get(colId).compareTo(o2.get(colId));
            }
        };

        if (sortDir.equals(SortDirection.ASC)) {
            //O(N log(N))
            Collections.sort(this.values, comp);
        } else {
            //O(N log(N))
            Collections.sort(this.values, comp.reversed());
        }
    }

    /**
     * Erzeugt eine Tabelle mit dem Bezeichner newTableID, die alle Zeilen enthält, bei denen ein
     * Test über dem Wert in der Spalte, die mit aColId bezeichnet ist, erfolgreich ist.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Zeilen in der Tabelle
     * und f(N) = N.
     *
     * @param aColId     Bezeichner der Spalte, deren Werte für den Vergleich herangezogen werden
     *                   sollen.
     * @param p          Ein Predicate-Objekt zum Testen des jeweiligen Spaltenwertes
     * @param newTableId Bezeichner der erzeugten Tabelle.
     * @return erzeugte Tabelle.
     * @pre aColId != null
     * @pre p != null
     * @pre newTableId != null
     * @pre der Bezeichner aColId muss gültig sein
     * @pre die Tabelle muss eine Spalte mit dem Bezeichner aColId haben
     * @pre der Bezeichner newTableId muss gültig sein
     */
    public DBTable select(final String aColId, final Predicate<String> p, final String newTableId) {
        assert aColId != null;
        assert p != null;
        assert newTableId != null;
        assert isValidIdentifier(aColId);
        assert hasCol(aColId);
        assert isValidIdentifier(newTableId);

        DBTable result = new DBTable(newTableId, this.cols);
        int colID = this.cols.indexOf(aColId);
        for (int i = 0; i < this.values.size(); i++) {
            if (p.test(this.values.get(i).get(colID))) {
                //O(1)
                result.appendRow(this.values.get(i));
            }
        }

        return result;

    }

    /**
     * Erzeugt eine Tabelle mit dem Bezeichner newTableID, die alle Spalten enthält, deren
     * Bezeichner in someColIds aufgeführt sind, dabei wird die Reihenfolge der Spalten aus
     * someColIds übernommen. Ein Iterator der Collection someColIds muss demzufolge die
     * Spaltennamen in der Reihenfolge liefern, in der sie in der erzeugten Tabelle stehen sollen.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Zeilen in der Tabelle
     * und f(N) = N.
     *
     * @param someColIds Bezeichner der Spalten, die in die erzeugte Tabelle übernommen werden.
     *                   Die Reihenfolge der Spalten in someColIds entspricht der in der erzeugten
     *                   Tabelle.
     * @param newTableId Bezeichner der Tabelle, die erzeugt wird.
     * @return die erzeugte Tabelle
     * @pre someColIds != null
     * @pre newTableId != null
     * @pre someCoIds muss mindestens einen Spaltenbezeichner enthalten
     * @pre zu allen Einträgen in someColIds gibt es eine entsprechende Spalte in der Tabelle
     * @pre der Bezeichner newTableId muss gültig sein
     */
    public DBTable project(final Collection<String> someColIds, final String newTableId) {
        assert someColIds != null;
        assert newTableId != null;
        assert !someColIds.isEmpty();
        assert this.hasCols(someColIds);
        assert isValidIdentifier(newTableId);

        DBTable result = new DBTable(newTableId, someColIds);
        int[] connection = new int[someColIds.size()];

        for (int i = 0; i < connection.length; i++) {
            connection[i] = this.cols.indexOf(result.cols.get(i));
        }
        for (int i = 0; i < this.values.size(); i++) {
            List<String> newRow = new ArrayList<>();

            for (int j = 0; j < connection.length; j++) {
                //O(1)
                newRow.add(this.values.get(i).get(connection[j]));
            }
            //O(1)
            result.appendRow(newRow);
        }
        return result;
    }


    /**
     * Führt eine join-Operation mit der aktuellen und der übergebenen Tabelle other durch. Hierbei
     * wird eine neue Tabelle mit dem Bezeichner newTableID erzeugt, die alle Spalten beider
     * Tabellen enthält: zunächst die Spalten der aktuellen Tabelle und danach die Spalten der
     * übergebenen Tabelle. In der neuen Tabelle befinden sich alle Zeilen, in denen die Werte an
     * den Positionen der übergebenen Spaltenbezeichner (thisColId für die aktuelle Tabelle und
     * otherColId für die übergebene Tabelle) übereinstimmen. Es bleibt die Reihenfolge der Zeilen
     * aus der aktuellen Tabelle bzw. der Tabelle other erhalten.
     * <p>
     * Die Spaltenbezeichner der neuen Tabelle werden aus den Spaltenbezeichnern der beiden
     * vorhandenen Tabellen erzeugt und zwar nach dem Schema, dass vor jeden vorhandenen Bezeichner
     * der Name der entsprechenden Ursprungstabelle gefolgt von einem Unterstrich geschrieben wird.
     *
     * @param other      die Tabelle, mit der this gejoint werden soll
     * @param newTableId Bezeichner der Tabelle, die erzeugt wird.
     * @param thisColId  Spaltenbezeichner der Spalte deren Werte in this verglichen werden.
     * @param otherColId Spaltenbezeichner der Spalte deren Werte in other verglichen werden.
     * @return die erzeugte Tabelle
     * @pre other != null
     * @pre thisColId != null
     * @pre otherColId != null
     * @pre newTableId != null
     * @pre der Bezeichner newTableId muss gültig sein.
     * @pre die Tabellennamen der beiden Tabellen this und other müssen verschieden sein.
     * @pre der Bezeichner thisColId muss in der Tabelle this vorhanden sein
     * @pre der Bezeichner otherColId muss in der Tabelle other vorhanden sein
     * @post die Bezeichner der neuen Tabellenspalten müssen gültig sein
     * @post die Bezeichner der neuen Tabellenspalten müssen eindeutig sein
     */
    public DBTable equijoin(final DBTable other, final String thisColId, final String otherColId,
                            final String newTableId) {
        assert other != null;
        assert thisColId != null;
        assert otherColId != null;
        assert newTableId != null;
        assert isValidIdentifier(newTableId);
        assert !this.id.equals(other.id);
        assert this.cols.contains(thisColId);
        assert other.cols.contains(otherColId);
        assert areValidIdentifiers(other.cols);
        assert areOnlyUniqueValues(other.cols);

        List<String> allCols = new ArrayList<>();
        for (String s : this.cols) {
            allCols.add(this.id + "_" + s);
        }
        for (String s : other.cols) {
            allCols.add(other.id + "_" + s);
        }

        DBTable result = new DBTable(newTableId, allCols);

        int colThis = this.cols.indexOf(thisColId);
        int colOther = other.cols.indexOf(otherColId);
        for (int i = 0; i < this.values.size(); i++) {
            for (int j = 0; j < other.values.size(); j++) {
                if (this.values.get(i).get(colThis).equals(other.values.get(j).get(colOther))) {
                    List<String> row = new ArrayList<>();
                    row.addAll(this.values.get(i));
                    row.addAll(other.values.get(j));
                    result.appendRow(row);
                }
            }
        }
        return result;
    }

    /**
     * Liefert die Stringrepräsentation der Datenbanktabelle. Die Stringrepräsentation erfolgt
     * linksbündig blockweise in der Reihenfolge in der die Spalten in der Tabelle vorkommen. Sie
     * ist wie folgt aufgebaut:
     * <ul>
     * <li>Ein Feld (Block) hat immer die Breite des längsten Eintrages, der in dessen Spalte
     * vorkommt</li>
     * <li>Felder sind durch " | " (Leerzeichen, Pipe, Leerzeichen) voneinander getrennt. Es
     * befinden sich zudem eine Pipe und ein Leerzeichen am Anfang jeder Zeile. Auf die letzte Pipe
     * in einer Zeile folgt kein Leerzeichen mehr.</li>
     * <li>In der ersten Zeile stehen (entsprechend formatiert) die Spalten der
     * Datenbanktabelle.</li>
     * <li>Es folgt eine Zeile, die die Trennung von Kopfzeile und Daten symbolisiert. Die Felder
     * dieser Zeile sind komplett mit Minuszeichen gefüllt (keine Leerzeichen an den Pipes)</li>
     * <li>Es folgen die Zeilen der Datenbanktabelle in der Reihenfolge, in der sie in der
     * Datenbanktabelle gespeichert sind (in entsprechender blockweiser Formatierung).</li>
     * </ul>
     * <p>
     * Zeilenumbrüche (Unix, DOS, MacOs) innerhalb der Felder der Tabelle werden zugunsten der
     * besseren Lesbarkeit hierbei entfernt und durch ein einzelnes Leerzeichen ersetzt.
     * <p>
     * Es werden stets Unix-Zeilenumbrüche (\n) verwendet.
     *
     * @return Stringrepräsentation der Datenbanktabelle.
     */
    public String printTable() {
        return toString();
    }

    /**
     * Gibt die Stringrepresentation einer Zeile zurück
     *
     * @param maxLengths maximale Länge der einzelnen Spalten
     * @param line       Zeile mit String werten
     * @return Stringbuilder
     */
    private String getRowString(List<Integer> maxLengths, List<String> line) {
        StringBuilder sb = new StringBuilder();
        String seperator = " |";
        sb.append("|");
        for (int i = 0; i < line.size(); i++) {
            String elem = String.format(" %-" + maxLengths.get(i) + "s" + seperator,
                    line.get(i).replaceAll("[\n\r\025]", " "));
            sb.append(elem);
        }
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Gibt die maximale Länge der Strings der entsprechenden Spalte col an
     *
     * @param col ausgewählte Spalte
     * @return maximale Länge
     */
    private int getMaxLength(int col) {
        int max = this.cols.get(col).length();
        for (List<String> line : this.values) {
            max = line.get(col).length() > max ? line.get(col).length() : max;
        }
        return max;
    }

    @Override
    public String toString() {
        List<Integer> maxLengths = new ArrayList<>();
        for (int i = 0; i < this.cols.size(); i++) {
            maxLengths.add(getMaxLength(i));
        }

        StringBuilder sb = new StringBuilder();

        sb.append(getRowString(maxLengths, this.cols));

        sb.append("|");
        for (int i = 0; i < maxLengths.size(); i++) {
            for (int j = 0; j < maxLengths.get(i) + 2; j++) {
                sb.append("-");
            }
            sb.append("|");
        }
        sb.append("\n");

        for (int i = 0; i < this.values.size(); i++) {
            sb.append(getRowString(maxLengths, this.values.get(i)));
        }
        return sb.toString();
    }


    /**
     * Prüft, ob die Zeichenkette str ein gültiger Bezeichner für eine Datenbanktabelle bzw. die
     * Spalte einer Datenbanktabelle ist. Ein valider Bezeichner besteht aus einem Zeichen aus der
     * Menge [a-zA-Z] gefolgt von einer beliebigen Anzahl von Zeichen aus der Menge [a-zA-Z0-9_].
     *
     * @param str Zeichenkette, die geprüft wird.
     * @return boolscher Wert, der angibt, ob die Zeichenkette str ein gültiger Bezeichner ist.
     * @pre str != null
     */
    public static boolean isValidIdentifier(final String str) {
        assert str != null;

        return str.matches("[a-zA-Z][a-zA-Z0-9_]*");
    }

    /**
     * Prüft, ob in dem Array aus Zeichenketten strs nur gültige Bezeichner für eine
     * Datenbanktabelle bzw. die Spalte einer Datenbanktabelle ist. Ein valider Bezeichner besteht
     * aus einem Zeichen aus der Menge [a-zA-Z] gefolgt von einer beliebigen Anzahl von Zeichen aus
     * der Menge [a-zA-Z0-9_].
     *
     * @param strs Bezeichner, die geprüft werden
     * @return boolscher Wert, der angibt, ob das Array strs nur gültige Bezeichner enthält.
     * @pre strs != null
     */
    public static boolean areValidIdentifiers(final Collection<String> strs) {
        assert strs != null;

        for (String s : strs) {
            if (!isValidIdentifier(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft, ob alle übergebenen Strings eindeutig sind. Sollte mindestens eine Zeichenkette
     * doppelt vorkommen, so gibt die Methode false zurück.
     *
     * @param strs Bezeichner, die geprüft werden
     * @return true, wenn alle Zeichenketten einmalig sind
     * @pre strs != null
     */
    public static boolean areOnlyUniqueValues(final Collection<String> strs) {
        assert strs != null;

        Set<String> set = new HashSet<>();
        for (String elem : strs) {
            if (!set.add(elem)) {
                return false;
            }
        }
        return true;
    }

}
