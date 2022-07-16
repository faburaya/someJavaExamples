package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class TestFile extends FileSystemTestNode {
    public TestFile(String name) {
        super(name, null);
    }

    public List<FileSystemTestNode> getChildNodes() {
        return Collections.emptyList();
    }

    public FileSystemTestNode create() throws IOException {
        Files.createFile(getPath());
        return this;
    }

    public void delete() throws IOException {
        Files.deleteIfExists(getPath());
    }
}
