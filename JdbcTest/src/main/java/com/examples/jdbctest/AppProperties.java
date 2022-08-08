package com.examples.jdbctest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Sammelt alle Einstellungen, die für den Betrieb
 * dieser Anwendung notwendig sind.
 */
class AppProperties {
    private static final String PROPKEY_DB_SOURCE = "dbSource";

    private static final String PROPKEY_DB_PROVIDER = "dbProvider";

    private final String databaseProvider;

    public String getDatabaseProvider() {
        return databaseProvider;
    }

    private final String databaseSource;

    public String getDatabaseSource() {
        return databaseSource;
    }

    private AppProperties(String databaseProvider, String databaseSource) {
        this.databaseProvider = databaseProvider;
        this.databaseSource = databaseSource;
    }

    /**
     * Ladet die Einstellungen aus einer *.properties Datei.
     * 
     * @param file Die zu ladende Datei.
     * @return Eine Instanz von <code>AppProperties</code>.
     * @throws FileNotFoundException Wenn die gegeben Datei nicht zu finden ist.
     * @throws IOException           Wenn nicht alle notwendigen Einstellungen
     *                               geladen werden können.
     */
    public static AppProperties loadFrom(File file) throws FileNotFoundException, IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            String databaseProvider = properties.getProperty(PROPKEY_DB_PROVIDER);
            String databaseSource = properties.getProperty(PROPKEY_DB_SOURCE);
            return new AppProperties(databaseProvider, databaseSource);
        }
    }
}