package com.examples.jdbctest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class SqliteConnectionProviderTest {
    private static final String SQLITE_DATABASE_FILE_NAME = "test_sqlite_db.dat";
    private static final String SQLITE_DB_SINGLE_TABLE_NAME = "test_table";
    private static final String SQLITE_DB_TABLE_COLUMN_NAME = "test_column";

    private File getDatabaseFile() {
        return new File(this.getClass().getResource(SQLITE_DATABASE_FILE_NAME).getFile());
    }

    @Test
    public void getConnection_and_use_it() throws SQLException {
        String dbFilePath = getDatabaseFile().getAbsolutePath();
        DbConnectionProviderInterface provider = new SqliteConnectionProvider(dbFilePath);
        try (Connection conn = provider.getConnection()) {
            assertNotNull(conn);
            String query = String.format("select * from %s;", SQLITE_DB_SINGLE_TABLE_NAME);
            Statement statement = conn.createStatement();
            ResultSet rowSet = statement.executeQuery(query);
            String actualRowValue = null;
            int actualRowCount = 0;
            while (rowSet.next()) {
                actualRowValue = rowSet.getString(SQLITE_DB_TABLE_COLUMN_NAME);
                ++actualRowCount;
            }
            assertEquals(1, actualRowCount);
            assertEquals("test_row", actualRowValue);
        }
    }
}
