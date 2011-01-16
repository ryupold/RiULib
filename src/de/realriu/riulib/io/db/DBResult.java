
package de.realriu.riulib.io.db;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Das Ergebnis eines Querys
 * @see ResultType
 * @author riu
 */
public abstract class DBResult {
    public DBResult(){
        
    }

    /**
     * Die Art des QueryResults
     */
    public static enum ResultType{

        /**
         * Ergebnis in Tabellenartiger form, z.B. von einem Select Statement
         */
        SelectResult,

        /**
         * Ergebnis ist die Menge betroffener Zeilen, z.B. bei einem Update Query
         */
        UpdateResult,

        /**
         * Dieser Ergebnistyp speichert die Generierten Keys, z.B. bei einem Insert Query
         */
        InsertResult
    }



    /**
     * Wird geworfen wenn man eine Ungültige Zeile oder Spalte angibt<br>
     * z.B.: zeile &lt; 0 oder einen Spaltennamen der nicht existiert.
     */
    public static class DBResultSetOutOfBoundsException extends RuntimeException{

        public DBResultSetOutOfBoundsException(String m) {
            super(m);
        }
        
    }

    /**
     * Gibt den Inhalt der angegeben Zelle aus einem ResultSet zurück.<br>
     * Falls in der Inhalt der Zelle NULL ist wird das Java null zurückgegeben. (Also aufpassen wegen Nullpointer ;-))
     * @param zeile - (Beginnend bei 0)
     * @param spalte - (Beginnend bei 0)
     * @return Inhalt der Zelle als String
     * @throws DBResultSetOutOfBoundsException - Wenn eine ungültige Zeile/Spalte angegeben wird.
     */
    public abstract String getCell(int zeile, int spalte);

    /**
     * Gibt den Inhalt der angegeben Zelle aus einem ResultSet zurück.<br>
     * * Falls in der Inhalt der Zelle NULL ist wird das Java null zurückgegeben. (Also aufpassen wegen Nullpointer ;-))<br>
     * Beispiel: <br>Select nr, gehaltprostunde as gehalt from arbeiter;<br>
     * Um nun das Gehalt des 4. arbeiters zu erhalten: getCell(3, "gehalt");
     * @param zeile - (Beginnend bei 0)
     * @param spalte - Name der Spalte als String
     * @return Inhalt der Zelle als String
     * @throws DBResultSetOutOfBoundsException - Wenn eine ungültige Zeile/Spalte angegeben wird.
     */
    public abstract String getCell(int zeile, String spalte);

    /**
     * Falls ein Insertquery abgeschickt wurde, werden mit dieser Mehtode die Automatisch generierten Keys abgefragt.
     * <br>
     * @param zeile - Beginnend bei 0
     * @param spalte - Beginnend bei 0
     * @return InsertedKeyZelle
     */
    public abstract String getGeneretedKeyCell(int zeile, int spalte);

    /**
     * Falls ein Insertquery abgeschickt wurde, werden mit dieser Mehtode die Automatisch generierten Keys abgefragt.
     * <br>
     * @param zeile - Beginnend bei 0
     * @param spalte - Beginnend bei 0
     * @return InsertedKeyZelle
     */
    public abstract String getGeneretedKeyCell(int zeile, String spalte);

    /**
     * Gibt die Anzahl der betroffenen Zeilen zurück.<br>
     * z.B. Bei einem Update oder Delete Query.
     * @return Anzahl der Betroffenen Zeilen
     */
    public abstract int getAffectedRowCount();

    /**
     * Anzahl der Spalten bei einem Select oder sonstigen Querys, die eine Tabelle zurückgeben.
     * @return Spaltenanzahl
     */
    public abstract int getColumns();

    /**
     * Anzahl der Zeilen bei einem Select oder sonstigen Querys, die eine Tabelle zurückgeben.
     * @return Zeilenanzahl
     */
    public abstract int getRows();


    /**
     * Anzahl der Automatisch generierten Keys Spalten.
     * @return Spalten
     */
    public abstract int getGeneratedKeysColumns();

    /**
     * Anzahl der Automatisch generierten Keys Zeilen.
     * @return Zeilen
     */
    public abstract int getGeneratedKeysRows();

    /**
     * Gibt die Namen der Spalten bei einem SelectResult zurück.<br>
     * z.B. Select nr, gehaltprostunde as gehalt from arbeiter;<br>
     * getColumnNames()[0] => nr    /   getColumnNames()[1] => gehalt
     * @return Spaltennamen
     */
    public abstract String[] getColumnNames();

    /**
     * 
     * Wie getCoulumnNames() nur für Automatisch generierte Keys
     * @see getColumnNames()
     * @return Spaltennamen für automatisch generierte Keys
     */
    public abstract String[] getGeneratedKeysColumnNames();


    /**
     * Gibt den Type des Results zurück
     * @return
     */
    public ResultType getType(){
        return type;
    }


    

    protected ArrayList<TreeMap<Integer, String>> data = new ArrayList<TreeMap<Integer, String>>();
    protected ArrayList<TreeMap<Integer, String>> generatedKeys = new ArrayList<TreeMap<Integer, String>>();
    protected int columns;
    protected int rows;
    protected int agColumns;
    protected int agRows;
    protected int affected;
    protected String[] columnNames;
    protected String[] autoKeysColumnNames;
    protected ResultType type;
}
