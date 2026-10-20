package org.example.eurder.domain;

import java.time.LocalDate;
import java.util.UUID;

public class ItemGroup {
    private final UUID itemId;
    private final String name;
    private final String description;
    private final int amount;
    private final double price;
    private final LocalDate shippingDate;

    public ItemGroup(Item item, int amount) {
        this.itemId = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.amount = amount;
        this.shippingDate = amount > item.getAmount() ?
                LocalDate.now().plusWeeks(1) : LocalDate.now().plusDays(1);
    }

    public UUID getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }
}
