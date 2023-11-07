package ru.spbu.apcyb.svp.tasks.task4;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MultithreadComputer {

  public static void main(String[] args) {

    if (args.length != 2) {
      throw new IllegalArgumentException(
          "Incorrect amount of arguments, check specification of program.");
    }

    try {
      Path inputPath = Path.of(args[0]);
      Path outputPath = Path.of(args[1]);

      computeTangents(inputPath, outputPath);

    } catch (InvalidPathException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private static void computeTangents(Path inputPath, Path outputPath) {

    try (Scanner input = new Scanner(inputPath); BufferedWriter output = Files.newBufferedWriter(
        outputPath)) {

      List<Double> numbers = new ArrayList<>();

      while (input.hasNextLine()) {
        numbers.add(Double.valueOf(input.nextLine()));
      }

      long startSingle = System.currentTimeMillis();
      List<Double> singleResult = computeTanSingleThread(numbers);
      long endSingle = System.currentTimeMillis();
      System.out.printf("Time spent on calculation with single thread: %d%n",
          endSingle - startSingle);

      long startMulti = System.currentTimeMillis();
      List<Double> multiResult = computeTanMultiThread(numbers, 8);
      long endMulti = System.currentTimeMillis();
      System.out.printf("Time spent on calculation with multiple threads: %d%n",
          endMulti - startMulti);

      output.write("Processed " + singleResult.size() + " numbers.\n");
      for (Double x : singleResult) {
        output.write(x + "\n");
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<Double> computeTanSingleThread(List<Double> numbers) {

    return numbers.stream().map(Math::tan).toList();
  }

  public static List<Double> computeTanMultiThread(List<Double> numbers, int threadCount) {

    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    List<TanComputer> tasks = new ArrayList<>();
    for (Double value : numbers) {
      tasks.add(new TanComputer(value));
    }

    try {
      List<Future<Double>> futures = executorService.invokeAll(tasks);

      List<Double> results = new ArrayList<>();
      for (Future<Double> x : futures) {
        results.add(x.get(1, TimeUnit.MILLISECONDS));
      }

      return results;

    } catch (InterruptedException e) {
      throw new RuntimeException("One of the threads was interrupted.", e);

    } catch (ExecutionException e) {
      throw new RuntimeException("Exception occured in one of the threads", e);

    } catch (TimeoutException e) {
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
