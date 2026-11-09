package org.example.eurder.feature.eurder.api.dto;

import org.example.eurder.feature.customer.domain.Customer;
import org.example.eurder.feature.eurder.domain.ItemGroup;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record EurderDto(
        UUID id,
        List<ItemGroupDto> items,
        BigDecimal totalPrice
) {}
