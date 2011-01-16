

package de.realriu.riulib.io.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;

/**
 *
 * @author riu
 */
public class SelectResult extends DBResult{

    public SelectResult(Statement s) throws SQLException {
        ResultSet rs = s.getResultSet();
        ResultSetMetaData meta = rs.getMetaData();
        columns = meta.getColumnCount();
        columnNames = new String[columns];

        for(int i=0; i<columns; i++){
            columnNames[i] = meta.getColumnLabel(i+1);
        }

        while(rs.next()){
            rows++;

            TreeMap<Integer, String> t = new TreeMap<Integer, String>();
            for(int i=0; i<columns; i++){
                t.put(i, rs.getString(i+1));
            }
            
            data.add(t);
        }

        s.close();

        type = ResultType.SelectResult;
    }



    @Override
    public String getCell(int zeile, int spalte) {
        if(zeile<0 || zeile>=rows)
            throw new DBResultSetOutOfBoundsException("Ungültige Zeile angegeben. (Mögliche Zeilen reichen von 0 bis "+(rows-1)+")");
        if(spalte<0 || spalte>=columns){
            throw new DBResultSetOutOfBoundsException("Ungültige Spalte angegeben. (Mögliche Spalten reichen von 0 bis "+(columns-1)+")");
        }

        return data.get(zeile).get(spalte);
    }

    @Override
    public String getCell(int zeile, String spalte) {
        if(zeile<0 || zeile>=rows)
            throw new DBResultSetOutOfBoundsException("Ungültige Zeile angegeben ("+spalte+"). (Mögliche Zeilen reichen von 0 bis "+(rows-1)+")");

        String names = "";
        for(int i=0; i<columns; i++){
            names += i<columns-1 ? columnNames[i]+", " : columnNames[i];
            if(spalte.equalsIgnoreCase(columnNames[i])){
                return data.get(zeile).get(i);
            }
        }

        throw new DBResultSetOutOfBoundsException("Ungültige Spalte angegeben. (Mögliche Spalten sind "+names+")");
    }

    @Override
    public String getGeneretedKeyCell(int zeile, int spalte) {
        throw new UnsupportedOperationException("Ein Select Statement generiert keine Keys. Benutze getCell um Zellen auszulesen.");
    }

    @Override
    public String getGeneretedKeyCell(int zeile, String spalte) {
        throw new UnsupportedOperationException("Ein Select Statement generiert keine Keys. Benutze getCell um Zellen auszulesen.");
    }

    @Override
    public int getAffectedRowCount() {
        throw new UnsupportedOperationException("Ein Select Statement verändert keine Zeilen. ");
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getGeneratedKeysColumns() {
        throw new UnsupportedOperationException("Ein Select Statement generiert keine Keys. Benutze getColumns() um die Spaltenanzahl herauszufinden.");
    }

    @Override
    public int getGeneratedKeysRows() {
        throw new UnsupportedOperationException("Ein Select Statement generiert keine Keys. Benutze getRows() um die Zeilenanzahl herauszufinden.");
    }

    @Override
    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public String[] getGeneratedKeysColumnNames() {
        throw new UnsupportedOperationException("Ein Select Statement generiert keine Keys. Benutze getColumnNames() um die Spaltennamen herauszufinden.");
    }

}
