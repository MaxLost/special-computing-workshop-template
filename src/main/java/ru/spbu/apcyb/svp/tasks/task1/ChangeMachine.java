package ru.spbu.apcyb.svp.tasks.task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
      String sumAsString = input.nextLine();
      this.sum = Integer.parseInt(sumAsString);
      if (sum < 0) {
        throw new RuntimeException("Amount for change cannot be negative.");
      }

      System.out.println("Enter available denominations for change:");
      String[] coinsAsStrings = input.nextLine().split(" ");

      if (coinsAsStrings.length == 0) {
        throw new RuntimeException("There should be at least 1 available denomination for change.");
      }

      this.coins = new int[coinsAsStrings.length];

      for (int i = 0; i < coinsAsStrings.length; i++) {
        this.coins[i] = Integer.parseInt(coinsAsStrings[i]);
      }
    }

  }

  /**
   * Method for solving coin change problem.
   *
   * @return Amount of change combinations and unique set of such combinations in standard output.
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
      StringBuilder optionAsString = new StringBuilder();
      for (int i = 0; i < option.size(); i++) {
        for (int j = 0; j < option.get(i); j++) {
          optionAsString.append(coins[i]).append(", ");
        }
      }
      int lastCommaIndex = optionAsString.lastIndexOf(",");
      result.add(optionAsString.substring(0, lastCommaIndex));
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
