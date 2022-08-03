package com.examples.jdbctest;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class DerbyConnectionProviderTest {
    private static final Path DERBY_DATABASE_FILE_PATH = Paths.get("temp", "derby_database");
    private static final String DERBY_DATABASE_USER = "Kerl";
    private static final char[] DERBY_DATABASE_PASSOWRD = "K3nnwort".toCharArray();

    private static void deletePath(Path path) throws IOException {
        if (!Files.exists(path))
            return;

        Files.walk(path, 1).forEach(subPath -> {
            try {
                if (subPath == path) {
                    return;
                }

                if (Files.isDirectory(subPath)) {
                    deletePath(subPath);
                } else {
                    Files.delete(subPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Files.delete(path);
    }

    @Before
    public void setUp() throws IOException {
        deletePath(DERBY_DATABASE_FILE_PATH);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        deletePath(DERBY_DATABASE_FILE_PATH);
    }

    @Test
    public void getConnection() throws SQLException {
        try (DerbyConnectionProvider provider = new DerbyConnectionProvider(DERBY_DATABASE_FILE_PATH,
                DERBY_DATABASE_USER, DERBY_DATABASE_PASSOWRD)) {
            try (Connection conn = provider.getConnection()) {
                assertNotNull(conn);
            }
        }
    }
}
