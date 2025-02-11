package ru.spbu.apcyb.svp.tasks.task5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
 * Class for getting word frequencies in text and saving result to file.
 */
public class WordFrequencyCounter {

  private static final Logger logger = Logger.getLogger("Word.frequency.counter");

  /**
   * Main method for getting word frequencies from file.
   *
   * @param args First argument - path to file with text for getting word frequencies.
   *             Second argument - path to file where result should be saved.
   */
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
    List<CompletableFuture<Void>> futures = new ArrayList<>();

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
        futures.add(
            writeWordToSeparateFile(wordFilePath, word.getKey(), word.getValue(), executorService));
      }

    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());

    } finally {
      futures.forEach(CompletableFuture::join);
      executorService.shutdown();
    }
  }

  private static CompletableFuture<Void> writeWordToSeparateFile(Path path, String word, Long repetitions,
      ExecutorService executorService) {

    return CompletableFuture.runAsync(() -> {

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
    }, executorService);
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
