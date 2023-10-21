package ru.spbu.apcyb.svp.tasks.task3;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemScanner {

  private final Directory root;

  public FileSystemScanner(String path) {

    try {
      Path startPath = Paths.get(path);
      root = new Directory(startPath);

    } catch (InvalidPathException e) {
      throw new RuntimeException("Passed string is not a valid path.");
    }
  }

  public Directory scan() {

    root.scan();
    return root;
  }

  public void scanToFile(String path) {

    try (FileWriter output = new FileWriter(path)) {

      root.scan();
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
