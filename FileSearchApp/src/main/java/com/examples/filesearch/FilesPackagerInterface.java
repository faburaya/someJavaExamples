package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @brief Schnittstelle zur Erzeugung von Paketen.
 */
public interface FilesPackagerInterface {
    /**
     * @brief Fügt dem zu erzeugende Paket eine Datei hinzu.
     * @param filePath Der Pfad der hinzufügenden Datei.
     * @throws IOException
     */
    void AddFileToPackage(Path filePath) throws IOException;
}
