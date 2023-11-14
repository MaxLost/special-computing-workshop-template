package ru.spbu.apcyb.svp.tasks.task4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Task4Test {

  @Test
  void performanceTest() {

    System.out.println("Calculation of 1 tan(x):");
    String oneInput = "src/test/resources/task4/numbers_1.txt";
    String oneSingleOut = "src/test/resources/task4/results_1_S.txt";
    String oneMultiOut = "src/test/resources/task4/results_1_M.txt";

    MultiThreadComputer.computeTangentsWithPerfomanceLog(oneInput, oneSingleOut, oneMultiOut);

    System.out.println("Calculation of 100 tan(x):");
    String hundredInput = "src/test/resources/task4/numbers_100.txt";
    String hundredSingleOut = "src/test/resources/task4/results_100_S.txt";
    String hundredMultiOut = "src/test/resources/task4/results_100_M.txt";

    MultiThreadComputer.computeTangentsWithPerfomanceLog(hundredInput, hundredSingleOut,
        hundredMultiOut);

    System.out.println("Calculation of 1000000 tan(x):");
    String mullionInput = "src/test/resources/task4/numbers_1000000.txt";
    String mullionSingleOut = "src/test/resources/task4/results_1000000_S.txt";
    String mullionMultiOut = "src/test/resources/task4/results_1000000_M.txt";

    MultiThreadComputer.computeTangentsWithPerfomanceLog(mullionInput, mullionSingleOut,
        mullionMultiOut);
    assertTrue(true);
  }

  @Test
  void multiThreadMultiplicationTest() throws IOException {

    System.out.println("Calculation of 100 tan(x):");
    String hundredInput = "src/test/resources/task4/numbers_100.txt";
    String hundredSingleOut = "src/test/resources/task4/results_100_S.txt";
    String hundredMultiOut = "src/test/resources/task4/results_100_M.txt";

    MultiThreadComputer.computeTangentsWithPerfomanceLog(hundredInput, hundredSingleOut,
        hundredMultiOut);

    Path singleThreadResult = Path.of(hundredSingleOut);
    Path multiThreadResult = Path.of(hundredMultiOut);
    assertEquals(-1, Files.mismatch(singleThreadResult, multiThreadResult));
  }

}
