package org.example.eurder.feature.eurder.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EurderItemsRequest(
        @NotEmpty(message = "An eurder must contain at least one item")
        @Size(max = 100, message = "An eurder can contain at most 100 items")
        List<@NotNull(message = "Item must not be null") @Valid EurderSingleItemRequest> items
) {
}