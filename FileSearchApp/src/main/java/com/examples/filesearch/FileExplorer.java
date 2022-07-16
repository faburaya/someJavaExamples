package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @brief Implementiert das Auflisten von Dateien.
 */
public class FileExplorer implements FileExplorerInterface {
    @Override
    public List<Path> listFiles(Path directoryPath) throws IOException {
        return Files.list(directoryPath)
                .filter(path -> Files.isRegularFile(path))
                .toList();
    }
}
