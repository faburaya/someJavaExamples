package com.examples.filesearch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class FileContentCheckerTest {
    private static final Path testFilePath = Paths.get("FileContentCheckerTestFile.txt");

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    private static Path createTextFileWith(CharSequence content) throws IOException {
        Files.writeString(testFilePath, content);
        return testFilePath;
    }

    @Test
    void hasMatch_singleLine() throws IOException {
        FileContentChecker checker = new FileContentChecker("\\w+\\d+");
        assertFalse(checker.HasMatch(createTextFileWith("Keine Übereinstimmung")));
        assertTrue(checker.HasMatch(createTextFileWith("Die Übereinstimmung1.")));
    }
    
    @Test
    void hasMatch_manyLines() throws IOException {
        FileContentChecker checker = new FileContentChecker("\\d+");
        StringBuilder textContent = new StringBuilder();
        textContent.append("Die Nummer\n");
        textContent.append("ist 696\n");
        textContent.append("oder?");
        assertTrue(checker.HasMatch(createTextFileWith(textContent)));
    }
}