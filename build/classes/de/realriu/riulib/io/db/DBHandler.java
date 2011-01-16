
package de.realriu.riulib.io.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;



/**
 * Handlerklasse um Verbindung mit einer MySQL-Datenbank aufzunehmen und mit dieser zu kommunizieren.
 * @author riu
 * @version 2.0
 */
public class DBHandler {
        private static DBHandler defaultDB = null;
        private static DBHandler lastDB = null;
	private String DBHost;
	private String DBUser;
	private String DBPass;
	private String DBName;

	/**
	 * Objekt, welches die verbindung zur Datenbank verwaltet.
	 */
	private Connection con;


	/**
	 * Erzeugt ein neues DBHandler Objekt, das sofort eine Verbindung mit den übergebenen Parametern aufnimmt.
	 * @param url - Hostadresse
	 * @param dbname - Datenbankname
	 * @param user - Benutzername
	 * @param pass - Passwort
	 * @throws SQLException
	 */
	public DBHandler(String url, String dbname, String user, String pass) throws SQLException {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.setLoginTimeout(6);
                        con = DriverManager.getConnection("jdbc:mysql://"+url+"/"+dbname+"?useUnicode=true&characterEncoding=UTF-8",user, pass);
			if(con != null){
				DBHost = url;
				DBUser = user;
				DBPass = pass;
				DBName = dbname;
			}
                        
		} catch (ClassNotFoundException e) {
			throw new SQLException(e.getMessage(), e.getCause());
		} catch (SQLException e) {
			throw new SQLException(e.getMessage(), e.getCause());
		}

                lastDB = this;
	}
	/**
	 * Erzeugt ein neues DBHandler Objekt, das das übergebene Verbindungsobjekt benutzt.
	 * @param con zu benutzende, aufgebaute Verbindung
	 * @throws SQLException
	 */
	public DBHandler(Connection con) throws SQLException {
		try{
                        this.con = con;

                        if(con == null){
                            throw new NullPointerException("Kein Connection Objekt übergeben");
                        }

                        DBHost = con.getMetaData().getURL();
                        DBUser = con.getMetaData().getUserName();
                        DBPass = null;
                        DBName = con.getMetaData().getSchemaTerm();
			
		} catch (SQLException e) {
			throw new SQLException(e.getMessage(), e.getCause());
		}

                lastDB = this;
	}


        /**
         * Gibt die zuletzt aufgebaute DBHandler Verbindung zurück.
         * @return letzte Verbindung
         */
        public static DBHandler getLastConnection(){
            return lastDB;
        }

        

	
        /**
         * Gibt die Hosturl zurück.
         * @return hostname/ip
         */
	public String getDBHost(){
		return DBHost;
	}

        /**
         * Gibt den Datenbankuser zurück.
         * @return user
         */
	public String getDBUser(){
		return DBUser;
	}

        /**
         * Gibt das Passwort zurück.
         * @return pw
         */
	public String getDBPass(){
		return DBPass;
	}

        /**
         * Gibt den Datenbanknamen zurück.
         * @return dbname
         */
	public String getDBName(){
		return DBName;
	}

        /**
         * Gibt die MetaDaten der Datenbank zurück.
         * @return metadaten
         * @throws SQLException
         */
	public DatabaseMetaData getMetaData() throws SQLException{
		try {
			return con.getMetaData();
		} catch (SQLException e) {
			throw new SQLException(e.getMessage(), e.getCause());
		}
	}

        /**
         * Schließt die Verbindung<br>
         * MUSS GEMACHT WERDEN DA MAN NUR EINE BEGRENZTE ZAHL AN VERBINDUNGEN OFFEN HALTEN KANN.
         * @throws SQLException
         */
	public void closeConnection() throws SQLException{
		try {
                    lastDB = lastDB == this ? null : lastDB;
                    defaultDB = defaultDB == this ? null : defaultDB;
		    con.close();
                    con = null;
		} catch (SQLException e) {
			throw new SQLException(e.getMessage(), e.getCause());
		}
	}

        /**
         * Prüft ob die Verbindung geschlossen wurde.
         * @return ist Offen?
         * @throws SQLException
         */
	public boolean isClosed() throws SQLException{
	    try {
		return con==null || con.isClosed();
	    } catch (SQLException e) {
		throw new SQLException(e.getMessage(), e.getCause());
	    }
	}


        /**
         * Pingt die Datenbank an.
         * @param timeout wie land gewartet werden soll bevor eine Exception kommt.
         * @return true - wenn die verbindung noch besteht<br>false - falls innerhalb der zeit klar wird das keine verbindung da is.
         * @throws SQLException bei Timeout
         */
	public boolean isReady(int timeout) throws SQLException{
	    try {
		return con.isValid(timeout);
	    } catch (SQLException e) {
		throw new SQLException(e.getMessage(), e.getCause());
	    }
	}

        /**
         * Führt einen SQL-Query aus und gibt ein entsprechendes DBResult zurück.
         * @see DBResult
         * @param query - SQLQuery String
         * @return Ergebnis DBResult
         * @throws SQLException
         */
        public DBResult executeQuery(String query) throws SQLException{
            PreparedStatement st = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            if(st.execute()){
                return new SelectResult(st);
            }else if(st.getUpdateCount()==-1 || st.getGeneratedKeys().next()){
                //st.getGeneratedKeys().beforeFirst();
                return new InsertResult(st);
            }else{
                return new UpdateResult(st);
            }
        }

    @Override
    protected void finalize() throws Throwable {
        if(!isClosed()){
            closeConnection();
        }
    }



}
