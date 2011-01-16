
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
public class InsertResult extends DBResult{

    public InsertResult(Statement s) throws SQLException {
        ResultSet rs = s.getGeneratedKeys();
        ResultSetMetaData meta = rs.getMetaData();
        agColumns = meta.getColumnCount();
        autoKeysColumnNames = new String[agColumns];

        for(int i=0; i<agColumns; i++){
            autoKeysColumnNames[i] = meta.getColumnLabel(i+1);
        }

        do{
            agRows++;

            TreeMap<Integer, String> t = new TreeMap<Integer, String>();
            for(int i=0; i<agColumns; i++){
                t.put(i, rs.getString(i+1));
            }

            generatedKeys.add(t);
        }while(rs.next());

        s.close();

        type = ResultType.InsertResult;
    }

    @Override
    public String getCell(int zeile, int spalte) {
        throw new UnsupportedOperationException("Um auf die generierten Keys zuzugreifen benutze getGeneretedKeyCell().");
    }

    @Override
    public String getCell(int zeile, String spalte) {
        throw new UnsupportedOperationException("Um auf die generierten Keys zuzugreifen benutze getGeneretedKeyCell().");
    }

    @Override
    public String getGeneretedKeyCell(int zeile, int spalte) {
        if(zeile<0 || zeile>=agRows)
            throw new DBResultSetOutOfBoundsException("Ungültige Zeile angegeben. (Mögliche Zeilen reichen von 0 bis "+(rows-1)+")");
        if(spalte<0 || spalte>=agColumns){
            throw new DBResultSetOutOfBoundsException("Ungültige Spalte angegeben. (Mögliche Spalten reichen von 0 bis "+(columns-1)+")");
        }

        return generatedKeys.get(zeile).get(spalte);
    }

    @Override
    public String getGeneretedKeyCell(int zeile, String spalte) {
        if(zeile<0 || zeile>=agRows)
            throw new DBResultSetOutOfBoundsException("Ungültige Zeile angegeben. (Mögliche Zeilen reichen von 0 bis "+(rows-1)+")");

        String names = "";
        for(int i=0; i<agColumns; i++){
            names += i<agColumns-1 ? autoKeysColumnNames[i]+", " : autoKeysColumnNames[i];
            if(spalte.equalsIgnoreCase(autoKeysColumnNames[i])){
                return generatedKeys.get(zeile).get(i);
            }
        }

        throw new DBResultSetOutOfBoundsException("Ungültige Spalte angegeben. (Mögliche Spalten sind "+names+")");
    }

    @Override
    public int getAffectedRowCount() {
        throw new UnsupportedOperationException("Dies ist ein InsertResult hier werden keine Zeilen verändert sondern höchstens Keys generiert.");
    }

    @Override
    public int getColumns() {
        throw new UnsupportedOperationException("Um auf die Spaltenanzahl der generierten Keys zuzugreifen benutze getGeneratedKeysColumns().");
    }

    @Override
    public int getRows() {
        throw new UnsupportedOperationException("Um auf die Zeilenanzahl der generierten Keys zuzugreifen benutze getGeneratedKeysRows().");
    }

    @Override
    public int getGeneratedKeysColumns() {
        return agColumns;
    }

    @Override
    public int getGeneratedKeysRows() {
        return agRows;
    }

    @Override
    public String[] getColumnNames() {
        throw new UnsupportedOperationException("Um auf die Spaltennamen der generierten Keys zuzugreifen benutze getGeneratedKeysColumnNames().");
    }

    @Override
    public String[] getGeneratedKeysColumnNames() {
        return autoKeysColumnNames;
    }
    
}
