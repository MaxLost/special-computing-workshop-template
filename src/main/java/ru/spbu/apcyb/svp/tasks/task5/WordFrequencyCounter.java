package ru.spbu.apcyb.svp.tasks.task5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class for
 */
public class WordFrequencyCounter {

  private static final Logger logger = Logger.getLogger("Word.frequency.counter");

  public static void main(String[] args) {

    if (args.length != 2) {
      throw new IllegalArgumentException(
          "Incorrect amount of arguments, check specification of program.");
    }

    try {
      Path inputPath = Path.of(args[0]);
      Path outputPath = Path.of(args[1]);

      Map<String, Long> words = countWords(inputPath);

      writeResultInFile(outputPath, words);

    } catch (InvalidPathException e) {
      throw new IllegalArgumentException(e.getMessage());

    } catch (Exception e) {
      String stackTrace = Arrays.stream(e.getStackTrace())
          .map(StackTraceElement::toString)
          .collect(Collectors.joining("\n"));
      logger.severe("One of the threads was interrupted at:\n" + stackTrace);
    }
  }

  private static void writeResultInFile(Path outputPath, Map<String, Long> words) {

    ExecutorService executorService = Executors.newFixedThreadPool(8);

    try (BufferedWriter output = Files.newBufferedWriter(outputPath)) {

      String pathBase = outputPath.toString().substring(0, outputPath.toString().lastIndexOf("."));
      Path directoryPath = Path.of(Path.of(pathBase) + File.separator);
      if (!Files.exists(directoryPath)) {
        Files.createDirectory(directoryPath);
      }

      for (Entry<String, Long> word : words.entrySet().stream()
          .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder())).toList()) {

        String result = word.getKey() + " " + word.getValue() + "\n";
        output.write(result);

        Path wordFilePath = Path.of(pathBase + File.separator + word.getKey() + ".txt");
        writeWordToSeparateFile(wordFilePath, word.getKey(), word.getValue());
      }

    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());

    } finally {
      executorService.shutdown();
    }
  }

  private static void writeWordToSeparateFile(Path path, String word, Long repetitions) {

    CompletableFuture.runAsync(() -> {

      try (BufferedWriter wordFile = Files.newBufferedWriter(path)) {
        for (int i = 0; i < repetitions; i++) {
          wordFile.write(word + " ");
        }

      } catch (IOException e) {
        String stackTrace = Arrays.stream(e.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.joining("\n"));
        logger.severe("Cannot open file for word.\n" + stackTrace);
      }
    });
  }

  private static Map<String, Long> countWords(Path inputPath) {

    String pattern = "\\p{Punct}";

    try (Stream<String> lines = Files.lines(inputPath).parallel()) {

      return lines.flatMap(line -> Arrays.stream(line.split(" ")))
          .map(word -> word.trim().replaceAll(pattern, "").toLowerCase())
          .filter(word -> !word.isEmpty())
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    } catch (IOException e) {
      throw new RuntimeException("Error during input file reading.");
    }
  }

}
