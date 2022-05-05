package db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * In einer Datenbank werden Datenbanktabellen verwaltet, die jeweils durch einen eindeutigen
 * Bezeichner (Datentyp String) identifiziert werden. Auch die Datenbank selbst hat einen Bezeichner
 * (Datentyp String).
 * <p>
 * Ein valider Bezeichner besteht stets aus einem Zeichen aus der Menge [a-zA-Z] gefolgt von einer
 * beliebigen Anzahl von Zeichen aus der Menge [a-zA-Z0-9_].
 *
 * @author kar, mhe, tti, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
public final class DB {

    /**
     * ID der Datenbank
     */
    private final String id;
    /**
     * Liste von Tabellen der Datenbank
     */
    private final List<DBTable> tables;

    /**
     * Erzeugt eine leere Datenbank mit dem Bezeichner anId.
     *
     * @param anId Bezeichner der Datenbank, die erzeugt werden soll.
     * @pre anId != null
     * @pre der Bezeichner anId muss gültig sein.
     */
    public DB(final String anId) {
        assert anId != null;
        assert DBTable.isValidIdentifier(anId);

        this.id = anId;
        this.tables = new ArrayList<>();
    }

    /**
     * Liefert den Bezeichner der Datenbank.
     *
     * @return Bezeichner der Datenbank.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Liefert die Anzahl der Tabellen in der Datenbank.
     *
     * @return Anzahl der Tabellen in der Datenbank.
     */
    public int getTableCnt() {
        return this.tables.size();
    }

    /**
     * Fügt die Tabelle tab in die Datenbank ein.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Tabellen in der
     * Datenbank und f(N) = 1.
     *
     * @param tab Tabelle, die in die Datenbank eingefügt werden soll.
     * @pre tab != null
     * @pre es darf keine Tabelle mit demselben Bezeichner wie dem von tab in der Datenbank
     * existieren.
     */
    public void addTable(final DBTable tab) {
        assert tab != null;
        assert !tableExists(tab.getId());

        this.tables.add(tab);
    }

    /**
     * Liefert die Tabelle mit dem Bezeichner anId.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Tabellen in der
     * Datenbank und f(N) = N.
     *
     * @param anId Bezeichner der Tabelle, die geliefert werden soll.
     * @return Tabelle mit dem Bezeichner anId (falls vorhanden, sonst NULL-Referenz).
     * @pre anId != null
     * @pre der Bezeichner anId muss gültig sein.
     */
    public DBTable getTable(final String anId) {
        assert anId != null;
        assert DBTable.isValidIdentifier(anId);

        DBTable result = null;
        for (int i = 0; result == null && i < this.getTableCnt(); i++) {
            if (this.tables.get(i).getId().equals(anId)) {
                result = this.tables.get(i);
            }
        }
        return result;
    }

    /**
     * Liefert eine seiteneffektfreie aufsteigend sortierte Liste der Tabellenbezeichner.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Tabellen in der
     * Datenbank und f(N) =  N * ( Log( N ) + 1 ) .
     *
     * @return aufsteigend sortierte Liste der Tabellenbezeichner.
     */
    public List<String> getTableIds() {
        List<String> result = new ArrayList<>();
        for (DBTable dbt : this.tables) {
            result.add(dbt.getId());
        }
        // O( N * Log( N ) )
        Collections.sort(result);

        return result;
    }

    /**
     * Gibt an, ob eine Tabelle mit dem Bezeichner anId in der Datenbank existiert.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Tabellen in der
     * Datenbank und f(N) = N.
     *
     * @param anId Bezeichner der Tabelle, deren Existenz geprüft werden soll.
     * @return boolscher Wert, der angibt, ob eine Tabelle mit dem Bezeichner anId in der Datenbank
     * existiert.
     * @pre anId != null
     * @pre der Bezeichner anId muss gültig sein.
     */
    public boolean tableExists(final String anId) {
        assert anId != null;
        assert DBTable.isValidIdentifier(anId);

        boolean result = false;
        for (int i = 0; !result && i < this.getTableCnt(); i++) {
            result = this.tables.get(i).getId().equals(anId);
        }
        return result;
    }

    /**
     * Entfernt die Tabelle mit dem Bezeichner anId aus der Datenbank.
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Tabellen in der
     * Datenbank und f(N) = N.
     *
     * @param anId Bezeichner der Tabelle, die aus der Datenbank entfernt werden soll.
     * @pre anId != null
     * @pre der Bezeichner anId muss gültig sein.
     * @post in der Datenbank befindet sich keine Tabelle mit dem Bezeichner anId.
     */
    public void removeTable(final String anId) {
        assert anId != null;
        assert DBTable.isValidIdentifier(anId);
        //O(N)
        this.tables.removeIf(new Predicate<DBTable>() {
            @Override
            public boolean test(DBTable dbTable) {
                return dbTable.getId().equals(anId);
            }
        });
        assert !tableExists(anId);
    }

    /**
     * Entfernt alle Tabellen aus der Datenbank.
     *
     * @post die Datenbank enthält keine Tabellen.
     */
    public void removeAllTables() {
        this.tables.clear();

        assert getTableCnt() == 0;
    }

    /**
     * Liefert alle Bezeichner von Tabellen deren Bezeichner (gemäß compareTo) in einem gegebenen
     * Bereich liegen. Das Ergebnis ist aufsteigend (gemäß compareTo) nach den Bezeichnern der
     * Tabellen sortiert. Dabei ist die Angabe in from als inklusive und die Angabe in to als
     * exklusive zu verstehen.
     * <p>
     * Beispiel: Wenn die Datenbank Tabellen mit den Namen "Gaerten", "Haendler", "Haeuser" und
     * "Inseln" enthält, liefert die Anfrage mit from="H" to="I" die Namen "Haendler" und "Haeuser".
     * <p>
     * Diese Methode arbeitet in O(f(N)), dabei ist N = Anzahl der vorhandenen Tabellen in der
     * Datenbank und f(N) =  N * ( 1 + Log( N ) ).
     *
     * @param from untere Grenze des Suchbereiches, inklusive
     * @param to   obere Grenze des Suchbereiches, exklusive
     * @return sortierte Liste mit den Tabellenbezeichnern
     * @pre from != null
     * @pre to != null
     * @pre from muss ein gültiger Bezeichner sein.
     * @pre to muss ein gültiger Bezeichner sein.
     */
    public List<String> getTableNamesBetween(final String from, final String to) {
        assert from != null;
        assert to != null;
        assert DBTable.isValidIdentifier(from);
        assert DBTable.isValidIdentifier(to);

        Predicate<String> p = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return (s.compareTo(from) >= 0) && (s.compareTo(to) < 0);
            }
        };

        List<String> result = new ArrayList<>();
        for (DBTable dbt : this.tables) {
            if (p.test(dbt.getId())) {
                result.add(dbt.getId());
            }
        }

        // O( N * Log(N) )
        // Sortieren der Ergebnisliste
        Collections.sort(result);

        return result;
    }

    /**
     * Liefert die Stringrepräsentation der Datenbank. Die Stringrepräsentation ist wie folgt
     * aufgebaut:
     * <ul>
     * <li>In der ersten Zeile steht <code>Datenbankname: </code> gefolgt von dem Bezeichner der
     * Datenbank.</li>
     * <li>Die zweite Zeile ist eine Leerzeile.</li>
     * <li>Es folgen Datenbanktabellen in aufsteigender Reihenfolge ihrer Bezeichner in folgender
     * Form:
     * <ul>
     * <li>Vor jeder Tabelle steht <code>Tabellenname: </code> gefolgt von dem Bezeichner der
     * Datenbanktabelle.</li>
     * <li>Danach folgt eine Leerzeile.</li>
     * <li>Es folgt die Stringrepräsentation der Datenbanktabelle.</li>
     * </ul>
     * </li>
     * <li>Nach jeder Tabelle folgt eine Leerzeile.</li>
     * </ul>
     *
     * @return die Stringrepräsentation der Datenbank.
     */
    public String printDB() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Datenbankname: " + getId() + "\n\n");
        List<DBTable> cloneTables = new ArrayList<>();
        cloneTables.addAll(this.tables);

        Collections.sort(cloneTables, new Comparator<DBTable>() {
            @Override
            public int compare(DBTable o1, DBTable o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        for (int i = 0; i < cloneTables.size(); i++) {
            result.append("Tabellenname: " + cloneTables.get(i).getId() + "\n\n");
            result.append(cloneTables.get(i).toString());
            result.append("\n");
        }


        return result.toString();
    }


}
