package com.examples.jdbctest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;
import java.util.Random;
import java.util.logging.Logger;

public class App {
    private static final Logger logger;

    static {
        logger = Logger.getLogger(App.class.getCanonicalName());
    }

    private static final String TABLE_NAME = "Samples";

    private static boolean createSchemaIfNotPresent(Connection dbConnection) throws SQLException {
        DatabaseMetaData dbMetadata = dbConnection.getMetaData();
        try (ResultSet tables = dbMetadata.getTables(null, null, null, new String[] { "TABLE" })) {
            while (tables.next()) {
                if (tables.getString("TABLE_NAME").toUpperCase().equals(TABLE_NAME.toUpperCase())) {
                    return false;
                }
            }
        }
        try (Statement statement = dbConnection.createStatement()) {
            statement.executeUpdate(String.format("create table %s (id int, name varchar(16))", TABLE_NAME));
        }
        return true;
    }

    private static String generateRandomName(Random randomizer) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            buffer.append((char) (randomizer.nextInt(0, 'Z' - 'A') + 'A'));
        }
        return buffer.toString();
    }

    private static void recordSomeData(Connection dbConnection) throws SQLException {
        dbConnection.setAutoCommit(false);
        try (PreparedStatement statement = dbConnection
                .prepareStatement(String.format("insert into %s (id, name) values (?, ?)", TABLE_NAME))) {
            Random randomizer = new Random();
            randomizer.setSeed(Clock.systemUTC().millis());

            final int rowCount = 3;
            for (int i = 0; i < rowCount; ++i) {
                statement.setInt(1, randomizer.nextInt());
                statement.setString(2, generateRandomName(randomizer));
                statement.addBatch();
            }
            statement.executeBatch();
            dbConnection.commit();
            logger.info(String.format("Der Datenbank wurden %d Zeilen hinzugefügt.", rowCount));
        } catch (Exception ex) {
            dbConnection.rollback();
            throw ex;
        } finally {
            dbConnection.setAutoCommit(true);
        }
    }

    private static void listData(Connection dbConnection) throws SQLException {
        System.out.println("Alle Zeilen in der Datenbank:");
        try (Statement statement = dbConnection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(String.format("select id, name from %s", TABLE_NAME))) {
                int i = 0;
                while (resultSet.next()) {
                    System.out.println(
                            String.format("#%d: %d, %s", i, resultSet.getInt("id"), resultSet.getString("name")));
                    ++i;
                }
            }
        }
    }

    private static final Path PROPS_FILE_PATH = Paths.get("jdbctest.properties");
    private static final Path CRED_FILE_PATH = Paths.get("credential.x");
    private static final Path SECRET_FILE_PATH = Paths.get("secret.x");

    private static DbConnectionProviderInterface getDatabaseConnectionProvider(AppProperties properties,
            Credential credential) throws Exception {
        Path databaseFilePath = Paths.get(properties.getDatabaseSource()).toAbsolutePath();
        switch (properties.getDatabaseProvider().toLowerCase()) {
            case "derby":
                return new DerbyConnectionProvider(databaseFilePath,
                        credential.getUserId(),
                        credential.getPassword());

            case "sqlite":
                return new SqliteConnectionProvider(databaseFilePath.toFile().toPath());

            default:
                throw new Exception(String.format("Der Datenbankanbieter '%s' ist nicht anerkannt!",
                        properties.getDatabaseProvider()));
        }
    }

    public static void main(String[] args) {
        try {
            CredentialFetcherInterface credentialFetcher = new CredentialFetcher(CRED_FILE_PATH, SECRET_FILE_PATH,
                    new InteractiveCredentialFetcher());

            Credential credential = credentialFetcher.getCredential();
            AppProperties properties = AppProperties.loadFrom(PROPS_FILE_PATH.toFile());

            try (DbConnectionProviderInterface dbConnectionProvider = getDatabaseConnectionProvider(properties,
                    credential)) {
                Connection dbConnection = dbConnectionProvider.getConnection();
                boolean schemaNotPresent = createSchemaIfNotPresent(dbConnection);
                logger.config(String.format("Schema der Datenbank schon vorhanden? %b", !schemaNotPresent));
                recordSomeData(dbConnection);
                listData(dbConnection);
            }
        } catch (Exception ex) {
            StackTraceElement[] stackTrace = ex.getStackTrace();
            logger.severe(String.format("%s\n@ %s", ex.toString(), stackTrace[stackTrace.length - 1].toString()));
        }
    }
}
