package org.example.eurder.api.dto.eurder.report;

public record ItemReportDto(
        String name,
        int amount,
        double totalItemPrice
) {
}
