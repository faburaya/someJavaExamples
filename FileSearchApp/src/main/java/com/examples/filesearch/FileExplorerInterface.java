package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Schnittstelle zum Auflisten von Dateien.
 */
interface FileExplorerInterface {
    /**
     * Listet die Dateien auf.
     * @param directoryPath Der Pfad des Ordners, deren Dateien aufgelistet werden müssen.
     * @return Eine Liste mit allen Dateien, die unter dem gegebenen Ordner und untergeordneten Ordnern stehen.
     * @throws IOException Falls ein Fehler während des Zugriffs auf das Dateisystem auftritt.
     */
    List<Path> listFiles(Path directoryPath) throws IOException;
}
