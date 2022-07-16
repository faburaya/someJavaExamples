package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @brief Schnittstelle zum Auflisten von Dateien.
 */
interface FileExplorerInterface {
    /**
     * @param directoryPath Der Pfad des Ordners, deren Dateien aufgelistet werden müssen.
     * @return Eine Liste mit allen Dateien, die unmittelbar unter dem gegebenen Ordner stehen.
     * @throws IOException
     * @brief Listet die Dateien auf.
     */
    List<Path> listFiles(Path directoryPath) throws IOException;
}
