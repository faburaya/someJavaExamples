package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Files;

class TestFile extends FileSystemTestNode {
    public TestFile(String name) {
        super(name, null);
    }

    public void create() throws IOException {
        Files.createFile(getPath());
    }

    public void delete() throws IOException {
        Files.deleteIfExists(getPath());
    }
}
