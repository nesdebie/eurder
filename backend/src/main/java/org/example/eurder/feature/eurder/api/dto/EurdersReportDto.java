package org.example.eurder.feature.eurder.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record EurdersReportDto(
        List<EurderDto> eurders,
        BigDecimal totalPrice
) {
}
