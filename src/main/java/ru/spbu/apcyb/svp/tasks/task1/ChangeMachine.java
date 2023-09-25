package ru.spbu.apcyb.svp.tasks.task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ChangeMachine {

  private final int sum;
  private final int[] coins;

  public ChangeMachine() throws Exception {

    try (Scanner input = new Scanner(System.in)) {

      System.out.println("Введите сумму для размена:");
      this.sum = input.nextInt();
      if (sum < 0) {
        throw new Exception("Сумма для размена не может быть отрицательной.");
      }
      input.nextLine();

      System.out.println("Введите номиналы для размена:");
      String[] coinsAsStrings = input.nextLine().split(" ");

      if (coinsAsStrings.length == 0) {
        throw new Exception("Укажите хотя бы один номинал для размена.");
      }

      this.coins = new int[coinsAsStrings.length];

      for (int i = 0; i < coinsAsStrings.length; i++) {
        this.coins[i] = Integer.parseInt(coinsAsStrings[i]);
      }
    }

  }

  public int countChangeOptions() {

    List<int[]> changeOptions = getChangeOptions(sum);
    HashSet<List<Integer>> uniqueOptions = new HashSet<>();

    for (int[] option : changeOptions) {
      List<Integer> optionAsList = Arrays.stream(option).boxed().toList();
      uniqueOptions.add(optionAsList);
    }

    List<String> optionsAsStrings = convertOptionsToString(uniqueOptions);
    System.out.println("Количество комбинаций: " + optionsAsStrings.size());
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
          int j = i;
          List<int[]> previousCombinations = getChangeOptions(s - coins[i]);
          previousCombinations.forEach(combination -> combination[j] += 1);
          combinations.addAll(previousCombinations);
        }
      }
    }

    return combinations;
  }

}
