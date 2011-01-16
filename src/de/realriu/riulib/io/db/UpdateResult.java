
package de.realriu.riulib.io.db;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author riu
 */
public class UpdateResult extends DBResult{

    public UpdateResult(Statement s) throws SQLException {
        affected = s.getUpdateCount();
        type = ResultType.UpdateResult;
    }

    @Override
    public String getCell(int zeile, int spalte) {
        throw new UnsupportedOperationException("Bei einem Update oder Delete Query werden nur die Betroffenen Zeilen zurückgegeben. Benutze getAffectedRowCount() um auf sie zuzugreifen.");
    }

    @Override
    public String getCell(int zeile, String spalte) {
        throw new UnsupportedOperationException("Bei einem Update oder Delete Query werden nur die Betroffenen Zeilen zurückgegeben. Benutze getAffectedRowCount() um auf sie zuzugreifen.");
    }

    @Override
    public String getGeneretedKeyCell(int zeile, int spalte) {
        throw new UnsupportedOperationException("Bei einem Update oder Delete Query werden nur die Betroffenen Zeilen zurückgegeben. Benutze getAffectedRowCount() um auf sie zuzugreifen.");
    }

    @Override
    public String getGeneretedKeyCell(int zeile, String spalte) {
        throw new UnsupportedOperationException("Bei einem Update oder Delete Query werden nur die Betroffenen Zeilen zurückgegeben. Benutze getAffectedRowCount() um auf sie zuzugreifen.");
    }

    @Override
    public int getAffectedRowCount() {
        return affected;
    }

    @Override
    public int getColumns() {
        throw new UnsupportedOperationException("Bei einem Update oder Delete Query werden nur die Betroffenen Zeilen zurückgegeben. Benutze getAffectedRowCount() um auf sie zuzugreifen.");
    }

    @Override
    public int getRows() {
        throw new UnsupportedOperationException("Bei einem Update oder Delete Query werden nur die Betroffenen Zeilen zurückgegeben. Benutze getAffectedRowCount() um auf sie zuzugreifen.");
    }

    @Override
    public int getGeneratedKeysColumns() {
        throw new UnsupportedOperationException("Bei einem Update oder Delete Query werden nur die Betroffenen Zeilen zurückgegeben. Benutze getAffectedRowCount() um auf sie zuzugreifen.");
    }

    @Override
    public int getGeneratedKeysRows() {
        throw new UnsupportedOperationException("Bei einem Update oder Delete Query werden nur die Betroffenen Zeilen zurückgegeben. Benutze getAffectedRowCount() um auf sie zuzugreifen.");
    }

    @Override
    public String[] getColumnNames() {
        throw new UnsupportedOperationException("Bei einem Update oder Delete Query werden nur die Betroffenen Zeilen zurückgegeben. Benutze getAffectedRowCount() um auf sie zuzugreifen.");
    }

    @Override
    public String[] getGeneratedKeysColumnNames() {
        throw new UnsupportedOperationException("Bei einem Update oder Delete Query werden nur die Betroffenen Zeilen zurückgegeben. Benutze getAffectedRowCount() um auf sie zuzugreifen.");
    }

}
