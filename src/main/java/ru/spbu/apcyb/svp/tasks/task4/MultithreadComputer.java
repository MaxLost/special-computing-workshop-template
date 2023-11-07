package ru.spbu.apcyb.svp.tasks.task4;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultithreadComputer {

  public static void main(String[] args) {

    if (args.length != 3) {
      throw new IllegalArgumentException(
          "Incorrect amount of arguments, check specification of program.");
    }

    try {
      Path inputPath = Path.of(args[0]);
      Path singleOutputPath = Path.of(args[1]);
      Path multiOutputPath = Path.of(args[2]);

      computeTangents(inputPath, singleOutputPath, multiOutputPath);

    } catch (InvalidPathException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private static void computeTangents(Path inputPath, Path singleOutputPath, Path multiOutputPath) {

    try (Scanner input = new Scanner(inputPath)) {

      List<Double> numbers = new ArrayList<>();

      while (input.hasNextLine()) {
        numbers.add(Double.valueOf(input.nextLine()));
      }

      Instant startSingle = Instant.now();
      List<Double> resultSingle = computeTanSingleThread(numbers);
      Instant endSingle = Instant.now();
      writeResultInFile(singleOutputPath, resultSingle);

      System.out.printf("Time spent on calculation with single thread: %d ms%n",
          Duration.between(startSingle, endSingle).toMillis());

      Instant startMulti = Instant.now();
      List<Double> resultMulti = computeTanMultiThread(numbers, 5);
      Instant endMulti = Instant.now();
      writeResultInFile(multiOutputPath, resultMulti);

      System.out.printf("Time spent on calculation with multiple threads: %d ms%n",
          Duration.between(startMulti, endMulti).toMillis());

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

  public static List<Double> computeTanSingleThread(List<Double> numbers) {

    List<Double> result = new ArrayList<>();

    for (Double x : numbers) {
      result.add(Math.tan(x));
    }

    return result;
  }

  public static List<Double> computeTanMultiThread(List<Double> numbers, int threadCount) {

    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    try {
      List<Future<Double>> futures = new ArrayList<>();
      for (Double x : numbers) {
        futures.add(executorService.submit(new TanComputer(x)));
      }

      List<Double> result = new ArrayList<>();
      for (Future<Double> f : futures) {
        result.add(f.get());
      }

      return result;
      /*Stream<Future<Double>> futures = executorService.invokeAll(
          numbers.stream().map(TanComputer::new).toList()).stream();

      return futures.map(doubleFuture -> {
        try {
          return doubleFuture.get();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }).toList();*/

    } catch (Exception e) {
      throw new RuntimeException(e);

    } finally {
      executorService.shutdownNow();
    }
  }

  private static class TanComputer implements Callable<Double> {

    Double value;

    public TanComputer(Double value) {
      this.value = value;
    }

    @Override
    public Double call() throws Exception {
      return Math.tan(this.value);
    }
  }

}
