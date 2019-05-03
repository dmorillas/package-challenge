package com.mobiquityinc.model;

import java.util.Objects;

public class Item implements Comparable {
    private int index;
    private int weight;
    private int cost;

    public Item() {
        this.index = 0;
        this.weight = 0;
        this.cost = 0;
    }

    public Item(int index, int weight, int cost) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }

    public int getIndex() {
        return index;
    }

    public int getWeight() {
        return weight;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public int compareTo(Object o) {
        Item other = (Item)o;

        return Integer.compare(cost, other.getCost());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return index == item.index &&
                weight == item.weight &&
                cost == item.cost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, weight, cost);
    }

    @Override
    public String toString() {
        return "Package{" +
                "index=" + index +
                ", weight=" + weight +
                ", cost=" + cost +
                '}';
    }
}
