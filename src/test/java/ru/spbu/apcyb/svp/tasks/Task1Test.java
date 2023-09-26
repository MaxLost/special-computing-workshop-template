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
    ChangeMachine machine = new ChangeMachine();
    int changeOptions = machine.countChangeOptions();
    assertEquals(1, changeOptions);
  }

  @Test
  void testSuccess2() {

    System.setIn(new ByteArrayInputStream("4\n2 1".getBytes()));
    ChangeMachine machine = new ChangeMachine();
    int changeOptions = machine.countChangeOptions();
    assertEquals(3, changeOptions);
  }

  @Test
  void testSuccess3() {

    System.setIn(new ByteArrayInputStream("40\n25 20 10 5".getBytes()));
    ChangeMachine machine = new ChangeMachine();
    int changeOptions = machine.countChangeOptions();
    assertEquals(11, changeOptions);
  }

  private void exceptionThrowingTest(String inputString, String expectedExceptionMessage) {

    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    Exception e = assertThrows(RuntimeException.class, () -> {
      ChangeMachine machine = new ChangeMachine();
    });
    assertEquals(expectedExceptionMessage, e.getMessage());
  }

  @Test
  void testChangeZero() {
    exceptionThrowingTest("0\n25 20 10 5",
        "Amount for change should be a positive integer.");
  }

  @Test
  void testNegativeAmountForChange() {
    exceptionThrowingTest("-1\n2 1",
        "Amount for change should be a positive integer.");
  }

  @Test
  void testAmountForChangeIsNotPositiveInteger() {
    exceptionThrowingTest("abc\n2 1",
        "Amount for change should be a positive integer.");
  }

  @Test
  void testAmountForChangeAsExpression() {
    exceptionThrowingTest("3+2\n2 1",
        "Amount for change should be a positive integer.");
  }

  @Test
  void testEmptyAmountForChangeInput() {
    exceptionThrowingTest("",
        "Incorrect input.");
  }

  @Test
  void testTooLargeAmountForChange() {
    exceptionThrowingTest("3000000000\n2 1",
        "Amount for change should be between 1 and 2 147 483 647.");
  }

  @Test
  void testTooFewChangeOptions() {
    exceptionThrowingTest("5\n ",
        "There should be at least 1 available denomination for change.");
  }

  @Test
  void testInvalidChangeOptionsInput() {
    exceptionThrowingTest("5\nf",
        "Only positive integers supported as change options.");
  }

  @Test
  void testNegativeChangeOption() {
    exceptionThrowingTest("30\n-2 1",
        "Only positive integers supported as change options.");
  }

  @Test
  void testTooLargeChangeOption() {
    exceptionThrowingTest("1000\n3000000000 1",
        "Change options should be between 1 and 2 147 483 647.");
  }

}
