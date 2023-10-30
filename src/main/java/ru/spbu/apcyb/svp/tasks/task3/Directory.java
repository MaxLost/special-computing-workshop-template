package ru.spbu.apcyb.svp.tasks.task3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Class that represents a directory of a file system as a Java Object.
 */
public class Directory {

  Path directoryPath;
  List<Path> files = new ArrayList<>();
  List<Directory> subdirectories = new ArrayList<>();

  /**
   * Constructs directory on base of path as a string.
   *
   * @param path path to directory as a string
   */
  public Directory(String path) {

    try {
      Path startPath = Paths.get(path).toAbsolutePath();
      if (Files.isDirectory(startPath)) {
        this.directoryPath = startPath;
      } else {
        throw new InvalidPathException(path, "Passed string is not a valid path to directory.");
      }
    } catch (InvalidPathException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Constructs directory on base of Path object.
   *
   * @param path path to directory
   */
  public Directory(Path path) {

    if (!Files.isDirectory(path)) {
      throw new RuntimeException("Provided path is not leading to directory");
    }

    this.directoryPath = path.toAbsolutePath();
  }

  /**
   * This method scans directory and all of its subdirectories and records a snapshot of a file
   * system.
   */
  public void updateStructure() {

    try (Stream<Path> stream = Files.list(directoryPath)) {

      List<Path> content = stream.toList();
      for (Path file : content) {
        if (Files.isDirectory(file)) {
          subdirectories.add(new Directory(file));
        } else {
          files.add(file);
        }
      }

      for (Directory subdir : subdirectories) {
        subdir.updateStructure();
      }

    } catch (IOException e) {
      throw new RuntimeException("Directory scanning failed at:" + directoryPath.toString());
    }
  }
}
