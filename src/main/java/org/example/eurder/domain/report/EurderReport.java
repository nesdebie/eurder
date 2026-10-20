package org.example.eurder.domain.report;

import org.example.eurder.domain.Eurder;

import java.util.List;
import java.util.UUID;

public class EurderReport {
    private UUID id;
    private List<ItemReport> items;
    private double totalOrderPrice;

    public EurderReport(Eurder eurder) {
        this.id = eurder.getId();
        this.items = eurder.getItems().stream()
                .map(ItemReport::new)
                .toList();
        this.totalOrderPrice = this.items.stream()
                .mapToDouble(ItemReport::getTotalItemPrice)
                .sum();
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public UUID getId() {
        return id;
    }

    public List<ItemReport> getItems() {
        return items;
    }
}
