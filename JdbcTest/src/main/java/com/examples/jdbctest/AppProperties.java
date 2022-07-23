package com.examples.jdbctest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Sammelt alle Einstellungen, die für den Betrieb
 * dieser Anwendung notwendig sind.
 */
class AppProperties {
    private static final String PROPKEY_DB_PASSWORD = "dbPassword";

    private static final String PROPKEY_DB_USER = "dbUser";

    private static final String PROPKEY_DB_URL = "dbUrl";

    private final String databaseUrl;

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    private final String databaseUser;

    public String getDatabaseUser() {
        return databaseUser;
    }

    private final String databasePassword;

    public String getDatabasePassword() {
        return databasePassword;
    }

    private AppProperties(String databaseUrl, String databaseUser, String databasePassword) {
        this.databaseUrl = databaseUrl;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
    }

    /**
     * Ladet die Einstellungen aus einer *.properties Datei.
     * 
     * @param filePath Der Pfad der zu ladenden Datei.
     * @return Eine Instanz von <code>AppProperties</code>.
     * @throws FileNotFoundException Wenn die gegeben Datei nicht zu finden ist.
     * @throws IOException           Wenn nicht alle notwendigen Einstellungen
     *                               geladen werden können.
     */
    public static AppProperties loadFrom(Path filePath) throws FileNotFoundException, IOException {
        try (FileInputStream inputStream = new FileInputStream(filePath.toFile())) {
            Properties properties = new Properties();
            properties.load(inputStream);
            String databaseUrl = properties.getProperty(PROPKEY_DB_URL);
            String databaseUser = properties.getProperty(PROPKEY_DB_USER);
            String databasePassword = properties.getProperty(PROPKEY_DB_PASSWORD);
            return new AppProperties(databaseUrl, databaseUser, databasePassword);
        }
    }
}