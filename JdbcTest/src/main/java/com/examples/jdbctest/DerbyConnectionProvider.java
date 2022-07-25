package com.examples.jdbctest;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.derby.jdbc.EmbeddedDataSource;

/**
 * Implementiert die <code>DbConnectionProviderInterface</code> für Apache
 * Derby.
 * 
 * @see DbConnectionProviderInterface
 */
class DerbyConnectionProvider implements DbConnectionProviderInterface {
    private final String databaseName;
    private final String user;
    private final String password;

    /**
     * Erstellt eine neue Instanz von <code>DerbyConnectionProvider</code>.
     * 
     * @param databaseName Der Pfad der Datei, welche die Datenbank enthält. Wenn
     *                     sie nicht vorhanden ist, wird sie geschaffen.
     * @param user         Der Name des Benutzers der Datenbank.
     * @param password     Das Kennwordt des gegebenen Benutzers.
     */
    public DerbyConnectionProvider(String dbFilePath, String user, String password) {
        String reformattedFilePath = Paths.get(dbFilePath).toAbsolutePath().toString().replace('\\', '/');
        databaseName = String.format("%s;create=true", reformattedFilePath);
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(databaseName);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        return dataSource.getConnection();
    }
}
