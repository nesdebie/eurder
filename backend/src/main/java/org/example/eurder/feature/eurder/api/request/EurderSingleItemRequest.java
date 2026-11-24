package org.example.eurder.feature.eurder.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record EurderSingleItemRequest(
        @NotNull(message = "Item ID is required")
        @Schema(description = "Id of the item to order", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be more than 0")
        @Schema(description = "Number of items to buy", example = "10")
        Integer amount
) {
}