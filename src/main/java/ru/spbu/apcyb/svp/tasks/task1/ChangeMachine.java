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

    String sumAsString = null;
    int sum;

    try {
      sumAsString = input.nextLine();
      sum = Integer.parseInt(sumAsString);

    } catch (NoSuchElementException e) {
      throw new RuntimeException("Incorrect input.");

    } catch (NumberFormatException e) {
      Pattern integerPattern = Pattern.compile("\\d");
      Matcher matcher = integerPattern.matcher(sumAsString);
      if (matcher.find()) {
        throw new RuntimeException("Only non-negative integer amount supported.");
      } else {
        throw new RuntimeException("Amount for change should be a non-negative integer.");
      }
    }

    if (sum < 0) {
      throw new RuntimeException("Amount for change cannot be negative.");
    }

    return sum;
  }

  private static int[] getChangeOptionsFromInput(Scanner input) {

    String[] coinsAsStrings = input.nextLine().split(" ");

    if (coinsAsStrings.length == 0) {
      throw new RuntimeException("There should be at least 1 available denomination for change.");
    }

    int[] coins = new int[coinsAsStrings.length];

    for (int i = 0; i < coinsAsStrings.length; i++) {
      try {
        coins[i] = Integer.parseInt(coinsAsStrings[i]);
        if (coins[i] <= 0) {
          throw new RuntimeException("Change options should be positive.");
        }

      } catch (NumberFormatException e) {
        Pattern integerPattern = Pattern.compile("\\d");
        Matcher matcher = integerPattern.matcher(coinsAsStrings[i]);

        if (matcher.find()) {
          throw new RuntimeException("Only positive integers supported as change options.");
        } else {
          throw new RuntimeException("Change options should be positive integers.");
        }
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

    if (sum > 0) {

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

    } else {

      List<String> optionsAsStrings = new ArrayList<>(List.of(" "));
      System.out.println(
          "Amount of change combinations: " + optionsAsStrings.size() + "\nCombinations:\n{ }");
      return 1;
    }
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
