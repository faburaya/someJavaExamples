package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

abstract class FileSystemTestNode {
    private final String name;

    public String getName() {
        return name;
    }

    private FileSystemTestNode parent;

    protected void setParent(FileSystemTestNode parent) {
        this.parent = parent;
    }

    protected FileSystemTestNode(String name, FileSystemTestNode parent) {
        assert name != null;
        assert !name.isEmpty();
        this.name = name;
        this.parent = parent;
    }

    protected abstract List<FileSystemTestNode> getChildNodes();

    public Path getPath() {
        if (parent != null) {
            return parent.getPath().resolve(name);
        }
        return Paths.get(name);
    }

    public abstract FileSystemTestNode create() throws IOException;

    public abstract void delete() throws IOException;
}
