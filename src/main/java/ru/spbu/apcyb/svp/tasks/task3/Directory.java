package ru.spbu.apcyb.svp.tasks.task3;

import java.io.FileWriter;
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
   * system into string.
   */
  public String updateStructure(boolean isRoot, int identation) {

    try (Stream<Path> stream = Files.list(directoryPath)) {

      StringBuilder str = new StringBuilder();
      String baseIdentation = "\t".repeat(identation + 1);

      if (isRoot) {
        str.append(directoryPath).append("\n");
      }

      List<Path> content = stream.toList();
      List<Path> files = new ArrayList<>();

      for (Path file : content) {
        if (Files.isDirectory(file)) {
          str.append(baseIdentation).append(file.getFileName()).append("\n");
          str.append(new Directory(file).updateStructure(false, identation + 1));
        } else {
          files.add(file);
        }
      }

      for (Path file : files) {
        str.append(baseIdentation).append(file.getFileName()).append("\n");
      }

      return str.toString();

    } catch (IOException e) {
      throw new RuntimeException("Directory scanning failed at:" + directoryPath.toString());
    }
  }
}
