package com.mobiquityinc.packer;

import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.Package;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PackerTest {
    private Packer packer;

    @Before
    public void setUp() {
        packer = new Packer();
    }

    @Test
    public void testIfNoItemsMatchCriteriaReturnsEmptyList() {
        Package pkg = new Package(1000, Arrays.asList(new Item(1, 1200, 3)));

        List<Integer> items = packer.pickItems(pkg);

        assertThat(items.size(), is(0));
    }

    @Test
    public void testIfOnlyOneElementReturnsTheElement() {
        Package pkg = new Package(1000, Arrays.asList(new Item(1, 900, 3)));

        List<Integer> items = packer.pickItems(pkg);

        assertThat(items.size(), is(1));
        assertThat(items.get(0), is(1));
    }

    @Test
    public void testIfTwoElementsAndOneDoesNotMatchReturnsTheElementThatMatchesCriteria() {
        Package pkg = new Package(1000, Arrays.asList(new Item(1, 1200, 3), new Item(2, 900, 3)));

        List<Integer> items = packer.pickItems(pkg);

        assertThat(items.size(), is(1));
        assertThat(items.get(0), is(2));
    }

    @Test
    public void testIfTwoElementsReturnsTheElementThatMatchesCriteria() {
        Package pkg = new Package(1000, Arrays.asList(new Item(1, 800, 5), new Item(2, 900, 3)));

        List<Integer> items = packer.pickItems(pkg);

        assertThat(items.size(), is(1));
        assertThat(items.get(0), is(1));
    }

    @Test
    public void testIfManyElementsReturnsExpected() {
        Package pkg = new Package(7500,
                Arrays.asList(
                        new Item(1, 8531, 29),
                        new Item(2, 1455, 74),
                        new Item(3, 398, 16),
                        new Item(4, 2624, 55),
                        new Item(5, 6369, 52),
                        new Item(6, 7625, 75),
                        new Item(7, 6002, 74),
                        new Item(8, 9318, 35),
                        new Item(9, 8995, 78)
                )
        );

        List<Integer> items = packer.pickItems(pkg);

        List<Integer> expectedItems = Arrays.asList(2, 7);
        assertThat(items, is(expectedItems));
    }


    @Test
    public void testWhenSeveralCombinationsWithSameCostReturnsLessWeight() {
        Package pkg = new Package(5600,
                Arrays.asList(
                        new Item(1, 9072, 13),
                        new Item(2, 3380, 40),
                        new Item(3, 4315, 10),
                        new Item(4, 3797, 16),
                        new Item(5, 4681, 36),
                        new Item(6, 4877, 79),
                        new Item(7, 8180, 45),
                        new Item(8, 1936, 79),
                        new Item(9, 676, 64)
                )
        );

        List<Integer> items = packer.pickItems(pkg);

        List<Integer> expectedItems = Arrays.asList(8, 9);
        assertThat(items, is(expectedItems));
    }
}
