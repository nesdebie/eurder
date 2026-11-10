package org.example.eurder.feature.item.service;

import java.util.Arrays;

public enum ItemInventory {
    STOCK_LOW(5),
    STOCK_MEDIUM(10),
    STOCK_HIGH(Integer.MAX_VALUE);

    private final int upperBoundExclusive;

    ItemInventory(int upperBoundExclusive) {
        this.upperBoundExclusive = upperBoundExclusive;
    }

    public static ItemInventory of(int amount) {
        return Arrays.stream(values())
                .filter(urgency -> amount < urgency.upperBoundExclusive)
                .findFirst()
                .orElseThrow();
    }
}