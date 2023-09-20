package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.spbu.apcyb.svp.tasks.task1.ChangeMachine;

/**
 * Тесты для задания 1.
 */
public class Task1Test {

  @Test
  public void test1() {

    int sum = 5;
    int[] coins = new int[]{3, 2};
    ChangeMachine machine = new ChangeMachine(sum, coins);
    int changeOptions = machine.countChangeOptions();
    assertEquals(1, changeOptions);
  }

  @Test
  public void test2() {

    int sum = 4;
    int[] coins = new int[]{2, 1};
    ChangeMachine machine = new ChangeMachine(sum, coins);
    int changeOptions = machine.countChangeOptions();
    assertEquals(3, changeOptions);
  }

}
