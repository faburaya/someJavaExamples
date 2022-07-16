package com.examples.filesearch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * @brief Sucht den Inhalt einer Textdatei mithilfe eines regulären Ausdrucks.
 */
class FileContentChecker implements FileContentCheckerInterface {
    private final Pattern pattern;

    /**
     * @brief Erstellt eine neue Instanz von <code>FileContentChecker</code>.
     * @param regularExpression Der reguläre Ausdruck, den diese Instanz verwenden wird,
     *                          um den Inhalt der Dateien zu überprüfen.
     */
    public FileContentChecker(String regularExpression) {
        if (regularExpression == null || regularExpression.isEmpty()) {
            throw new IllegalArgumentException("Regulärer Ausdruck nicht gegeben!");
        }
        pattern = Pattern.compile(regularExpression);
    }

    @Override
    public boolean HasMatch(Path filePath) throws IOException {
        if (Files.probeContentType(filePath).startsWith("text")) {
            return Files.lines(filePath, StandardCharsets.UTF_8)
                    .anyMatch(line -> pattern.matcher(line).find());
        }
        return false;
    }
}
