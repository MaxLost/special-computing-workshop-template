package ru.spbu.apcyb.svp.tasks.task3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class FileSystemScannerTest {

  /*@Test
  void noSubdirectoriesScanTest() {
    String directoryPath = "src/test/resources/noSubdirectoriesTest/";
    Path path = Path.of(directoryPath).toAbsolutePath();
    Directory expected = new Directory(path);
    expected.files.add(Paths.get(path + "/1.txt"));
    expected.files.add(Paths.get(path + "/2.txt"));

    Directory result = new FileSystemScanner(directoryPath).scan();
    assertTrue(directoryComparator(expected, result));
  }

  @Test
  void emptyDirectoryScanTest() {
    String directoryPath = "src/test/resources/directoryScanTest/emptyDirectoryTest/";
    Directory expected = new Directory(Path.of(directoryPath));

    Directory result = new FileSystemScanner(directoryPath).scan();
    assertTrue(directoryComparator(expected, result));
  }

  @Test
  void directoryScanTest() {
    String directoryPath = "src/test/resources/directoryScanTest/";
    Directory expected = new Directory(Path.of(directoryPath));
    expected.files.add(Path.of(expected.directoryPath + "/2.ini"));
    expected.subdirectories.add(new Directory(Paths.get(directoryPath + "directory1/")));
    expected.subdirectories.add(new Directory(Paths.get(directoryPath + "emptyDirectoryTest/")));
    Directory dir1 = expected.subdirectories.get(0);
    dir1.files.add(Paths.get(dir1.directoryPath + "/1.cfg"));
    dir1.files.add(Paths.get(dir1.directoryPath + "/1.txt"));

    Directory result = new FileSystemScanner(directoryPath).scan();
    assertTrue(directoryComparator(expected, result));
  }*/

  @Test
  void noSubdirectoriesScanTest() throws IOException {
    String directoryPath = "src/test/resources/task3/noSubdirectoriesTest/";
    String pathToFile = "src/test/resources/task3/noSubdirectoriesTest_expected.txt";

    Path path = Path.of(directoryPath);

    try (FileWriter output = new FileWriter(pathToFile)) {
      output.write(path.toAbsolutePath() + "\n");
      output.write("\t1.txt\n");
      output.write("\t2.txt\n");
    }

    new FileSystemScanner(directoryPath).scanToFile("src/test/resources/task3/");

    Path expectedPathToResult = Path.of(
        path.toAbsolutePath() + "_file_structure.txt");
    assertEquals(-1, Files.mismatch(Path.of(pathToFile), expectedPathToResult));
  }

  @Test
  void invalidPathTest() {
    String directoryPath = "it's just a string";

    Exception e = assertThrows(RuntimeException.class, () -> new FileSystemScanner(directoryPath));
    assertEquals("Passed string is not a valid path to directory.: it's just a string",
        e.getMessage());
  }

  @Test
  void directoryScanTest() throws IOException {
    String directoryPath = "src/test/resources/task3/directoryScanTest/";
    String pathToFile = "src/test/resources/task3/directoryScanTest_expected.txt";

    Path path = Path.of(directoryPath);

    try (FileWriter output = new FileWriter(pathToFile)) {
      output.write(path.toAbsolutePath() + "\n");
      output.write("\tdirectory1\n");
      output.write("\t\t1.cfg\n");
      output.write("\t\t1.txt\n");
      output.write("\temptyDirectoryTest\n");
      output.write("\t2.ini\n");
    }

    new FileSystemScanner(directoryPath).scanToFile("src/test/resources/task3/");

    Path expectedPathToResult = Path.of(
        path.toAbsolutePath() + "_file_structure.txt");
    if (Files.notExists(expectedPathToResult)) {
      fail("Cannot find result file at expected path.");
    }

    assertEquals(-1, Files.mismatch(Path.of(pathToFile), expectedPathToResult));
  }

  @Test
  void emptyDirectoryScanTest() throws IOException {
    String directoryPath = "src/test/resources/task3/directoryScanTest/emptyDirectoryTest/";
    String pathToFile = "src/test/resources/task3/emptyDirectoryTest_expected.txt";

    Path path = Path.of(directoryPath);

    try (FileWriter output = new FileWriter(pathToFile)) {
      output.write(path.toAbsolutePath() + "\n");
    }

    new FileSystemScanner(directoryPath).scanToFile("src/test/resources/task3/");

    Path expectedPathToResult = Path.of(
        Path.of("src/test/resources/task3/emptyDirectoryTest").toAbsolutePath() + "_file_structure.txt");
    if (Files.notExists(expectedPathToResult)) {
      fail("Cannot find result file at expected path.");
    }

    assertEquals(-1, Files.mismatch(Path.of(pathToFile), expectedPathToResult));
  }

}
