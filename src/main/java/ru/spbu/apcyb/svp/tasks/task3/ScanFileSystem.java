package ru.spbu.apcyb.svp.tasks.task3;

/**
 * Class for getting a snapshot of filesystem structure and saving it in file.
 */
public class ScanFileSystem {

  /**
   * Takes a snapshot of file system structure and saves it in file at specified path.
   *
   * @param args expected 2 strings:
   *             1) Path to directory expected to be scanned
   *             2) Path to file where result should be saved
   */
  public static void main(String[] args) {

    if (args.length != 2) {
      System.out.println(
          "Incorrect amount of arguments. Should be two strings: path to directory that should be "
              + "scanned and path to file where a snapshot will be saved.");
    }

    FileSystemScanner scanner = new FileSystemScanner(args[0]);
    scanner.scanToFile(args[1]);
  }

}
