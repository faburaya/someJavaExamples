package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Schnittstelle zur Erzeugung von Paketen.
 */
public interface FilePackagerInterface {
    /**
     * Fügt dem zu erzeugende Paket eine Datei hinzu.
     * @param filePath Der Pfad der hinzufügenden Datei.
     * @throws IOException Falls ein Fehler während des Zugriffs auf das Dateisystem auftritt.
     */
    void addFileToPackage(Path filePath) throws IOException;
}
