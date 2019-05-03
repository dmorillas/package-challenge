package com.mobiquityinc.parser;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.Package;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.currentThread;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PackagesParserTest {
    private PackagesParser packagesParser;

    @Before
    public void setUp() {
        packagesParser = new PackagesParser();
    }

    @Test(expected = APIException.class)
    public void testIfFileDoesNotExistThrowsException() {
        packagesParser.parseFile("non_existing_file.txt");
    }

    @Test(expected = APIException.class)
    public void testIfFileIsEmptyThrowsException() {
        String path = getPathForResource("empty.txt");

        packagesParser.parseFile(path);
    }

    private String getPathForResource(String resource) {
        return currentThread().getContextClassLoader().getResource(resource).getFile();
    }

    @Test(expected = APIException.class)
    public void testIfPackageWeightIsMissingThrowsException() {
        String path = getPathForResource("wrong_line_format.txt");

        packagesParser.parseFile(path);
    }

    @Test(expected = APIException.class)
    public void testIfPackageWeightNotNumberThrowsException() {
        String path = getPathForResource("weight_not_number.txt");

        packagesParser.parseFile(path);
    }

    @Test(expected = APIException.class)
    public void testIfPackageWeightExceedsMaxThrowsException() {
        String path = getPathForResource("weight_exceeds_max.txt");

        packagesParser.parseFile(path);
    }

    @Test(expected = APIException.class)
    public void testIfItemWeightExceedsMaxThrowsException() {
        String path = getPathForResource("item_weight_exceeds_max.txt");

        packagesParser.parseFile(path);
    }

    @Test(expected = APIException.class)
    public void testIfItemCostExceedsMaxThrowsException() {
        String path = getPathForResource("item_cost_exceeds_max.txt");

        packagesParser.parseFile(path);
    }

    @Test(expected = APIException.class)
    public void testIfMoreThanMaxItemsAllowedThrowsException() {
        String path = getPathForResource("max_number_items_exceeded.txt");

        packagesParser.parseFile(path);
    }

    @Test(expected = APIException.class)
    public void testIfNoItemsThrowsException() {
        String path = getPathForResource("no_items.txt");

        packagesParser.parseFile(path);
    }

    @Test
    public void testIfEverythingRightWorksAsExpected() {
        String path = getPathForResource("input.txt");
        List<Package> packages = packagesParser.parseFile(path);

        List<Package> expectedPackages = new ArrayList<>();
        Package pkg = new Package(8100, Arrays.asList(new Item(1,5338, 45), new Item(2,8862,98)));
        expectedPackages.add(pkg);

        assertThat(packages, is(expectedPackages));
    }
}
