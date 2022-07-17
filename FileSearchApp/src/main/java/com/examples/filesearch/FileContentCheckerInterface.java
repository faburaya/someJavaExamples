package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Schnittstelle zur Überprüfung des Inhalts von Dateien.
 */
interface FileContentCheckerInterface {
    /**
     * @param filePath Der zu suchende Dateipfad.
     * @return Ob der Inhalt der Datei wenigstens eine Übereinstimmung mit einem angegebenen Muster aufweist.
     * @throws IOException Falls ein Fehler während des Zugriffs auf das Dateisystem auftritt.
     */
    boolean HasMatch(Path filePath) throws IOException;
}
