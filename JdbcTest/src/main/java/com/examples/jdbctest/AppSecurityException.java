package com.examples.jdbctest;

/**
 * Triff auf, wenn ein Fehler zustande kommt,
 * der die Sicherheit der Anwendung betrifft.
 */
public class AppSecurityException extends Exception {
    public AppSecurityException(String message) {
        super(message);
    }

    public AppSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
