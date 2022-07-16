package com.examples.filesearch;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileExplorerTest {
    private final String testDirName = "FileExplorerTestDirectory";

    @Test
    void listFiles_flat_hierarchy() throws IOException {
        FileSystemTestNode testDirectory =
                new TestDirectory(testDirName).with(
                    List.of(new TestFile("1"), new TestFile("2"))
                );
        testDirectory.create();

        try
        {
            List<String> expectedNames = List.of("1", "2");
            List<Path> expectedPaths = expectedNames.stream()
                    .map(name -> Paths.get(testDirName).resolve(name))
                    .sorted()
                    .toList();

            FileExplorer fileExplorer = new FileExplorer();
            List<Path> actualPaths =
                    fileExplorer.listFiles(Paths.get(testDirName))
                            .stream().sorted().toList();

            assertArrayEquals(expectedPaths.toArray(), actualPaths.toArray());
        }
        finally {
            testDirectory.delete();
        }
    }

    @Test
    void listFiles_two_levels() throws IOException {
        FileSystemTestNode testDirectory =
                new TestDirectory(testDirName).with(
                    List.of(new TestFile("1"),
                            new TestFile("2"),
                            new TestDirectory("sub").with(
                                    List.of(new TestFile("3"),
                                            new TestFile("4"))))
                );
        testDirectory.create();

        try
        {
            Path rootPath = Path.of(testDirName);
            List<Path> expectedPaths = List.of(
                    rootPath.resolve("1"),
                    rootPath.resolve("2"),
                    rootPath.resolve("sub").resolve("3"),
                    rootPath.resolve("sub").resolve("4"));

            FileExplorer fileExplorer = new FileExplorer();
            List<Path> actualPaths =
                    fileExplorer.listFiles(rootPath)
                            .stream().sorted().toList();

            assertArrayEquals(expectedPaths.toArray(), actualPaths.toArray());
        }
        finally {
            testDirectory.delete();
        }
    }
}