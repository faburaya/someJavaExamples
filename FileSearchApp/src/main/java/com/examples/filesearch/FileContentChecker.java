package com.examples.filesearch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Implementiert <code>FileContentCheckerInterface</code>, indem sie
 * den Inhalt einer Textdatei mithilfe eines regulären Ausdrucks sucht.
 * @see com.examples.filesearch.FileContentCheckerInterface
 */
class FileContentChecker implements FileContentCheckerInterface {
    private final Pattern pattern;

    /**
     * Erstellt eine neue Instanz von <code>FileContentChecker</code>.
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
    public boolean hasMatch(Path filePath) throws IOException {
        String contentType = Files.probeContentType(filePath);
        if (contentType != null && contentType.startsWith("text")) {
            try (Stream<String> stream = Files.lines(filePath, StandardCharsets.UTF_8)) {
                return stream.anyMatch(line -> pattern.matcher(line).find());
            }
        }
        return false;
    }
}
