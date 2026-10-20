package org.example.eurder.api.dto.eurder.report;

import java.util.List;

public record EurdersReportDto(
        List<EurderReportDto> eurders,
        double ordersTotalPrice
) {
}
