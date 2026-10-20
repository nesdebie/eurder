package org.example.eurder.api.request;

import java.util.List;
import java.util.UUID;

public record PostEurderRequest(
        List<OrderLineDto> eurders
) {
    public record OrderLineDto(UUID itemId, int amount) {}
}
