package ru.spbu.apcyb.svp.tasks.task3;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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
      if (Files.isDirectory(startPath)) {
        root = new Directory(startPath);
      } else {
        throw new InvalidPathException(path, "Passed string is not a valid path to directory.");
      }
    } catch (InvalidPathException e) {
      throw new RuntimeException("Passed string is not a valid path to directory.");
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

    String[] pathAsArray = root.directoryPath.toString()
        .split("\\\\");
    String fileName = pathAsArray[pathAsArray.length - 1] + "_file_structure.txt";

    try (FileWriter output = new FileWriter(path + "/" + fileName)) {

      root.updateStructure();
      output.write(root.directoryPath + "\n");
      for (Directory subdir : root.subdirectories) {
        writeSubdirectoryToFile(subdir, output, 1);
      }
      for (Path file : root.files) {
        output.write("\t" + file.getFileName().toString() + "\n");
      }

    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private void writeSubdirectoryToFile(Directory root, FileWriter output, int identation)
      throws IOException {

    String base = "\t".repeat(identation);

    String[] pathAsStringArray = root.directoryPath.toString().split("\\\\");
    output.write(base + pathAsStringArray[pathAsStringArray.length - 1] + "\n");
    for (Directory subdir : root.subdirectories) {
      writeSubdirectoryToFile(subdir, output, identation + 1);
    }
    for (Path file : root.files) {
      output.write(base + "\t" + file.getFileName().toString() + "\n");
    }
  }

}
