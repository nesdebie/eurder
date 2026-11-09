package org.example.eurder.feature.eurder.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ItemGroupDto(
        UUID itemId,
        String itemName,
        BigDecimal unitPrice,
        int amount,
        BigDecimal totalPrice,
        LocalDate shippingDate
) {}
