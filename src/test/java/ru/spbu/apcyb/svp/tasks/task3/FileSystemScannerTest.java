package ru.spbu.apcyb.svp.tasks.task3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class FileSystemScannerTest {

  @Test
  void noSubdirectoriesScanTest() {
    String directoryPath = "src/test/resources/noSubdirectoriesTest/";
    Directory expected = new Directory(Path.of(directoryPath));
    expected.files.add(Paths.get(directoryPath + "1.txt"));
    expected.files.add(Paths.get(directoryPath + "2.txt"));

    Directory result = new FileSystemScanner(directoryPath).scan();
    assertEquals(expected, result);
  }

  @Test
  void emptyDirectoryScanTest() {
    String directoryPath = "src/test/resources/directoryScanTest/emptyDirectoryTest/";
    Directory expected = new Directory(Path.of(directoryPath));

    Directory result = new FileSystemScanner(directoryPath).scan();
    assertEquals(expected, result);
  }

  @Test
  void directoryScanTest() {
    String directoryPath = "src/test/resources/directoryScanTest/";
    Directory expected = new Directory(Path.of(directoryPath));
    expected.subdirectories.add(new Directory(Paths.get(directoryPath + "directory1/")));
    expected.subdirectories.add(new Directory(Paths.get(directoryPath + "emptyDirectoryTest/")));
    Directory dir1 = expected.subdirectories.get(0);
    dir1.files.add(Paths.get(dir1.directoryPath + "/1.cfg"));
    dir1.files.add(Paths.get(dir1.directoryPath + "/1.txt"));

    Directory result = new FileSystemScanner(directoryPath).scan();
    assertEquals(expected, result);
  }
}
