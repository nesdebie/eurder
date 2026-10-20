package org.example.eurder.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.IntPredicate;

public enum ItemInventory {
    STOCK_LOW("STOCK_LOW", amount -> amount < 5),
    STOCK_MEDIUM("STOCK_MEDIUM", amount -> amount >= 5 && amount < 10),
    STOCK_HIGH("STOCK_HIGH", amount -> amount >= 10);

    private final String value;
    private final IntPredicate range;

    ItemInventory(String value, IntPredicate range) {
        this.value = value;
        this.range = range;
    }

    public String value() { return value; }

    public boolean matches(int amount) { return range.test(amount); }

    public static Optional<ItemInventory> fromValue(String value) {
        return Arrays.stream(values())
                .filter(inv -> inv.value.equalsIgnoreCase(value))
                .findFirst();
    }
}