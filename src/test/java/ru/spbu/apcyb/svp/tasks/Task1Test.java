package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;
import ru.spbu.apcyb.svp.tasks.task1.ChangeMachine;

/**
 * Тесты для задания 1.
 */
class Task1Test {

  @Test
  void test1() {

    System.setIn(new ByteArrayInputStream("5\n3 2".getBytes()));

    try {

      ChangeMachine machine = new ChangeMachine();
      int changeOptions = machine.countChangeOptions();
      assertEquals(1, changeOptions);

    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void test2() {

    System.setIn(new ByteArrayInputStream("4\n2 1".getBytes()));

    try {

      ChangeMachine machine = new ChangeMachine();
      int changeOptions = machine.countChangeOptions();
      assertEquals(3, changeOptions);

    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void test3() {

    System.setIn(new ByteArrayInputStream("-1\n2 1".getBytes()));

    try {
      ChangeMachine machine = new ChangeMachine();
      fail();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

}
