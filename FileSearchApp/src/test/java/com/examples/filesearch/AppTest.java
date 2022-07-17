package com.examples.filesearch;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AppTest {
    @Test
    void searchAndPackageMatchingFiles() throws IOException {
        List<Path> filePaths = List.of(Paths.get("0"), Paths.get("1"), Paths.get("2"));
        FileExplorerInterface fileExplorerMock = Mockito.mock(FileExplorerInterface.class);
        Mockito.when(fileExplorerMock.listFiles(any(Path.class)))
                .thenReturn(filePaths);

        FileContentCheckerInterface fileContentCheckerMock = Mockito.mock(FileContentCheckerInterface.class);
        Mockito.when(fileContentCheckerMock.hasMatch(filePaths.get(0))).thenReturn(true);
        Mockito.when(fileContentCheckerMock.hasMatch(filePaths.get(1))).thenReturn(true);
        Mockito.when(fileContentCheckerMock.hasMatch(filePaths.get(2))).thenReturn(false);

        FilePackagerInterface filePackagerMock = Mockito.mock(FilePackagerInterface.class);
        App application = new App(fileExplorerMock, fileContentCheckerMock, filePackagerMock);
        assertEquals(2, application.SearchAndPackageMatchingFiles(Paths.get("someDirectory")));

        verify(filePackagerMock, times(1)).addFileToPackage(filePaths.get(0));
        verify(filePackagerMock, times(1)).addFileToPackage(filePaths.get(1));
        verify(filePackagerMock, never()).addFileToPackage(filePaths.get(2));
    }
}