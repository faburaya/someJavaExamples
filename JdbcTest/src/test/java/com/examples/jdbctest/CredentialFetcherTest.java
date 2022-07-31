package com.examples.jdbctest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class CredentialFetcherTest {
    private static final String TEST_CRED_FILE_PATH = "encrypted_credential_test";
    private static final String TEST_SECRET_FILE_PATH = "encrypted_secret_test";

    private static CredentialFetcherInterface createFetcherToProvide(String userId, char[] password) {
        return new CredentialFetcherInterface() {
            private int callCount = 0;

            public Credential getCredential() {
                assertTrue(callCount++ == 0);
                return new Credential(userId, password);
            }
        };
    }

    @Before
    public void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_CRED_FILE_PATH));
        Files.deleteIfExists(Paths.get(TEST_SECRET_FILE_PATH));
    }

    @Test
    public void getCredential() throws AppSecurityException {
        final String expectedUserId = "Benutzer";
        final char[] expectedPassword = "Kennwort".toCharArray();

        CredentialFetcher fetcher = new CredentialFetcher(TEST_CRED_FILE_PATH, TEST_SECRET_FILE_PATH,
                createFetcherToProvide(expectedUserId, expectedPassword));

        // holt die Anmeldeinformationen zuerst aus der Testinfrastruktur:
        Credential actualCredential = fetcher.getCredential();
        assertEquals(expectedUserId, actualCredential.getUserId());
        assertArrayEquals(expectedPassword, actualCredential.getPassword());

        assertTrue(String.format("File %s not present!", TEST_CRED_FILE_PATH),
                Paths.get(TEST_CRED_FILE_PATH).toFile().exists());

        assertTrue(String.format("File %s not present!", TEST_SECRET_FILE_PATH),
                Paths.get(TEST_SECRET_FILE_PATH).toFile().exists());

        // nun holt die Anmeldeinfomationen aus dem Speicher:
        actualCredential = fetcher.getCredential();
        assertEquals(expectedUserId, actualCredential.getUserId());
        assertArrayEquals(expectedPassword, actualCredential.getPassword());
    }
}
