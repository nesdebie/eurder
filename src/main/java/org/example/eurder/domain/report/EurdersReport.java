package org.example.eurder.domain.report;


import org.example.eurder.domain.Eurder;

import java.util.List;

public class EurdersReport {
    private List<EurderReport> eurders;
    private double ordersTotalPrice;

    public EurdersReport(List<Eurder> eurders) {
        this.eurders = eurders.stream()
                .map(EurderReport::new)
                .toList();
        this.ordersTotalPrice = this.eurders.stream()
                .mapToDouble(EurderReport::getTotalOrderPrice)
                .sum();
    }

    public List<EurderReport> getEurders() {
        return eurders;
    }

    public double getOrdersTotalPrice() {
        return ordersTotalPrice;
    }
}