package org.example.eurder.api.dto;

import java.util.UUID;

public record EurderedItemDto(
        UUID id,
        String name,
        String description,
        double price
) {}
