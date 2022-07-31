package com.examples.jdbctest;

import java.io.Closeable;
import java.util.Arrays;

class Credential implements Closeable {
    private final String userId;

    public String getUserId() {
        return userId;
    }

    private final char[] password;

    public char[] getPassword() {
        return password;
    }

    public Credential(String userId, char[] password) {
        this.userId = userId;
        this.password = password;
    }

    @Override
    public void close() {
        Arrays.fill(password, (char) 0);
    }
}
