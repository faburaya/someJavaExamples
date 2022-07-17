package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Implementiert <code>FileExplorerInterface</code>, indem sie Dateien auflistet.
 * @see com.examples.filesearch.FileExplorerInterface
 */
class FileExplorer implements FileExplorerInterface {
    @Override
    public List<Path> listFiles(Path directoryPath) throws IOException {
        try (Stream<Path> stream = Files.walk(directoryPath)) {
            return stream.filter(Files::isRegularFile).toList();
        }
    }
}
