package ru.spbu.apcyb.svp.tasks.task4;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class for multi-thread computing of tangents.
 */
public class MultiThreadComputer {

  private static final Logger logger = Logger.getLogger("Multi.thread.computer");

  /**
   * Method that reads values from one file and then computes and writes tangents of given values
   * to another file.
   *
   * @param args first argument - path to source of values, second - path to final results.
   */
  public static void main(String[] args) {

    if (args.length != 2) {
      throw new IllegalArgumentException(
          "Incorrect amount of arguments, check specification of program.");
    }

    try {
      Path inputPath = Path.of(args[0]);
      Path multiOutputPath = Path.of(args[1]);

      List<Double> values = readValuesFromFile(inputPath);
      List<Double> result = computeTanMultiThread(values, 8);
      writeResultInFile(multiOutputPath, result);

    } catch (InvalidPathException e) {
      throw new IllegalArgumentException(e.getMessage());

    } catch (Exception e) {
      String stackTrace = Arrays.stream(e.getStackTrace())
          .map(StackTraceElement::toString)
          .collect(Collectors.joining("\n"));
      logger.severe("One of the threads was interrupted at:\n" + stackTrace);
    }
  }

  /**
   * Method for logging performance of single and multi thread calculation of tangents.
   *
   * @param input file with input values
   * @param singleOutput file for single-thread output
   * @param multiOutput file for multi-thread output
   */
  public static void computeTangentsWithPerformanceLog(String input, String singleOutput,
      String multiOutput) {

    try {
      Path inputPath = Path.of(input);
      Path singleOutputPath = Path.of(singleOutput);
      Path multiOutputPath = Path.of(multiOutput);

      computeTangentsWithPerformanceLogs(inputPath, singleOutputPath, multiOutputPath);

    } catch (InvalidPathException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private static void computeTangentsWithPerformanceLogs(Path inputPath, Path singleOutputPath,
      Path multiOutputPath) {

    List<Double> numbers = readValuesFromFile(inputPath);

    Instant startSingle = Instant.now();
    List<Double> resultSingle = computeTanSingleThread(numbers);
    Instant endSingle = Instant.now();
    writeResultInFile(singleOutputPath, resultSingle);

    System.out.printf("Time spent on calculation with single thread: %d ms%n",
        Duration.between(startSingle, endSingle).toMillis());

    try {
      Instant startMulti = Instant.now();

      List<Double> resultMulti = computeTanMultiThread(numbers, 4);

      Instant endMulti = Instant.now();
      writeResultInFile(multiOutputPath, resultMulti);

      System.out.printf("Time spent on calculation with multiple threads: %d ms%n",
          Duration.between(startMulti, endMulti).toMillis());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static List<Double> readValuesFromFile(Path inputPath) {

    try (Scanner input = new Scanner(inputPath)) {
      List<Double> numbers = new ArrayList<>();

      while (input.hasNextLine()) {
        numbers.add(Double.valueOf(input.nextLine()));
      }

      return numbers;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void writeResultInFile(Path singleOutputPath, List<Double> result) {
    try (BufferedWriter output = Files.newBufferedWriter(singleOutputPath)) {
      output.write("Processed " + result.size() + " numbers.\n");

      for (Double x : result) {
        output.write(x + "\n");
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Method for computing tangents in a single thread.
   *
   * @param values values for which tangents will be calculated
   * @return list of computed tangents
   */
  public static List<Double> computeTanSingleThread(List<Double> values) {

    List<Double> result = new ArrayList<>();

    for (Double x : values) {
      result.add(Math.tan(x));
    }

    return result;
  }

  /**
   * Method for computing tangents by multiple parallel threads.
   *
   * @param values values for which tangents will be calculated
   * @param threadCount number of parallel threads
   * @return list of computed tangents
   */
  public static List<Double> computeTanMultiThread(List<Double> values, int threadCount)
      throws Exception {

    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    try {
      Future<List<Double>> futures = executorService.submit(
          () -> values.parallelStream().map(Math::tan).toList());

      return futures.get();

    } finally {
      executorService.shutdownNow();
    }
  }

}
