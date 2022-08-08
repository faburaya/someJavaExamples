package com.examples.jdbctest;

import java.nio.file.Path;
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
     * @param dbPath Der Pfad zur Datei, welche die SQLite-Datenbank enthält.
     */
    public SqliteConnectionProvider(Path dbPath) {
        String dbPathSuffix = dbPath.toAbsolutePath().toString().replace('\\', '/');
        url = "jdbc:sqlite:" + dbPathSuffix;
    }

    /**
     * Erstellt eine neue Instanz von <code>SqliteConnectionProvider</code>.
     * 
     * Der Datenbank wird im Arbeitsspeicher gespeichert.
     */
    public SqliteConnectionProvider() {
        url = "jdbc:sqlite::memory:";
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    @Override
    public void close() {
        return; // no-op
    }
}
