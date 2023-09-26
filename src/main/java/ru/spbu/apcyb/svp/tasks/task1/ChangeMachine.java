package ru.spbu.apcyb.svp.tasks.task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that implements solution for coin change problem with search of combinations for change.
 */
public class ChangeMachine {

  private final int sum;
  private final int[] coins;

  /**
   * Constructor that initializes problem from standard input.
   */
  public ChangeMachine() {

    try (Scanner input = new Scanner(System.in)) {

      System.out.println("Enter amount of money for change:");
      this.sum = getAmountForChangeFromInput(input);

      System.out.println("Enter available denominations for change:");
      this.coins = getChangeOptionsFromInput(input);
    }

  }

  private static int getAmountForChangeFromInput(Scanner input) {

    int sum;
    try {
      String sumAsString = input.nextLine();

      Pattern integerPattern = Pattern.compile("^[1-9]\\d*$");
      Matcher matcher = integerPattern.matcher(sumAsString);

      if (matcher.find()) {
        sum = Integer.parseInt(sumAsString);
      } else {
        throw new RuntimeException("Amount for change should be a positive integer.");
      }

    } catch (NoSuchElementException e) {
      throw new RuntimeException("Incorrect input.");
    } catch (NumberFormatException e) {
      throw new RuntimeException("Amount for change should be between 1 and 2 147 483 647.");
    }

    return sum;
  }

  private static int[] getChangeOptionsFromInput(Scanner input) {

    String[] coinsAsStrings = input.nextLine().split(" *");

    if (coinsAsStrings.length == 0) {
      throw new RuntimeException("There should be at least 1 available denomination for change.");
    }

    int[] coins = new int[coinsAsStrings.length];

    for (int i = 0; i < coinsAsStrings.length; i++) {
      try {
        Pattern integerPattern = Pattern.compile("^[1-9]\\d*$");
        Matcher matcher = integerPattern.matcher(coinsAsStrings[i]);

        if (matcher.find()) {
          coins[i] = Integer.parseInt(coinsAsStrings[i]);
        } else {
          throw new RuntimeException("Only positive integers supported as change options.");
        }

      } catch (NumberFormatException e) {
        throw new RuntimeException("Change options should be between 1 and 2 147 483 647.");
      }
    }

    return coins;
  }

  /**
   * Method for solving coin change problem.
   *
   * @return Amount of change combinations as value of function and in standard output, and unique
   *        set of such combinations in standard output.
   */
  public int countChangeOptions() {

    List<int[]> changeOptions = getChangeOptions(sum);
    HashSet<List<Integer>> uniqueOptions = new HashSet<>();

    for (int[] option : changeOptions) {
      List<Integer> optionAsList = Arrays.stream(option).boxed().toList();
      uniqueOptions.add(optionAsList);
    }

    List<String> optionsAsStrings = convertOptionsToString(uniqueOptions);
    System.out.println(
        "Amount of change combinations: " + optionsAsStrings.size() + "\nCombinations:");
    for (String option : optionsAsStrings) {
      System.out.println(option);
    }

    return optionsAsStrings.size();
  }

  private List<String> convertOptionsToString(Set<List<Integer>> options) {

    List<String> result = new ArrayList<>();

    for (List<Integer> option : options) {
      StringBuilder optionAsString = new StringBuilder("{");
      for (int i = 0; i < option.size(); i++) {
        for (int j = 0; j < option.get(i); j++) {
          optionAsString.append(coins[i]).append(", ");
        }
      }
      int lastCommaIndex = optionAsString.lastIndexOf(",");
      result.add(optionAsString.substring(0, lastCommaIndex) + "}");
    }

    return result;
  }

  private List<int[]> getChangeOptions(int s) {

    List<int[]> combinations = new ArrayList<>();

    if (s > 0) {

      for (int i = 0; i < coins.length; i++) {

        if (s == coins[i]) {

          int[] newCombination = new int[coins.length];
          newCombination[i] += 1;
          combinations.add(newCombination);
        } else {

          List<int[]> previousCombinations = getChangeOptions(s - coins[i]);

          for (int[] combination : previousCombinations) {
            combination[i]++;
          }
          combinations.addAll(previousCombinations);
        }
      }
    }

    return combinations;
  }

}
