package ru.spbu.apcyb.svp.tasks.task1;

/**
 * Задание 1.
 */
public class Task1 {

  public static void main(String[] args) {

    try {
      ChangeMachine machine = new ChangeMachine();
      machine.countChangeOptions();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

}
