package com.mobiquityinc.parser;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.Package;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PackagesParser {
    private static final String REGULAR_EXPRESSION = "\\((\\d+),(\\d+\\.\\d{1,2}),€(\\d+)\\)";
    private static final int MAX_WEIGHT_PER_PACKAGE = 100 * 100;
    private static final int MAX_NUMBER_ITEMS = 15;
    private static final int MAX_WEIGHT_PER_ITEM = 100 * 100;
    private static final int MAX_COST_PER_ITEM = 100;

    public List<Package> parseFile(String filePath) throws APIException {
        Path path = Paths.get(filePath);
        checkPath(path);

        try (Stream<String> stream = Files.lines(path)) {
            List<Package> packages = stream
                    .map(line -> parseLine(line))
                    .collect(Collectors.toList());

            if(packages == null || packages.size() == 0) {
                throw new APIException("The file can not be empty");
            }

            return packages;
        } catch (IOException e) {
            throw new APIException(e);
        }
    }

    private void checkPath(Path path) throws APIException {
        if(!Files.exists(path) || Files.isDirectory(path)) {
            throw new APIException("The provided path must not only exist but be a file: " + path.toString());
        }
    }

    private Package parseLine(String line) throws APIException {
        Pattern pattern = Pattern.compile(REGULAR_EXPRESSION);

        String[] elements = line.split(":");
        if(elements.length != 2) {
            throw new APIException("I think you wrote the line in a wrong fromat. What I expect is -> <package_weight> : <list of items>");
        }

        int weight = 0;
        try {
            weight = Integer.parseInt(elements[0].trim()) * 100;
        } catch (NumberFormatException e) {
            throw new APIException("The weight for the package must be a number. What I found: " + elements[0].trim());
        }

        if(weight > MAX_WEIGHT_PER_PACKAGE) {
            throw new APIException("The max weight that a package can take is ≤ 100. Found: " + weight);
        }

        Matcher matcher = pattern.matcher(elements[1].trim());
        List<Item> items = new ArrayList<>();
        while(matcher.find()) {
            int index = Integer.parseInt(matcher.group(1));
            int itemWeight = (int)(Double.parseDouble(matcher.group(2)) * 100);
            int cost = Integer.parseInt(matcher.group(3));

            if(itemWeight > MAX_WEIGHT_PER_ITEM) {
                throw new APIException("Max weight of an item is ≤ 100. Found: " + itemWeight);
            }

            if(cost > MAX_COST_PER_ITEM) {
                throw new APIException("Max cost of an item is ≤ 100. Found: " + cost);
            }

            items.add(new Item(index, itemWeight, cost));
        }

        if(items.size() == 0 || items.size() > MAX_NUMBER_ITEMS) {
            throw new APIException("There might be 1 and up to 15 items you need to choose from. Found package with " + items.size());
        }

        return new Package(weight, items);
    }
}
