package com.examples.jdbctest;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DerbyConnectionProviderTest {
    private final String DERBY_DATABASE_FILE_PATH = "test_derby_database.dat";
    private final String DERBY_DATABASE_USER = "Kerl";
    private final String DERBY_DATABASE_PASSOWRD = "K3nnwort";

    @Test
    public void getConnection() {
        DbConnectionProviderInterface provider = new DerbyConnectionProvider(DERBY_DATABASE_FILE_PATH,
                DERBY_DATABASE_USER, DERBY_DATABASE_PASSOWRD);
        assertNotNull(provider);
    }
}
