package org.example.eurder.api.request;

public record PostItemRequest (
        String name,
        String description,
        double price,
        int amount
) {}
