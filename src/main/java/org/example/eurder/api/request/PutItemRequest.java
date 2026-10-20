package org.example.eurder.api.request;

import java.util.UUID;

public record PutItemRequest(
        UUID id,
        String name,
        String description,
        double price,
        int amount
) {}
