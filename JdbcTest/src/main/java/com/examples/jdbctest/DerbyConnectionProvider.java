package com.examples.jdbctest;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.apache.derby.jdbc.EmbeddedDataSource;

/**
 * Implementiert die <code>DbConnectionProviderInterface</code> für Apache
 * Derby.
 * 
 * @see DbConnectionProviderInterface
 */
class DerbyConnectionProvider implements DbConnectionProviderInterface, AutoCloseable {
    private static final Logger logger;

    static {
        logger = Logger.getLogger(DerbyConnectionProvider.class.getCanonicalName());
    }

    private final Path dbFilePath;
    private final String user;
    private final char[] password;

    /**
     * Erstellt eine neue Instanz von <code>DerbyConnectionProvider</code>.
     * 
     * @param dbFilePath Der Pfad der Datei, welche die Datenbank enthält. Wenn
     *                   sie nicht vorhanden ist, wird sie geschaffen.
     * @param user       Der Name des Benutzers der Datenbank.
     * @param password   Das Kennwordt des gegebenen Benutzers.
     */
    public DerbyConnectionProvider(Path dbFilePath, String user, char[] password) {
        this.dbFilePath = dbFilePath;
        this.user = user;
        this.password = password;
    }

    private static String reformatFilePath(Path dbFilePath) {
        return dbFilePath.toAbsolutePath().toString().replace('\\', '/');
    }

    @Override
    public Connection getConnection() throws SQLException {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(String.format("%s;create=true", reformatFilePath(dbFilePath)));
        dataSource.setUser(user);
        dataSource.setPassword(StandardCharsets.UTF_8.encode(CharBuffer.wrap(password)).toString());
        return dataSource.getConnection();
    }

    @Override
    public void close() {
        try {
            if (Files.exists(dbFilePath)) {
                DriverManager.getConnection(String.format("jdbc:derby:%s;shutdown=true", reformatFilePath(dbFilePath)));
            }
        } catch (SQLException sqlex) {
            if (sqlex.getErrorCode() != 45000 && sqlex.getErrorCode() != 50000) {
                logger.severe(sqlex.getMessage());
            }
        }
    }
}
