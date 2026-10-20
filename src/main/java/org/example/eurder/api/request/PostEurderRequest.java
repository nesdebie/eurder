package org.example.eurder.api.request;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.UUID;

public record PostEurderRequest(
        List<OrderLineDto> orders
) {
    public record OrderLineDto(UUID itemId, int amount) {}
}
