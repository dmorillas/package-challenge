package com.mobiquityinc.model;

import java.util.List;
import java.util.Objects;

public class Package {
    private int weight;
    private List<Item> items;

    public Package(int weight, List<Item> items) {
        this.weight = weight;
        this.items = items;
    }

    public int getWeight() {
        return weight;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return weight == aPackage.weight &&
                Objects.equals(items, aPackage.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, items);
    }

    @Override
    public String toString() {
        return "Package{" +
                "weight=" + weight +
                ", items=" + items +
                '}';
    }
}
