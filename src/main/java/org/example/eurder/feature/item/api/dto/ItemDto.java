package org.example.eurder.feature.item.api.dto;

import java.util.UUID;

public record ItemDto(
        UUID id,
        String name,
        String description,
        double price,
        int amount
) {}
