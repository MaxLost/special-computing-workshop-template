package ru.spbu.apcyb.svp.tasks.task3;

import java.io.FileWriter;
import java.io.IOException;

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

    this.root = new Directory(path);
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

      String fileStructure = root.updateStructure(true, 0);
      output.write(fileStructure);

    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

}
