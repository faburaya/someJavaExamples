package com.examples.jdbctest;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Schnittstelle zur Einrichung einer Verbindung zur Datenbank.
 */
public interface DbConnectionProviderInterface {
    /**
     * Gewährt eine Verbindung zur Datenbank.
     * 
     * @return Eine Verbindung zur Datenbank.
     * @throws SQLException Wenn die Verbindung nicht erstellt werden konnte.
     */
    public Connection getConnection() throws SQLException;
}
