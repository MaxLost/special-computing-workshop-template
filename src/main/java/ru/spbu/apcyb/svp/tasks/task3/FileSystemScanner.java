package ru.spbu.apcyb.svp.tasks.task3;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class for creating a snapshot of file system structure.
 */
public class FileSystemScanner {

  private final Directory root;

  /**
   * Creates scanner for a certain directory.
   *
   * @param path path of directory for which scanner will be created
   */
  public FileSystemScanner(String path) {

    try {
      Path startPath = Paths.get(path);
      root = new Directory(startPath);

    } catch (InvalidPathException e) {
      throw new RuntimeException("Passed string is not a valid path.");
    }
  }

  /**
   * Creates snapshot of a current file structure of certain directory.
   *
   * @return snapshot of a file structure
   */
  public Directory scan() {

    root.updateStructure();
    return root;
  }

  /**
   * Creates snapshot of a current file structure of certain directory and saves it as text in file.
   *
   * @param path specifies where to save a directory snapshot.
   */
  public void scanToFile(String path) {

    try (FileWriter output = new FileWriter(path)) {

      root.updateStructure();
      output.write(root.directoryPath.toString());
      for (Directory subdir : root.subdirectories) {
        writeSubdirectoryToFile(subdir, output, 1);
      }
      for (Path file : root.files) {
        output.write(" " + file.getFileName().toString());
      }

    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private void writeSubdirectoryToFile(Directory root, FileWriter output, int identation)
      throws IOException {

    String base = "\t".repeat(identation);

    output.write(root.directoryPath.toString());
    for (Directory subdir : root.subdirectories) {
      writeSubdirectoryToFile(subdir, output, identation + 1);
    }
    for (Path file : root.files) {
      output.write(base + file.getFileName().toString());
    }
  }

}
