package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @brief Schnittstelle zum Auflisten von Dateien.
 */
public interface FileExplorerInterface {
    /**
     * @brief Listet die Dateien auf.
     * @param directoryPath Der Pfad des Ordners, deren Dateien aufgelistet werden müssen.
     * @return Eine Liste mit allen Dateien, die unmittelbar unter dem gegebenen Ordner stehen.
     * @throws IOException
     */
    List<Path> ListFiles(Path directoryPath) throws IOException;
}
