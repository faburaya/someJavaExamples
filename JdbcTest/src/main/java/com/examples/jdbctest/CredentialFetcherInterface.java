package com.examples.jdbctest;

/**
 * Schnittstelle zur Beschaffung der Anmeldeinformationen.
 */
public interface CredentialFetcherInterface {

    /**
     * Holt die Anmeldeinformationen.
     * 
     * @return Die Anmeldeinformationen.
     * @throws AppSecurityException Wenn ingendwelches Problem zustande kommt.
     */
    Credential getCredential() throws AppSecurityException;
}
