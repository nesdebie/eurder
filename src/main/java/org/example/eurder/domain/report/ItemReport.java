package org.example.eurder.domain.report;

import org.example.eurder.domain.ItemGroup;

public class ItemReport {
    private final String name;
    private final int amount;
    private final double totalItemPrice;

    public ItemReport(ItemGroup item) {
        this.name = item.getName();
        this.amount = item.getAmount();
        this.totalItemPrice = item.getPrice() * item.getAmount();
    }

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
