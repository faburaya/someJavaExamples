package com.examples.filesearch;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @brief Implementiert <code>FilesPackagerInterface</code>,
 *        indem das Paket eine Zip-Datei ist.
 * @see com.examples.filesearch.FilesPackagerInterface
 */
class FilesZipper implements FilesPackagerInterface, AutoCloseable {
    private Path basePath;
    private ZipOutputStream zipOutStream;

    /**
     * @brief Erstellt eine neue Instanz von <code>FilesZipper</code>.
     * @param basePath Der oberste Pfad, auf den alle im Paket hinzufügende Dateien sich beziehen.
     * @param filePath Der Pfad der zu erzeugenden Zip-Datei.
     * @throws FileNotFoundException
     */
    public FilesZipper(Path basePath, Path filePath) throws FileNotFoundException {
        this.basePath = basePath;
        zipOutStream = new ZipOutputStream(new FileOutputStream(filePath.toString()));
    }

    @Override
    public void close() throws IOException {
        zipOutStream.close();
    }

    @Override
    public void AddFileToPackage(Path filePath) throws IOException {
        Path relativePath = filePath.relativize(basePath);
        ZipEntry zipEntry = new ZipEntry(relativePath.toString());
        zipEntry.setTime(filePath.toFile().lastModified());
        zipOutStream.putNextEntry(zipEntry);
        Files.copy(filePath, zipOutStream);
        zipOutStream.closeEntry();
    }
}
