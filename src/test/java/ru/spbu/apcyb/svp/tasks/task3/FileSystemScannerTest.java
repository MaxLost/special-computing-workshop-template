package ru.spbu.apcyb.svp.tasks.task3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class FileSystemScannerTest {

  private boolean directoryComparator(Directory dir1, Directory dir2) {

      if (dir1.files.equals(dir2.files) && dir1.subdirectories.size() == dir2.subdirectories.size()) {

        for (int i = 0; i < dir1.subdirectories.size(); i++) {

          if (!directoryComparator(dir1.subdirectories.get(i),dir2.subdirectories.get(i))) {
            return false;
          }
        }

        return true;
      }

      return false;
  }

  @Test
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
  }

  @Test
  void invalidPathTest() {
    String directoryPath = "it's just a string";

    Exception e = assertThrows(RuntimeException.class, () -> new FileSystemScanner(directoryPath));
    assertEquals("Passed string is not a valid path to directory.", e.getMessage());
  }

  @Test
  void scanToFileTest() throws IOException {
    String directoryPath = "src/test/resources/directoryScanTest/";
    String pathToFile = "src/test/resources/expected.txt";

    Path path = Path.of(directoryPath);

    try (FileWriter output = new FileWriter(pathToFile)) {
      output.write(path.toAbsolutePath() + "\n");
      output.write("\tdirectory1\n");
      output.write("\t\t1.cfg\n");
      output.write("\t\t1.txt\n");
      output.write("\temptyDirectoryTest\n");
      output.write("\t2.ini\n");
    }

    new FileSystemScanner(directoryPath).scanToFile("src/test/resources/");

    Path expectedPathToResult = Path.of(
        path.toAbsolutePath() + "_file_structure.txt");
    if (Files.notExists(expectedPathToResult)) {
      fail("Cannot find result file at expected path.");
    }

    assertEquals(-1, Files.mismatch(Path.of(pathToFile), expectedPathToResult));
  }
}
