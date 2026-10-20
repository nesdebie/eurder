package org.example.eurder.api.dto.eurder.report;

import java.util.List;
import java.util.UUID;

public record EurderReportDto(
        UUID id,
        List<ItemReportDto> items,
        double totalOrderPrice
) {
}
