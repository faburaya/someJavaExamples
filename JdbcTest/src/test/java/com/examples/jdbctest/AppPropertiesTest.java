package com.examples.jdbctest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class AppPropertiesTest {
    private File getPropertiesFile() {
        return new File(this.getClass().getResource("jdbctest.properties").getFile());
    }

    @Test
    public void testLoadFrom() throws FileNotFoundException, IOException {
        AppProperties appProperties = AppProperties.loadFrom(getPropertiesFile());
        assertEquals("database_product", appProperties.getDatabaseProvider());
        assertEquals("the_source", appProperties.getDatabaseSource());
    }
}
