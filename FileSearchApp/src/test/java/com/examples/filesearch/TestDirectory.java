package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

class TestDirectory extends FileSystemTestNode {
    private final List<FileSystemTestNode> childNodes;

    public TestDirectory(String name) {
        super(name, null);
        childNodes = new ArrayList<>();
    }

    public FileSystemTestNode with(List<FileSystemTestNode> children) {
        assert children != null;
        for (FileSystemTestNode node : children) {
            node.setParent(this);
            childNodes.add(node);
        }
        return this;
    }

    public void create() throws IOException {
        Files.createDirectory(getPath());
        for (FileSystemTestNode node : childNodes) {
            node.create();
        }
    }

    public void delete() throws IOException {
        for (FileSystemTestNode node : childNodes) {
            node.delete();
        }
        Files.deleteIfExists(getPath());
    }
}
