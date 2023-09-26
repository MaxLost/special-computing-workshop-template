package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;
import ru.spbu.apcyb.svp.tasks.task1.ChangeMachine;

/**
 * Тесты для задания 1.
 */
class Task1Test {

  @Test
  void testSuccess1() {

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
  void testSuccess2() {

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
  void testSuccess3() {

    System.setIn(new ByteArrayInputStream("40\n25 20 10 5".getBytes()));

    try {

      ChangeMachine machine = new ChangeMachine();
      int changeOptions = machine.countChangeOptions();
      assertEquals(11, changeOptions);

    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void testChangeZero() {

    System.setIn(new ByteArrayInputStream("0\n25 20 10 5".getBytes()));

    try {

      ChangeMachine machine = new ChangeMachine();
      int changeOptions = machine.countChangeOptions();
      assertEquals(1, changeOptions);

    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void testNegativeAmountForChange() {

    System.setIn(new ByteArrayInputStream("-1\n2 1".getBytes()));

    try {
      ChangeMachine machine = new ChangeMachine();
      fail();
    } catch (RuntimeException e) {
      assertEquals("Amount for change cannot be negative.", e.getMessage());
    }
  }

  @Test
  void testTooFewChangeOptions() {

    System.setIn(new ByteArrayInputStream("5\n ".getBytes()));

    try {
      ChangeMachine machine = new ChangeMachine();
      fail();
    } catch (RuntimeException e) {
      assertEquals("There should be at least 1 available denomination for change.", e.getMessage());
    }
  }

  @Test
  void testInvalidAmountInput() {

    System.setIn(new ByteArrayInputStream("f\n ".getBytes()));
    assertThrows(RuntimeException.class, () -> {
      ChangeMachine machine = new ChangeMachine();
    }, "Amount for change should be a non-negative integer.");
  }

  @Test
  void testInvalidChangeOptionsInput() {

    System.setIn(new ByteArrayInputStream("5\nf".getBytes()));
    assertThrows(RuntimeException.class, () -> {
      ChangeMachine machine = new ChangeMachine();
    }, "Change options should be positive integers.");
  }

  @Test
  void testTooLargeAmountForChange() {

    System.setIn(new ByteArrayInputStream("3000000000\n2 1".getBytes()));
    assertThrows(RuntimeException.class, () -> {
      ChangeMachine machine = new ChangeMachine();
    }, "Only non-negative integer amount supported.");
  }

  @Test
  void testNegativeChangeOption() {

    System.setIn(new ByteArrayInputStream("30\n-2 1".getBytes()));
    assertThrows(RuntimeException.class, () -> {
      ChangeMachine machine = new ChangeMachine();
    }, "Change options should be positive.");
  }

  @Test
  void testTooLargeChangeOption() {

    System.setIn(new ByteArrayInputStream("1000\n3000000000 1".getBytes()));
    assertThrows(RuntimeException.class, () -> {
      ChangeMachine machine = new ChangeMachine();
    }, "Change options should be positive.");
  }

}
