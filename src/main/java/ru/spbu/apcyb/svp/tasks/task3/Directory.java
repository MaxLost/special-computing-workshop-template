package ru.spbu.apcyb.svp.tasks.task3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

  public Directory(Path path) {

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

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof Directory dir && (this.directoryPath.equals(dir.directoryPath)
        && this.files.equals(dir.files)
        && this.subdirectories.size() == dir.subdirectories.size())) {

      for (int i = 0; i < this.subdirectories.size(); i++) {

        if (!this.subdirectories.get(i).equals(dir.subdirectories.get(i))) {
          return false;
        }
      }

      return true;
    }

    return false;
  }

}
