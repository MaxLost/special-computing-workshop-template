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
}
