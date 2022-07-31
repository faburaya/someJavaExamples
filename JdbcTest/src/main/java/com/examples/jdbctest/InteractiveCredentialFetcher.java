package com.examples.jdbctest;

/**
 * Fragt den Benutzer nach den Anmeldeinformationen.
 */
public class InteractiveCredentialFetcher implements CredentialFetcherInterface {
    @Override
    public Credential getCredential() {
        System.out.print("user: ");
        String userId = System.console().readLine();
        System.out.print("password: ");
        char[] password = System.console().readPassword();
        return new Credential(userId, password);
    }
}
