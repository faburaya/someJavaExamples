package com.examples.jdbctest;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Implementiert die <code>DbConnectionProviderInterface</code> für SQLite.
 * 
 * @see DbConnectionProviderInterface
 */
class SqliteConnectionProvider implements DbConnectionProviderInterface {
    private final String url;

    /**
     * Erstellt eine neue Instanz von <code>SqliteConnectionProvider</code>.
     * 
     * @param dbPath Der Pfad zur Datei, welche die SQLite-Datenbank enthält,
     *               oder ":memory:", falls sie im Arbeitsspeicher gespeichert
     *               werden muss.
     */
    public SqliteConnectionProvider(String dbPath) {
        if (dbPath != ":memory:") {
            dbPath = Paths.get(dbPath).toAbsolutePath().toString().replace('\\', '/');
        }
        url = "jdbc:sqlite:" + dbPath;
    }

    @Override
    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(url);
    }
}
