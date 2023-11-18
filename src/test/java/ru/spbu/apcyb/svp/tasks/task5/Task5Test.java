package ru.spbu.apcyb.svp.tasks.task5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import ru.spbu.apcyb.svp.tasks.task4.MultiThreadComputer;

class Task5Test {

  @Test
  void shortFileTest() throws IOException {
    String[] args = new String[]{"src/test/resources/task5/short_file.txt",
        "src/test/resources/task5/short_file_result.txt"};
    WordFrequencyCounter.main(args);
    assertEquals(-1, Files.mismatch(
            Path.of("src/test/resources/task5/short_file_result_expected.txt"),
            Path.of("src/test/resources/task5/short_file_result.txt")
        )
    );
    assertTrue(Files.exists(Path.of("src/test/resources/task5/short_file_result/")));
  }

  @Test
  void invalidArgumentAmountTest() {
    String[] args = new String[] {"123", "123123", "13333111"};
    Exception e = assertThrows(IllegalArgumentException.class, () -> WordFrequencyCounter.main(args));
    assertEquals("Incorrect amount of arguments, check specification of program.", e.getMessage());
  }

}
