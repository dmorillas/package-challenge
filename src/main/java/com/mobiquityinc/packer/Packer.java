package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.parser.PackagesParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Packer {

    public static String pack(String filePath) throws APIException {
        PackagesParser parser = new PackagesParser();
        List<Package> packages = parser.parseFile(filePath);

        Packer packer = new Packer();
        StringBuilder sb = new StringBuilder();
        packages.stream().forEach(pkg -> {
            String result = packer.pickItems(pkg).stream().map(id -> id.toString()).collect(Collectors.joining(","));
            sb.append(result.isEmpty() ? "-" : result).append("\n");
        });

        return sb.toString();
    }

    public List<Integer> pickItems(Package pkg) {
        List<Item> items = pkg.getItems().stream()
                .filter(item -> item.getWeight() <= pkg.getWeight())
                .collect(Collectors.toList());

        // Using "The Knapsack Problem" algorithm to make the different calculations.
        int numItems = items.size();
        int[][] matrix = new int[numItems + 1][pkg.getWeight() + 1];

        for (int itemPosition = 1; itemPosition <= numItems; itemPosition++) {
            for (int weight = 0; weight <= pkg.getWeight(); weight++) {
                Item item = items.get(itemPosition - 1);

                if (item.getWeight() > weight) {
                    matrix[itemPosition][weight] = matrix[itemPosition - 1][weight];
                } else {
                    matrix[itemPosition][weight] = Math.max(matrix[itemPosition - 1][weight], matrix[itemPosition - 1][weight - item.getWeight()] + item.getCost());
                }
            }
        }

        int maxCost = matrix[numItems][pkg.getWeight()];
        int maxWeight = pkg.getWeight();
        List<Integer> itemsSolution = new ArrayList<>();

        // Taking the smallest weight with the highest cost.
        while(maxWeight > 0 && matrix[numItems][maxWeight - 1] == maxCost) {
            maxWeight--;
        }

        for(int itemPosition = numItems; itemPosition > 0; itemPosition--) {
            if(matrix[itemPosition][maxWeight] != matrix[itemPosition - 1][maxWeight]) {
                Item item = items.get(itemPosition - 1);

                itemsSolution.add(item.getIndex());
                maxWeight -= item.getWeight();
            }
        }

        return itemsSolution.stream().sorted().collect(Collectors.toList());
    }
}
