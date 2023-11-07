package ru.spbu.apcyb.svp.tasks.task4;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;

public class Task4Test {

  @Test
  void perfomanceTest() {

    System.out.println("Calculation of 1 tan(x):");
    String[] args1 = new String[]{"src/test/resources/task4/numbers_1.txt",
        "src/test/resources/task4/results_1_S.txt", "src/test/resources/task4/results_1_M.txt"};

    MultithreadComputer.main(args1);

    System.out.println("Calculation of 100 tan(x):");
    String[] args2 = new String[]{"src/test/resources/task4/numbers_100.txt",
        "src/test/resources/task4/results_100_S.txt", "src/test/resources/task4/results_100_M.txt"};

    MultithreadComputer.main(args2);

    System.out.println("Calculation of 1000000 tan(x):");
    String[] args3 = new String[]{"src/test/resources/task4/numbers_1000000.txt",
        "src/test/resources/task4/results_1000000_S.txt",
        "src/test/resources/task4/results_1000000_M.txt"};

    MultithreadComputer.main(args3);
    assertTrue(true);
  }

}
