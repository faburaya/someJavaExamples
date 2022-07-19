package com.examples.filesearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class App {
    private final FileExplorerInterface fileExplorer;
    private final FileContentCheckerInterface contentChecker;
    private final FilePackagerInterface packager;

    /**
     * Erstellt eine neue Instanz dieser Anwendung.
     * @param fileExplorer Die injizierte Implementierung zum Entdecken von Dateien in einem bestimmten Ordner.
     * @param contentChecker Die injizierte Implementierung zur Überprüfung des Inhalts einer gegebenen Datei.
     * @param packager Die injizierte Implementierung zur Erstellung von Paketen, die Dateien enthalten.
     */
    public App(FileExplorerInterface fileExplorer,
               FileContentCheckerInterface contentChecker,
               FilePackagerInterface packager) {
        this.fileExplorer = fileExplorer;
        this.contentChecker = contentChecker;
        this.packager = packager;
    }

    /**
     * Sucht nach Dateien, deren Inhalt wenigstens eine Übereinstimmung mit
     * einem bestimmten Muster aufweist, und verpackt sie in einer Zip-Datei.
     *
     * @param inputDirPath Der Pfad des Ordners, wo die zu suchenden Dateien sich befinden.
     * @return Die Anzahl von Dateien die im Paket hinzugefügt wurden.
     *         Null bedeutet, dass der Inhalt von keiner Datei mit dem Muster übereinstimmte
     *         und kein Paket wird daher erzeugt.
     */
    public int SearchAndPackageMatchingFiles(Path inputDirPath) throws IOException {
        int fileCount = 0;
        List<Path> files = fileExplorer.listFiles(inputDirPath);
        for (Path filePath : files) {
            if (contentChecker.hasMatch(filePath)) {
                packager.addFileToPackage(filePath);
                ++fileCount;
            }
        }
        return fileCount;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Falsche Argumente!");
            System.out.println("Nutzung: regex [Eingabe] [Ausgabe]");
            System.out.println("Eingabe = Pfad des Ordners mit den zu suchenden Dateien.");
            System.out.println("Ausgabe = Pfad der Zip-Datei, die alle Dateien enthalten wird, deren Inhalt wenigstens eine Übereinstimmung mit dem regulären Ausdruck aufweist.");
            return;
        }

        final String regularExpression = args[0];

        final Path inputDirPath = Paths.get(args[1]);
        if (!Files.isDirectory(inputDirPath)) {
            System.out.println("Fehler: '" + inputDirPath + "' ist kein Ordner!");
            return;
        }

        final Path outputZipPath = Paths.get(args[2]);
        if (Files.exists(outputZipPath)) {
            try {
                Files.delete(outputZipPath);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try (FileZipper zipper = new FileZipper(inputDirPath, outputZipPath)) {
            App application = new App(new FileExplorer(), new FileContentChecker(regularExpression), zipper);
            int matchingFilesCount = application.SearchAndPackageMatchingFiles(inputDirPath);
            System.out.println("Der Inhalt von " + matchingFilesCount + " Datei(en) wies(es) Überstimmung mit dem Muster auf.");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}